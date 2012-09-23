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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void scanIndicesConfigurationShouldSucceed() throws IOException {
        fileHelper = new FileHelper();
        underTest = new DropCreateIndicesCommand(fileHelper);
        File rootFolder = new ClassPathResource("/testelasticsearchlayout").getFile();
        List<IndexConfiguration> indexConfigurations = underTest.scanIndexConfigurations(rootFolder, "json");
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

        Collection<IndexConfiguration> index1Configuration = Collections2.filter(indexConfigurations, new Predicate<IndexConfiguration>() {
            @Override
            public boolean apply(IndexConfiguration input) {
                return "index1".equals(input.getName());

            }

        });

        List<MappingConfiguration> index1MappingConfigurations = index1Configuration.iterator().next().getMappingConfigurations();
        Collection<String> index1Mappings = Collections2.transform(index1MappingConfigurations, new Function<MappingConfiguration, String>() {
            @Override
            public String apply(MappingConfiguration input) {
                return input.getType();
            }
        });

        assertEquals(3, index1Mappings.size());
        assertTrue(index1Mappings.contains("mapping11"));
        assertTrue(index1Mappings.contains("mapping12"));
        assertTrue(index1Mappings.contains("mapping13"));

        Collection<IndexConfiguration> index2Configuration = Collections2.filter(indexConfigurations, new Predicate<IndexConfiguration>() {
            @Override
            public boolean apply(IndexConfiguration input) {
                return "index2".equals(input.getName());

            }

        });

        List<MappingConfiguration> index2MappingConfigurations = index2Configuration.iterator().next().getMappingConfigurations();
        Collection<String> index2Mappings = Collections2.transform(index2MappingConfigurations, new Function<MappingConfiguration, String>() {
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
        String rootFolderName = "some";
        String format = "yml";
        String type = "restaurant";
        String location = "/" + rootFolderName + "/path/to/" + type + "." + format;
        String mappingPath = "/home/any/path/before/" + rootFolderName + "/path/to/" + type + "." + format;
        File mapping = mock(File.class);
        when(mapping.getPath()).thenReturn(mappingPath);

        MappingConfiguration mappingConfiguration = underTest.newMappingConfiguration(rootFolderName, mapping, format);
        assertNotNull(mappingConfiguration);
        assertEquals(type, mappingConfiguration.getType());
        assertEquals(location, mappingConfiguration.getLocation());
    }

    @Test
    public void newIndexConfigurationShouldSucceed() {
        String rootFolderName = "some";
        String format = "json";
        String index = "index1";
        String absoluteIndexPath = "/home/any/path/before/" + rootFolderName + "/path/to/" + index;
        String relativeIndexPath = "/" + rootFolderName + "/path/to/" + index;
        String configLocation = "/" + rootFolderName + "/path/to/" + index + "/_settings." + format;
        File indexDirectory = mock(File.class);
        when(indexDirectory.getPath()).thenReturn(relativeIndexPath);
        File type1MappingFile = new File(absoluteIndexPath + "/" + "type1" + "." + format);
        File type2MappingFile = new File(absoluteIndexPath + "/" + "type2" + "." + format);
        File type3MappingFile = new File(absoluteIndexPath + "/" + "type3" + "." + format);
        List<File> files = Lists.newArrayList(type1MappingFile, type2MappingFile, type3MappingFile);

        when(fileHelper.listFilesByFilter(indexDirectory, format)).thenReturn(files);

        IndexConfiguration indexConfiguration = underTest.newIndexConfiguration(rootFolderName, indexDirectory, format);
        assertNotNull(indexConfiguration);
        assertEquals(index, indexConfiguration.getName());
        assertEquals(configLocation, indexConfiguration.getConfigLocation());
        List<MappingConfiguration> mappingConfigurations = indexConfiguration.getMappingConfigurations();

        Collection<String> types = Collections2.transform(mappingConfigurations, new Function<MappingConfiguration, String>() {
            @Override
            public String apply(MappingConfiguration input) {
                return input.getType();
            }
        });
        assertTrue(types.contains("type1"));
        assertTrue(types.contains("type2"));
        assertTrue(types.contains("type3"));

        Collection<String> mappingLocations = Collections2.transform(mappingConfigurations, new Function<MappingConfiguration, String>() {
            @Override
            public String apply(MappingConfiguration input) {
                return input.getLocation();
            }
        });
        assertTrue(mappingLocations.contains(relativeIndexPath + "/" + "type1" + "." + format));
        assertTrue(mappingLocations.contains(relativeIndexPath + "/" + "type2" + "." + format));
        assertTrue(mappingLocations.contains(relativeIndexPath + "/" + "type3" + "." + format));
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
        IndicesExistsRequestBuilder indicesExistsRequestBuilder = mock(IndicesExistsRequestBuilder.class);
        when(indicesAdminClient.prepareExists(indexName)).thenReturn(indicesExistsRequestBuilder);
        ListenableActionFuture<IndicesExistsResponse> actionFutureListener = mock(ListenableActionFuture.class);
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
        IndicesExistsRequestBuilder indicesExistsRequestBuilder = mock(IndicesExistsRequestBuilder.class);
        when(indicesAdminClient.prepareExists(indexName)).thenReturn(indicesExistsRequestBuilder);
        ListenableActionFuture<IndicesExistsResponse> actionFutureListener = mock(ListenableActionFuture.class);
        when(indicesExistsRequestBuilder.execute()).thenReturn(actionFutureListener);
        IndicesExistsResponse indiceExistsResponse = mock(IndicesExistsResponse.class);
        when(actionFutureListener.actionGet()).thenReturn(indiceExistsResponse);
        when(indiceExistsResponse.exists()).thenReturn(true);
        DeleteIndexRequestBuilder deleteIndexRequestBuilder = mock(DeleteIndexRequestBuilder.class);
        when(indicesAdminClient.prepareDelete(indexName)).thenReturn(deleteIndexRequestBuilder);
        ListenableActionFuture<DeleteIndexResponse> deleteIndexActionFutureListener = mock(ListenableActionFuture.class);
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
        IndicesExistsRequestBuilder indicesExistsRequestBuilder = mock(IndicesExistsRequestBuilder.class);
        when(indicesAdminClient.prepareExists(indexName)).thenReturn(indicesExistsRequestBuilder);
        ListenableActionFuture<IndicesExistsResponse> actionFutureListener = mock(ListenableActionFuture.class);
        when(indicesExistsRequestBuilder.execute()).thenReturn(actionFutureListener);
        IndicesExistsResponse indiceExistsResponse = mock(IndicesExistsResponse.class);
        when(actionFutureListener.actionGet()).thenReturn(indiceExistsResponse);
        when(indiceExistsResponse.exists()).thenReturn(true);
        DeleteIndexRequestBuilder deleteIndexRequestBuilder = mock(DeleteIndexRequestBuilder.class);
        when(indicesAdminClient.prepareDelete(indexName)).thenReturn(deleteIndexRequestBuilder);
        ListenableActionFuture<DeleteIndexResponse> deleteIndexActionFutureListener = mock(ListenableActionFuture.class);
        when(deleteIndexRequestBuilder.execute()).thenReturn(deleteIndexActionFutureListener);
        DeleteIndexResponse deleteIndexResponse = mock(DeleteIndexResponse.class);
        when(deleteIndexActionFutureListener.actionGet()).thenReturn(deleteIndexResponse);
        when(deleteIndexResponse.acknowledged()).thenReturn(false);
        underTest.dropIndex(client, indexConfiguration);
    }

}
