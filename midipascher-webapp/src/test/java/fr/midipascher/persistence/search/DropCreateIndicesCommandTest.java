package fr.midipascher.persistence.search;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.IndicesExistsResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * User: lgueye Date: 21/09/12 Time: 10:37
 */
public class DropCreateIndicesCommandTest {

  private DropCreateIndicesCommand underTest;
  private FileHelper fileHelper;

  @Before
  public void setUp() throws Exception {
    fileHelper = mock(FileHelper.class);
    underTest = new DropCreateIndicesCommand(fileHelper);
  }

  @Test
  public void scanIndexConfigurationsITTestShouldSucceed() throws IOException {
    underTest = new DropCreateIndicesCommand(new FileHelper());
    File rootFolder = new ClassPathResource("/testelasticsearchlayout").getFile();
    List<IndexConfiguration>
        indexConfigurations =
        underTest.scanIndexConfigurations(rootFolder, "json");
    assertNotNull(indexConfigurations);
    assertEquals(2, indexConfigurations.size());
    Collection<String>
        indexNames =
        Collections2.transform(indexConfigurations, new Function<IndexConfiguration, String>() {
          @Override
          public String apply(IndexConfiguration input) {
            return input.getName();
          }
        });
    assertEquals(2, indexNames.size());
    assertTrue(indexNames.contains("index1"));
    assertTrue(indexNames.contains("index2"));

    Collection<String>
        indexConfigLocations =
        Collections2.transform(indexConfigurations, new Function<IndexConfiguration, String>() {
          @Override
          public String apply(IndexConfiguration input) {
            return input.getConfigLocation();
          }
        });

    assertEquals(2, indexConfigLocations.size());

    for (String indexConfigLocation : indexConfigLocations) {
      assertTrue(Pattern.matches(".*index(\\d)/_settings\\.json$", indexConfigLocation));
    }

    Collection<IndexConfiguration>
        index1Configuration =
        Collections2.filter(indexConfigurations, new Predicate<IndexConfiguration>() {
          @Override
          public boolean apply(IndexConfiguration input) {
            return "index1".equals(input.getName());

          }

        });

    List<MappingConfiguration>
        index1MappingConfigurations =
        index1Configuration.iterator().next().getMappingConfigurations();
    Collection<String>
        index1Mappings =
        Collections2
            .transform(index1MappingConfigurations, new Function<MappingConfiguration, String>() {
              @Override
              public String apply(MappingConfiguration input) {
                return input.getType();
              }
            });

    assertEquals(3, index1Mappings.size());
    assertTrue(index1Mappings.contains("mapping11"));
    assertTrue(index1Mappings.contains("mapping12"));
    assertTrue(index1Mappings.contains("mapping13"));

    Collection<IndexConfiguration>
        index2Configuration =
        Collections2.filter(indexConfigurations, new Predicate<IndexConfiguration>() {
          @Override
          public boolean apply(IndexConfiguration input) {
            return "index2".equals(input.getName());

          }

        });

    List<MappingConfiguration>
        index2MappingConfigurations =
        index2Configuration.iterator().next().getMappingConfigurations();
    Collection<String>
        index2Mappings =
        Collections2
            .transform(index2MappingConfigurations, new Function<MappingConfiguration, String>() {
              @Override
              public String apply(MappingConfiguration input) {
                return input.getType();
              }
            });
    assertEquals(1, index2Mappings.size());
    assertTrue(index2Mappings.contains("mapping21"));

  }

  @Test
  public void newMappingConfigurationShouldSucceed() {

    final String rootFolderName = "elasticsearch";
    final String pathToIndex2 = "/some/path/to/index2";
    String configFormat = "json";
    File mapping = mock(File.class);
    final String type = "mapping21";
    final String location = "/" + rootFolderName + pathToIndex2 + "/" + type + "." + configFormat;
    when(mapping.getPath()).thenReturn(location);
    MappingConfiguration
        mappingConfiguration =
        underTest.newMappingConfiguration(rootFolderName, mapping, configFormat);
    assertNotNull(mappingConfiguration);
    assertEquals(type, mappingConfiguration.getType());
    assertEquals(location, mappingConfiguration.getLocation());
  }

  @Test
  public void newIndexConfigurationShouldSucceed() {

    File rootFolder = mock(File.class);
    File index1 = mock(File.class);
    when(fileHelper.listChildrenDirectories(rootFolder)).thenReturn(new File[]{index1});
    final String pathToElasticsearchConfigRootFolder = "/home/path/to/project/";
    final String rootFolderName = "elasticsearch";
    when(rootFolder.getName()).thenReturn(rootFolderName);
    final String index = "index1";
    final String pathToIndex1 = "/some/path/to/" + index;
    when(index1.getPath())
        .thenReturn(pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1);
    String configFormat = "json";
    File index1Settings = mock(File.class);
    File mapping11 = mock(File.class);
    File mapping12 = mock(File.class);
    File mapping13 = mock(File.class);
    when(fileHelper.listFilesByFilter(index1, configFormat))
        .thenReturn(Lists.<File>newArrayList(index1Settings, mapping11, mapping12, mapping13));
    when(index1Settings.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/_settings."
        + configFormat);
    when(mapping11.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping11."
        + configFormat);
    when(mapping12.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping12."
        + configFormat);
    when(mapping13.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping13."
        + configFormat);

    when(mapping11.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping11." + configFormat);
    when(mapping12.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping12." + configFormat);
    when(mapping13.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping13." + configFormat);

    IndexConfiguration
        indexConfiguration =
        underTest.newIndexConfiguration(rootFolderName, index1, configFormat);
    assertNotNull(indexConfiguration);
    assertEquals(index, indexConfiguration.getName());
    final
    String
        expectedConfigLocation =
        "/" + rootFolderName + pathToIndex1 + "/_settings." + configFormat;
    assertEquals(expectedConfigLocation, indexConfiguration.getConfigLocation());
    List<MappingConfiguration>
        mappingConfigurations =
        indexConfiguration.getMappingConfigurations();

    Collection<String>
        types =
        Collections2.transform(mappingConfigurations, new Function<MappingConfiguration, String>() {
          @Override
          public String apply(MappingConfiguration input) {
            return input.getType();
          }
        });
    assertTrue(types.contains("mapping11"));
    assertTrue(types.contains("mapping12"));
    assertTrue(types.contains("mapping13"));

    Collection<String>
        mappingLocations =
        Collections2.transform(mappingConfigurations, new Function<MappingConfiguration, String>() {
          @Override
          public String apply(MappingConfiguration input) {
            return input.getLocation();
          }
        });
    assertTrue(mappingLocations.contains(
        "/" + rootFolderName + pathToIndex1 + "/" + "mapping11" + "." + configFormat));
    assertTrue(mappingLocations.contains(
        "/" + rootFolderName + pathToIndex1 + "/" + "mapping12" + "." + configFormat));
    assertTrue(mappingLocations.contains(
        "/" + rootFolderName + pathToIndex1 + "/" + "mapping13" + "." + configFormat));
  }

  @Test
  public void dropIndexShouldIgnoreIfIndexDoesntExist() {
    Client client = mock(Client.class);
    IndexConfiguration indexConfiguration = mock(IndexConfiguration.class);
    AdminClient adminClient = mock(AdminClient.class);
    when(client.admin()).thenReturn(adminClient);
    IndicesAdminClient indicesAdminClient = mock(IndicesAdminClient.class);
    when(adminClient.indices()).thenReturn(indicesAdminClient);
    String indexName = "index";
    when(indexConfiguration.getName()).thenReturn(indexName);
    IndicesExistsRequestBuilder
        indicesExistsRequestBuilder =
        mock(IndicesExistsRequestBuilder.class);
    when(indicesAdminClient.prepareExists(indexName)).thenReturn(indicesExistsRequestBuilder);
    ListenableActionFuture<IndicesExistsResponse>
        actionFutureListener =
        mock(ListenableActionFuture.class);
    when(indicesExistsRequestBuilder.execute()).thenReturn(actionFutureListener);
    IndicesExistsResponse indiceExistsResponse = mock(IndicesExistsResponse.class);
    when(actionFutureListener.actionGet()).thenReturn(indiceExistsResponse);
    when(indiceExistsResponse.exists()).thenReturn(false);
    underTest.dropIndex(client, indexConfiguration);
    verify(client).admin();
    verifyNoMoreInteractions(client);
  }

  @Test
  public void dropIndexShouldSucceed() {
    Client client = mock(Client.class);
    IndexConfiguration indexConfiguration = mock(IndexConfiguration.class);
    AdminClient adminClient = mock(AdminClient.class);
    when(client.admin()).thenReturn(adminClient);
    IndicesAdminClient indicesAdminClient = mock(IndicesAdminClient.class);
    when(adminClient.indices()).thenReturn(indicesAdminClient);
    String indexName = "index";
    when(indexConfiguration.getName()).thenReturn(indexName);
    IndicesExistsRequestBuilder
        indicesExistsRequestBuilder =
        mock(IndicesExistsRequestBuilder.class);
    when(indicesAdminClient.prepareExists(indexName)).thenReturn(indicesExistsRequestBuilder);
    ListenableActionFuture<IndicesExistsResponse>
        actionFutureListener =
        mock(ListenableActionFuture.class);
    when(indicesExistsRequestBuilder.execute()).thenReturn(actionFutureListener);
    IndicesExistsResponse indiceExistsResponse = mock(IndicesExistsResponse.class);
    when(actionFutureListener.actionGet()).thenReturn(indiceExistsResponse);
    when(indiceExistsResponse.exists()).thenReturn(true);
    DeleteIndexRequestBuilder deleteIndexRequestBuilder = mock(DeleteIndexRequestBuilder.class);
    when(indicesAdminClient.prepareDelete(indexName)).thenReturn(deleteIndexRequestBuilder);
    ListenableActionFuture<DeleteIndexResponse>
        deleteIndexActionFutureListener =
        mock(ListenableActionFuture.class);
    when(deleteIndexRequestBuilder.execute()).thenReturn(deleteIndexActionFutureListener);
    DeleteIndexResponse deleteIndexResponse = mock(DeleteIndexResponse.class);
    when(deleteIndexActionFutureListener.actionGet()).thenReturn(deleteIndexResponse);
    when(deleteIndexResponse.acknowledged()).thenReturn(true);
    underTest.dropIndex(client, indexConfiguration);
    verify(deleteIndexResponse).acknowledged();
  }

  @Test(expected = RuntimeException.class)
  public void dropIndexShouldThrowRuntimeException() {
    Client client = mock(Client.class);
    IndexConfiguration indexConfiguration = mock(IndexConfiguration.class);
    AdminClient adminClient = mock(AdminClient.class);
    when(client.admin()).thenReturn(adminClient);
    IndicesAdminClient indicesAdminClient = mock(IndicesAdminClient.class);
    when(adminClient.indices()).thenReturn(indicesAdminClient);
    String indexName = "index";
    when(indexConfiguration.getName()).thenReturn(indexName);
    IndicesExistsRequestBuilder
        indicesExistsRequestBuilder =
        mock(IndicesExistsRequestBuilder.class);
    when(indicesAdminClient.prepareExists(indexName)).thenReturn(indicesExistsRequestBuilder);
    ListenableActionFuture<IndicesExistsResponse>
        actionFutureListener =
        mock(ListenableActionFuture.class);
    when(indicesExistsRequestBuilder.execute()).thenReturn(actionFutureListener);
    IndicesExistsResponse indiceExistsResponse = mock(IndicesExistsResponse.class);
    when(actionFutureListener.actionGet()).thenReturn(indiceExistsResponse);
    when(indiceExistsResponse.exists()).thenReturn(true);
    DeleteIndexRequestBuilder deleteIndexRequestBuilder = mock(DeleteIndexRequestBuilder.class);
    when(indicesAdminClient.prepareDelete(indexName)).thenReturn(deleteIndexRequestBuilder);
    ListenableActionFuture<DeleteIndexResponse>
        deleteIndexActionFutureListener =
        mock(ListenableActionFuture.class);
    when(deleteIndexRequestBuilder.execute()).thenReturn(deleteIndexActionFutureListener);
    DeleteIndexResponse deleteIndexResponse = mock(DeleteIndexResponse.class);
    when(deleteIndexActionFutureListener.actionGet()).thenReturn(deleteIndexResponse);
    when(deleteIndexResponse.acknowledged()).thenReturn(false);
    underTest.dropIndex(client, indexConfiguration);
  }

  @Test
  public void scanIndexConfigurationsUnitTestShouldSucceed() throws IOException {

    File rootFolder = mock(File.class);
    File index1 = mock(File.class);
    File index2 = mock(File.class);
    when(fileHelper.listChildrenDirectories(rootFolder)).thenReturn(new File[]{index1, index2});
    final String pathToElasticsearchConfigRootFolder = "/home/path/to/project/";
    final String rootFolderName = "elasticsearch";
    when(rootFolder.getName()).thenReturn(rootFolderName);
    final String pathToIndex1 = "/some/path/to/index1";
    when(index1.getPath())
        .thenReturn(pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1);
    final String pathToIndex2 = "/some/path/to/index2";
    when(index2.getPath())
        .thenReturn(pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex2);
    String configFormat = "json";
    File index1Settings = mock(File.class);
    File mapping11 = mock(File.class);
    File mapping12 = mock(File.class);
    File mapping13 = mock(File.class);
    File index2Settings = mock(File.class);
    File mapping21 = mock(File.class);
    when(fileHelper.listFilesByFilter(index1, configFormat))
        .thenReturn(Lists.<File>newArrayList(index1Settings, mapping11, mapping12, mapping13));
    when(fileHelper.listFilesByFilter(index2, configFormat))
        .thenReturn(Lists.<File>newArrayList(index2Settings, mapping21));
    when(index1Settings.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/_settings."
        + configFormat);
    when(index2Settings.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex2 + "/_settings."
        + configFormat);
    when(mapping11.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping11."
        + configFormat);
    when(mapping12.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping12."
        + configFormat);
    when(mapping13.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping13."
        + configFormat);
    when(mapping21.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex2 + "/mapping21."
        + configFormat);

    when(mapping11.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping11." + configFormat);
    when(mapping12.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping12." + configFormat);
    when(mapping13.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping13." + configFormat);
    when(mapping21.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex2 + "/mapping21." + configFormat);
    List<IndexConfiguration>
        indexConfigurations =
        underTest.scanIndexConfigurations(rootFolder, configFormat);

    assertNotNull(indexConfigurations);
    assertEquals(2, indexConfigurations.size());
    Collection<String>
        indexNames =
        Collections2.transform(indexConfigurations, new Function<IndexConfiguration, String>() {
          @Override
          public String apply(IndexConfiguration input) {
            return input.getName();
          }
        });
    assertEquals(2, indexNames.size());
    assertTrue(indexNames.contains("index1"));
    assertTrue(indexNames.contains("index2"));

    Collection<String>
        indexConfigLocations =
        Collections2.transform(indexConfigurations, new Function<IndexConfiguration, String>() {
          @Override
          public String apply(IndexConfiguration input) {
            return input.getConfigLocation();
          }
        });

    assertEquals(2, indexConfigLocations.size());

    for (String indexConfigLocation : indexConfigLocations) {
      assertTrue(
          Pattern.matches(".*index(\\d)/_settings\\." + configFormat + "$", indexConfigLocation));
    }

    Collection<IndexConfiguration>
        index1Configuration =
        Collections2.filter(indexConfigurations, new Predicate<IndexConfiguration>() {
          @Override
          public boolean apply(IndexConfiguration input) {
            return "index1".equals(input.getName());

          }

        });

    List<MappingConfiguration>
        index1MappingConfigurations =
        index1Configuration.iterator().next().getMappingConfigurations();
    Collection<String>
        index1Mappings =
        Collections2
            .transform(index1MappingConfigurations, new Function<MappingConfiguration, String>() {
              @Override
              public String apply(MappingConfiguration input) {
                return input.getType();
              }
            });

    assertEquals(3, index1Mappings.size());
    assertTrue(index1Mappings.contains("mapping11"));
    assertTrue(index1Mappings.contains("mapping12"));
    assertTrue(index1Mappings.contains("mapping13"));

    Collection<IndexConfiguration>
        index2Configuration =
        Collections2.filter(indexConfigurations, new Predicate<IndexConfiguration>() {
          @Override
          public boolean apply(IndexConfiguration input) {
            return "index2".equals(input.getName());

          }

        });

    List<MappingConfiguration>
        index2MappingConfigurations =
        index2Configuration.iterator().next().getMappingConfigurations();
    Collection<String>
        index2Mappings =
        Collections2
            .transform(index2MappingConfigurations, new Function<MappingConfiguration, String>() {
              @Override
              public String apply(MappingConfiguration input) {
                return input.getType();
              }
            });
    assertEquals(1, index2Mappings.size());
    assertTrue(index2Mappings.contains("mapping21"));

  }

  @Test
  public void mappingConfigurationsShouldSucceed() {

    File index1 = mock(File.class);
    final String pathToElasticsearchConfigRootFolder = "/home/path/to/project/";
    final String rootFolderName = "elasticsearch";
    final String index = "index1";
    final String pathToIndex1 = "/some/path/to/" + index;
    when(index1.getPath())
        .thenReturn(pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1);
    String configFormat = "json";
    File index1Settings = mock(File.class);
    File mapping11 = mock(File.class);
    File mapping12 = mock(File.class);
    File mapping13 = mock(File.class);
    when(fileHelper.listFilesByFilter(index1, configFormat))
        .thenReturn(Lists.<File>newArrayList(index1Settings, mapping11, mapping12, mapping13));
    when(index1Settings.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/_settings."
        + configFormat);
    when(mapping11.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping11."
        + configFormat);
    when(mapping12.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping12."
        + configFormat);
    when(mapping13.getAbsolutePath()).thenReturn(
        pathToElasticsearchConfigRootFolder + rootFolderName + pathToIndex1 + "/mapping13."
        + configFormat);

    when(mapping11.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping11." + configFormat);
    when(mapping12.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping12." + configFormat);
    when(mapping13.getPath())
        .thenReturn("/" + rootFolderName + pathToIndex1 + "/mapping13." + configFormat);

    List<MappingConfiguration>
        mappingConfigurations =
        underTest.mappingConfigurations(rootFolderName, index1, configFormat);
    assertNotNull(mappingConfigurations);
    assertEquals(3, mappingConfigurations.size());
    Collection<String>
        types =
        Collections2.transform(mappingConfigurations, new Function<MappingConfiguration, String>() {
          @Override
          public String apply(MappingConfiguration input) {
            return input.getType();
          }
        });
    assertTrue(types.contains("mapping11"));
    assertTrue(types.contains("mapping12"));
    assertTrue(types.contains("mapping13"));

    Collection<String>
        mappingLocations =
        Collections2.transform(mappingConfigurations, new Function<MappingConfiguration, String>() {
          @Override
          public String apply(MappingConfiguration input) {
            return input.getLocation();
          }
        });
    assertTrue(mappingLocations.contains(
        "/" + rootFolderName + pathToIndex1 + "/" + "mapping11" + "." + configFormat));
    assertTrue(mappingLocations.contains(
        "/" + rootFolderName + pathToIndex1 + "/" + "mapping12" + "." + configFormat));
    assertTrue(mappingLocations.contains(
        "/" + rootFolderName + pathToIndex1 + "/" + "mapping13" + "." + configFormat));
  }


}
