package fr.midipascher.persistence.search;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.FileUtils.*;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 21/09/12
 * Time: 01:12
 * To change this template use File | Settings | File Templates.
 */
public class DropCreateIndicesCommand {

    public void execute(final Client client) throws IOException {
        List<IndexConfiguration> indicesConfiguration = scanIndicesConfiguration();
        for (IndexConfiguration indexConfiguration : indicesConfiguration) {
            String name = indexConfiguration.getName();
            if (client.admin().indices().prepareExists(name).execute().actionGet().exists()) {

                DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(name).execute().actionGet();
                if (!deleteIndexResponse.acknowledged()) throw new RuntimeException("Failed to delete index '" + name + "'");
            }
            String indexConfigLocation = indexConfiguration.getConfigLocation();
            String settings = Resources.toString(Resources.getResource(indexConfigLocation), Charsets.UTF_8);
            CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(name).setSettings(settings).execute().actionGet();
            if (!createIndexResponse.acknowledged()) throw new RuntimeException("Failed to create index '" + name + "'");

            List<MappingConfiguration> mappingsConfiguration = indexConfiguration.getMappingsConfiguration();
            for (MappingConfiguration mappingConfiguration : mappingsConfiguration) {
                String type = mappingConfiguration.getType();
                String mappingLocation = mappingConfiguration.getLocation();
                String mappingSource = Resources.toString(Resources.getResource(mappingLocation), Charsets.UTF_8);
                PutMappingResponse putMappingResponse = client.admin().indices().preparePutMapping(name).setSource(mappingSource).setType(type).execute().actionGet();
                if (!putMappingResponse.acknowledged()) throw new RuntimeException("Failed to put mapping '" + type + "' for index '" + name + "'");
            }


        }
    }

    List<IndexConfiguration> scanIndicesConfiguration() throws IOException {
        Collection files = listFiles(new ClassPathResource("elasticsearch").getFile(), new String[]{"json"}, true);
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
