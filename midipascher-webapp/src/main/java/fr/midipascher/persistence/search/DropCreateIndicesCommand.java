package fr.midipascher.persistence.search;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * louis.gueye@gmail.com
 */
public class DropCreateIndicesCommand {

  public void execute(final Client client, String configFormat) throws IOException {
    File rootFolder = new ClassPathResource("elasticsearch").getFile();
    List<IndexConfiguration> indicesConfiguration = scanIndexConfigurations(rootFolder, configFormat);
    for (IndexConfiguration indexConfiguration : indicesConfiguration) {
      String name = indexConfiguration.getName();
      if (client.admin().indices().prepareExists(name).execute().actionGet().exists()) {

        DeleteIndexResponse
            deleteIndexResponse =
            client.admin().indices().prepareDelete(name).execute().actionGet();
        if (!deleteIndexResponse.acknowledged()) {
          throw new RuntimeException("Failed to delete index '" + name + "'");
        }
      }
      String indexConfigLocation = indexConfiguration.getConfigLocation();
      String
          settings =
          Resources.toString(Resources.getResource(indexConfigLocation), Charsets.UTF_8);
      CreateIndexResponse
          createIndexResponse =
          client.admin().indices().prepareCreate(name).setSettings(settings).execute().actionGet();
      if (!createIndexResponse.acknowledged()) {
        throw new RuntimeException("Failed to create index '" + name + "'");
      }

      List<MappingConfiguration>
          mappingsConfiguration =
          indexConfiguration.getMappingConfigurations();
      for (MappingConfiguration mappingConfiguration : mappingsConfiguration) {
        String type = mappingConfiguration.getType();
        String mappingLocation = mappingConfiguration.getLocation();
        String
            mappingSource =
            Resources.toString(Resources.getResource(mappingLocation), Charsets.UTF_8);
        PutMappingResponse
            putMappingResponse =
            client.admin().indices().preparePutMapping(name).setSource(mappingSource).setType(type)
                .execute().actionGet();
        if (!putMappingResponse.acknowledged()) {
          throw new RuntimeException(
              "Failed to put mapping '" + type + "' for index '" + name + "'");
        }
      }


    }
  }

  List<IndexConfiguration> scanIndexConfigurations(File rootFolder, String configFormat) throws IOException {
    File[] folders = rootFolder.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
    List<IndexConfiguration> indexConfigurations = Lists.newArrayList();
    for (File folder : folders) {
      IndexConfiguration indexConfiguration = newIndexConfiguration(folder, configFormat);
      indexConfigurations.add(indexConfiguration);
    }

    return indexConfigurations;
  }

  IndexConfiguration newIndexConfiguration(File indexDirectory, String configFormat) {
    IndexConfiguration indexConfiguration = new IndexConfiguration();
    String folderPath = indexDirectory.getPath();
    String name = folderPath.substring(folderPath.lastIndexOf('/') + 1, folderPath.length());
    indexConfiguration.setName(name);
    String configLocation = folderPath + "/_settings." + configFormat;
    indexConfiguration.setConfigLocation(configLocation);
    List<MappingConfiguration>
        mappingConfigurations =
        mappingConfigurations(indexDirectory, configFormat);
    indexConfiguration.setMappingConfigurations(mappingConfigurations);
    return indexConfiguration;
  }

  private List<MappingConfiguration> mappingConfigurations(File indexDirectory, String configFormat) {
    Collection<File> indexFiles = FileUtils.listFiles(indexDirectory, new String[]{configFormat}, true);
    Iterator<File> indexFilesIterator = indexFiles.iterator();
    while (indexFilesIterator.hasNext()) {
      File file = indexFilesIterator.next();
      if (file.getAbsolutePath().contains("_settings")) indexFilesIterator.remove();
    }
    List<MappingConfiguration> mappingConfigurations = Lists.newArrayList();
    for (File mapping : indexFiles) {
      MappingConfiguration mappingConfiguration = newMappingConfiguration(mapping, configFormat);
      mappingConfigurations.add(mappingConfiguration);
    }
    return mappingConfigurations;
  }

  MappingConfiguration newMappingConfiguration(File mapping, String format) {
    MappingConfiguration mappingConfiguration = new MappingConfiguration();
    String mappingPath = mapping.getPath();
    String type = mappingPath.substring(mappingPath.lastIndexOf('/') + 1, mappingPath.indexOf("." + format));
    mappingConfiguration.setType(type);
    mappingConfiguration.setLocation(mappingPath);
    return mappingConfiguration;
  }


}
