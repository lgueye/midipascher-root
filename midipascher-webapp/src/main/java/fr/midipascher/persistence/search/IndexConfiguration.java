package fr.midipascher.persistence.search;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 21/09/12
 * Time: 01:05
 * To change this template use File | Settings | File Templates.
 */
public class IndexConfiguration {

    private String name;
    private String configLocation;
    private List<MappingConfiguration> mappingsConfiguration;

    public String getName() {
        return name;
    }


    public String getConfigLocation() {
        return configLocation;
    }

    public List<MappingConfiguration> getMappingsConfiguration() {
        return mappingsConfiguration;
    }
}
