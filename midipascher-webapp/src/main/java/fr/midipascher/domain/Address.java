/**
 *
 */
package fr.midipascher.domain;

import fr.midipascher.domain.validation.Create;
import fr.midipascher.domain.validation.Update;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author louis.gueye@gmail.com
 */
@Embeddable
public class Address extends AbstractObject implements Serializable {

    public static final String COLUMN_NAME_STREET_ADDRESS = "address_street_address";
    public static final String COLUMN_NAME_CITY = "address_city";
    public static final String COLUMN_NAME_POSTAL_CODE = "address_postal_code";
    public static final String COLUMN_NAME_COUNTRY_CODE = "address_country_code";
    public static final String COLUMN_NAME_LATITUDE = "address_latitude";
    public static final String COLUMN_NAME_LONGITUDE = "address_longitude";

    public static final int CONSTRAINT_STREET_ADDRESS_MAX_SIZE = 100;
    public static final int CONSTRAINT_CITY_MAX_SIZE = 50;
    public static final int CONSTRAINT_POSTAL_CODE_MAX_SIZE = 10;
    public static final int CONSTRAINT_COUNTRY_CODE_MAX_SIZE = 2;

    /**
     *
     */
    private static final long serialVersionUID = -5732718607385874727L;

    @Column(name = Address.COLUMN_NAME_STREET_ADDRESS)
    @NotEmpty(message = "{address.streetAddress.required}", groups = {Create.class, Update.class})
    @Size(max = Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE, message = "{address.streetAddress.max.size}", groups = {
            Create.class, Update.class})
    private String streetAddress;

    @Column(name = Address.COLUMN_NAME_CITY)
    @NotEmpty(message = "{address.city.required}", groups = {Create.class, Update.class})
    @Size(max = Address.CONSTRAINT_CITY_MAX_SIZE, message = "{address.city.max.size}", groups = {Create.class,
            Update.class})
    private String city;

    @Column(name = Address.COLUMN_NAME_POSTAL_CODE)
    @NotEmpty(message = "{address.postalCode.required}", groups = {Create.class, Update.class})
    @Size(max = Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE, message = "{address.postalCode.max.size}", groups = {
            Create.class, Update.class})
    private String postalCode;

    @Column(name = Address.COLUMN_NAME_COUNTRY_CODE)
    @NotNull(message = "{address.countryCode.required}", groups = {Create.class, Update.class})
    @Size(min = Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE, max = Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE, message = "{address.countryCode.exact.size}", groups = {
            Create.class, Update.class})
    private String countryCode;

    @Column(name = Address.COLUMN_NAME_LATITUDE)
    private BigDecimal latitude;

    @Column(name = Address.COLUMN_NAME_LONGITUDE)
    private BigDecimal longitude;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Address other = (Address) obj;
        if (this.city == null) {
            if (other.city != null)
                return false;
        } else if (!this.city.equals(other.city))
            return false;
        if (this.countryCode == null) {
            if (other.countryCode != null)
                return false;
        } else if (!this.countryCode.equals(other.countryCode))
            return false;
        if (this.postalCode == null) {
            if (other.postalCode != null)
                return false;
        } else if (!this.postalCode.equals(other.postalCode))
            return false;
        if (this.streetAddress == null) {
            if (other.streetAddress != null)
                return false;
        } else if (!this.streetAddress.equals(other.streetAddress))
            return false;
        return true;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.city == null ? 0 : this.city.hashCode());
        result = prime * result + (this.countryCode == null ? 0 : this.countryCode.hashCode());
        result = prime * result + (this.postalCode == null ? 0 : this.postalCode.hashCode());
        result = prime * result + (this.streetAddress == null ? 0 : this.streetAddress.hashCode());
        return result;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public BigDecimal getLatitude() {
      return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
      this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
      return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
      this.longitude = longitude;
    }

    public String formatAddress() {
      throw new UnsupportedOperationException("Not yet implemented");
    }
}
