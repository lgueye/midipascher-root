/*
 *
 */
package fr.midipascher.persistence;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.network.NetworkUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class AbstractNodesTests {

    protected final ESLogger logger = Loggers.getLogger(getClass());

    private final Map<String, Node> nodes = newHashMap();

    private final Map<String, Client> clients = newHashMap();

    private Settings defaultSettings = ImmutableSettings
            .settingsBuilder()
            .put("cluster.name",
                    "test-cluster-"
                            + NetworkUtils.getLocalAddress()
                            .getHostName()).build();

    public Node buildNode(final String id) {
        return buildNode(id, EMPTY_SETTINGS);
    }

    public Node buildNode(final String id, final Settings settings) {
        final String settingsSource = getClass().getName().replace('.', '/') + ".yml";
        Settings finalSettings = settingsBuilder().loadFromClasspath(settingsSource).put(this.defaultSettings)
                .put(settings).put("name", id).build();

        if (finalSettings.get("gateway.type") == null) // default to non gateway
            finalSettings = settingsBuilder().put(finalSettings).put("gateway.type", "none").build();

        final Node node = nodeBuilder().settings(finalSettings).build();
        this.nodes.put(id, node);
        this.clients.put(id, node.client());
        return node;
    }

    public Node buildNode(final String id, final Settings.Builder settings) {
        return buildNode(id, settings.build());
    }

    public Client client(final String id) {
        return this.clients.get(id);
    }

    public void closeAllNodes() {
        for (final Client client : this.clients.values())
            client.close();
        this.clients.clear();
        for (final Node node : this.nodes.values())
            node.close();
        this.nodes.clear();
    }

    public void closeNode(final String id) {
        final Client client = this.clients.remove(id);
        if (client != null) client.close();
        final Node node = this.nodes.remove(id);
        if (node != null) node.close();
    }

    public Node node(final String id) {
        return this.nodes.get(id);
    }

    public void putDefaultSettings(final Settings settings) {
        this.defaultSettings = ImmutableSettings.settingsBuilder().put(this.defaultSettings).put(settings).build();
    }

    public void putDefaultSettings(final Settings.Builder settings) {
        putDefaultSettings(settings.build());
    }

    public Node startNode(final String id) {
        return buildNode(id).start();
    }

    public Node startNode(final String id, final Settings settings) {
        return buildNode(id, settings).start();
    }

    public Node startNode(final String id, final Settings.Builder settings) {
        return startNode(id, settings.build());
    }

}
