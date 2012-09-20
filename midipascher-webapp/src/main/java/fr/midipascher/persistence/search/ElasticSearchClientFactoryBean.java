package fr.midipascher.persistence.search;

import com.google.common.base.Function;
import com.google.common.base.Strings;
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

import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: lgueye Date: 19/09/12 Time: 15:41
 */
public class ElasticSearchClientFactoryBean extends AbstractFactoryBean<Client> {

  private ElasticSearchClientTypology typology;

  private List<String> nodes;

  private String clusterName;

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
   * If not specified, defaults to 'elasticsearch'
   */
  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

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
    super
        .afterPropertiesSet();

  }

  /**
   * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
   */
  @Override
  protected Client createInstance() throws Exception {

    if (typology == null) {
      throw new BeanCreationException("Error creating " + Client.class.getName()
                                      + ": 'typology' property is required. Between "
                                      + ElasticSearchClientTypology.values());
    }

    ImmutableSettings.Builder builder;

    switch (typology) {

      case local:
        NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder();
        if (!Strings.isNullOrEmpty(clusterName)) {
          builder = nodeBuilder.getSettings();
          builder.put("cluster.name", clusterName);
        }
        node = nodeBuilder.node();
        client = node.client();
        return client;

      case remote:

        if (CollectionUtils.isEmpty(nodes)) {
          throw new BeanCreationException("Error creating " + Client.class.getName()
                                          + ": 'nodes' property is required if 'remote' typology is set");
        }
        Collection<InetSocketTransportAddress> addresses = fromNodes(nodes);
        if (!Strings.isNullOrEmpty(clusterName)) {
          builder = ImmutableSettings.settingsBuilder();
          builder.put("cluster.name", clusterName);
          builder.put("client.transport.sniff", true);
          client = new TransportClient(builder.build());
        } else {
          client = new TransportClient();
        }
        for (InetSocketTransportAddress address : addresses) {
          ((TransportClient) client).addTransportAddress(address);
        }
        return client;

      default:
        throw new IllegalStateException();
    }

  }

  private Collection<InetSocketTransportAddress> fromNodes(List<String> nodes) {
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
