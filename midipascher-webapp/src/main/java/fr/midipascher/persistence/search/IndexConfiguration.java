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
    private List<MappingConfiguration> mappingConfigurations;

    public String getName() {
        return name;
    }


    public String getConfigLocation() {
        return configLocation;
    }

    public List<MappingConfiguration> getMappingConfigurations() {
        return mappingConfigurations;
    }


  public void setName(String name) {
    this.name = name;
  }

  public void setConfigLocation(String configLocation) {
    this.configLocation = configLocation;
  }


  public void setMappingConfigurations(List<MappingConfiguration> mappingConfigurations) {
    this.mappingConfigurations = mappingConfigurations;
  }
}
