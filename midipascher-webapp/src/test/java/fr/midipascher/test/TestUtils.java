/*
 *
 */
package fr.midipascher.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Assert;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import fr.midipascher.domain.User;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class TestUtils {

	private static final String	STANDARD_CHARSET	= "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBNéèçàù7894561230";
	private static final String	EMAIL_CHARSET		= "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN";

	/**
	 * @param e
	 * @param errorCode
	 * @param propertyPath
	 */
	public static void assertViolationContainsTemplateAndPath(final ConstraintViolationException e,
			final String errorCode, final String propertyPath) {
		Assert.assertNotNull(e.getConstraintViolations());
		Assert.assertEquals(1, CollectionUtils.size(e.getConstraintViolations()));
		final ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
		Assert.assertEquals(errorCode, violation.getMessageTemplate());
		Assert.assertEquals(propertyPath, violation.getPropertyPath().toString());
	}

	public static <T> T fromJson(final String json, final Class<T> clazz) throws JsonParseException,
			JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(json.getBytes(), clazz);

	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXml(final String xml, final Class<T> clazz) throws JAXBException,
			UnsupportedEncodingException {

		final JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { clazz });

		final Unmarshaller xmlUnmarshaller = jaxbContext.createUnmarshaller();

		return (T) xmlUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes("UTF-8")));

	}

	public static <T> String toJson(final T object) throws JsonGenerationException, JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper();

		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		mapper.writeValue(out, object);

		return out.toString();

	}

	public static <T> String toXml(final T object) throws JAXBException, UnsupportedEncodingException {

		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		final JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { object.getClass() });

		final Marshaller xmlMarshaller = jaxbContext.createMarshaller();

		xmlMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		xmlMarshaller.marshal(object, out);

		return out.toString("UTF-8");

	}

	/**
	 * @return
	 */
	public static Address validAddress() {
		final Address address = new Address();
		address.setCity(RandomStringUtils.random(Address.CONSTRAINT_CITY_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		address.setCountryCode("fr");
		address.setPostalCode(RandomStringUtils.random(Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		address.setStreetAddress(RandomStringUtils.random(Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		return address;
	}

	/**
	 * @return
	 */
	public static Authority validAuthority() {
		final Authority authority = new Authority();
		authority.setCode(RandomStringUtils.random(Authority.CONSTRAINT_CODE_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		authority.setLabel(RandomStringUtils.random(Authority.CONSTRAINT_LABEL_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		return authority;
	}

	/**
	 * @return
	 */
	public static FoodSpecialty validFoodSpecialty() {
		final FoodSpecialty specialty = new FoodSpecialty();
		specialty.setCode(RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		specialty.setLabel(RandomStringUtils
				.random(FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		return specialty;
	}

	public static Restaurant validRestaurant() {
		final Restaurant restaurant = new Restaurant();
		restaurant.setAddress(TestUtils.validAddress());
		restaurant.setDescription(RandomStringUtils.random(Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		restaurant.setCompanyId(RandomStringUtils.random(Restaurant.CONSTRAINT_COMPANY_ID_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		restaurant.setHalal(true);
		restaurant.setKosher(false);
		restaurant.setVegetarian(false);
		restaurant.setMainOffer(RandomStringUtils.random(Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		restaurant.setName(RandomStringUtils.random(Restaurant.CONSTRAINT_NAME_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		restaurant.setPhoneNumber(RandomStringUtils.random(Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		restaurant.setSpecialties(new HashSet<FoodSpecialty>(Arrays.asList(TestUtils.validFoodSpecialty())));
		return restaurant;
	}

	/**
	 * @return
	 */
	public static User validUser() {
		final User user = new User();
		user.setAuthorities(new HashSet<Authority>(Arrays.asList(TestUtils.validAuthority())));
		user.setCreated(new DateTime());
		user.setEmail(RandomStringUtils.random(20, TestUtils.EMAIL_CHARSET) + "@"
				+ RandomStringUtils.random(20, TestUtils.EMAIL_CHARSET) + ".com");
		user.setFirstName(RandomStringUtils.random(User.CONSTRAINT_FIRST_NAME_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		user.setLastConnection(new DateTime());
		user.setLastName(RandomStringUtils.random(User.CONSTRAINT_LAST_NAME_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		user.setLocked(false);
		user.setPassword(RandomStringUtils.random(User.CONSTRAINT_PASSWORD_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		return user;
	}

}
