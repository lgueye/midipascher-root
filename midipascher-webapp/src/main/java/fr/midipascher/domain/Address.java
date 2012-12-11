/**
 *
 */
package fr.midipascher.domain;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.midipascher.domain.validation.Create;
import fr.midipascher.domain.validation.Update;

/**
 * @author louis.gueye@gmail.com
 */
@Embeddable
public class Address extends AbstractObject implements Serializable {

    public static final String COLUMN_NAME_STREET_ADDRESS = "address_street_address";
    public static final String COLUMN_NAME_CITY = "address_city";
    public static final String COLUMN_NAME_POSTAL_CODE = "address_postal_code";
    public static final String COLUMN_NAME_COUNTRY_CODE = "address_country_code";

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

    private Coordinates coordinates;

    private static final String FORMATTED_ADDRESS_PATTERN = "{0}, {1} {2}, {3}";
    private String formattedAddress;

    public void formattedAddress() {
        final String format = MessageFormat.format(FORMATTED_ADDRESS_PATTERN,
                                                 getStreetAddress(),
                                                 getPostalCode(),
                                                 getCity(),
                                                 new Locale("", getCountryCode())
                                                     .getDisplayCountry());
        this.formattedAddress = WordUtils.capitalize(format.toLowerCase());
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

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Coordinates getCoordinates() {
      return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
      this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Address)) {
        return false;
      }

      Address address = (Address) o;

      return city.equals(address.city) && countryCode.equals(address.countryCode) && postalCode
          .equals(address.postalCode) && streetAddress.equals(address.streetAddress);

    }

    @Override
    public int hashCode() {
      int result = streetAddress.hashCode();
      result = 31 * result + city.hashCode();
      result = 31 * result + postalCode.hashCode();
      result = 31 * result + countryCode.hashCode();
      return result;
    }

    public void addCoordinates(BigDecimal lat, BigDecimal lng) {
        setCoordinates(new Coordinates());
        getCoordinates().setLat(lat);
        getCoordinates().setLng(lng);
    }
}
