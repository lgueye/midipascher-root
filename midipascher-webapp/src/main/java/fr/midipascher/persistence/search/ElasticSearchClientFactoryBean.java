package fr.midipascher.persistence.search;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Not scanned, included in *search.xml and *search-test.xml file<br/>
 * The first one uses 'remote' typology and the second one uses 'local' one<br/>
 *
 * User: lgueye Date: 19/09/12 Time: 15:41
 */
public class ElasticSearchClientFactoryBean extends AbstractFactoryBean<Client> {

    private ElasticSearchClientTypology typology;

    private List<String> nodes;

    private Resource nodeConfigLocation;

    private IndicesUpdateStrategy indicesUpdateStrategy;

    private Client client;

    private Node node;

    /**
     * Supported values: 'local', 'remote'
     *
     * @see ElasticSearchClientTypology
     */
    public void setTypology(ElasticSearchClientTypology typology) {
        this.typology = typology;
    }

    /**
     * Supported node pattern: 'host:port' Multiple node example:
     * <pre>
     *   {@code
     *    <property name="nodes">
     *      <list>
     *        <value>jack:9300</value>
     *        <value>sparrow:9300</value>
     *        <value>bill:9300</value>
     *        <value>bottier:9300</value>
     *      </list>
     *    </property>
     *   }
     * </pre>
     */
    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    /**
     * <pre>
     *     {@code
     *     <property name="nodeConfigLocation" value="some/resource/path/myTemplate.txt"/>
     *     }
     * </pre>
     * Defaults to "classpath:elasticsearch/_settings.json"<br/>
     * Accepts either json or yml format<br/>
     * A suitable place to specify "cluster.name" property which defaults to "elasticsearch"
     * @see <a href="http://www.elasticsearch.org/guide/reference/modules/discovery/">Elastisearch discovery module</a>
     *
     * @param nodeConfigLocation
     */
    public void setNodeConfigLocation(Resource nodeConfigLocation) {
        this.nodeConfigLocation = nodeConfigLocation;
    }

    /**
     * Defaults to dropcreate
     * @see IndicesUpdateStrategy
     * @param indicesUpdateStrategy
     */
    public void setIndicesUpdateStrategy(IndicesUpdateStrategy indicesUpdateStrategy) {
        this.indicesUpdateStrategy = indicesUpdateStrategy;
    }

    /**
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType()
     */
    @Override
    public Class<Client> getObjectType() {
        return Client.class;
    }

    /**
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        if (indicesUpdateStrategy == null) {
            indicesUpdateStrategy = IndicesUpdateStrategy.dropcreate;
        }

        switch (indicesUpdateStrategy) {
            case dropcreate:
                new DropCreateIndicesCommand().execute(client);
                break;
            case merge:
                new MergeIndicesCommand().execute(client);
                break;
        }

    }

    /**
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
     */
    @Override
    protected Client createInstance() throws Exception {

        if (typology == null)
            throw new BeanCreationException("Error creating " + Client.class.getName()
                    + ": 'typology' property is required. Between "
                    + ElasticSearchClientTypology.values());

        if (nodeConfigLocation == null)
            nodeConfigLocation = new ClassPathResource("elasticsearch/_settings.json");


        switch (typology) {

            case local:
                NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder();
                nodeBuilder.settings().loadFromUrl(nodeConfigLocation.getURL());
                node = nodeBuilder.node();
                client = node.client();
                return client;

            case remote:

                if (CollectionUtils.isEmpty(nodes)) {
                    throw new BeanCreationException("Error creating " + Client.class.getName()
                            + ": 'nodes' property is required if 'remote' typology is set");
                }
                Collection<InetSocketTransportAddress> addresses = fromNodes(nodes);
                ImmutableSettings.Builder builder = ImmutableSettings.settingsBuilder();
                builder.loadFromUrl(nodeConfigLocation.getURL());
                builder.put("client.transport.sniff", true);
                client = new TransportClient(builder.build());
                for (InetSocketTransportAddress address : addresses) {
                    ((TransportClient) client).addTransportAddress(address);
                }
                return client;

            default:
                throw new UnsupportedOperationException("Unknown client typology");
        }

    }

    /**
     * Assumes that nodes use this pattern: <host>:<port>
     * @param nodes
     * @return
     */
    protected Collection<InetSocketTransportAddress> fromNodes(List<String> nodes) {
        return Collections2.transform(nodes, new Function<String, InetSocketTransportAddress>() {
            @Override
            public InetSocketTransportAddress apply(String node) {
                StringTokenizer tokenizer = new StringTokenizer(node, ":", false);
                String host = tokenizer.nextToken();
                String portAsString = tokenizer.nextToken();
                return new InetSocketTransportAddress(host, Integer.valueOf(portAsString));
            }
        });
    }

    /**
     * Destroy the singleton instance, if any.
     *
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        super.destroy();
        if (client != null) {
            client.close();
        }
        if (node != null) {
            node.close();
        }
    }
}
