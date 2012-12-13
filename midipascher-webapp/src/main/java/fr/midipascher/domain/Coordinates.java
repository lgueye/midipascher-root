package fr.midipascher.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author louis.gueye@gmail.com
 */
@Embeddable
public class Coordinates extends AbstractObject implements Serializable {

    public static final String COLUMN_NAME_LATITUDE = "address_latitude";
    public static final String COLUMN_NAME_LONGITUDE = "address_longitude";

    @Column(name = Coordinates.COLUMN_NAME_LATITUDE)
    private Double lat;

    @Column(name = Coordinates.COLUMN_NAME_LONGITUDE)
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

  @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (!(o instanceof Coordinates)) {
          return false;
        }

        Coordinates that = (Coordinates) o;

        return lat.equals(that.lat) && lng.equals(that.lng);

    }

    @Override
    public int hashCode() {
      int result = lat.hashCode();
      result = 31 * result + lng.hashCode();
      return result;
    }

}
