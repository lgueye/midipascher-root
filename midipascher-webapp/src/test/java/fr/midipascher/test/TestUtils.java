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
import org.junit.Assert;

import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class TestUtils {

	private static final String	charSet	= "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBNéèçàù7894561230";

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

	/**
	 * @return
	 */
	public static Address validAddress() {
		final Address address = new Address();
		address.setCity(RandomStringUtils.random(Address.CONSTRAINT_CITY_MAX_SIZE, TestUtils.charSet));
		address.setCountryCode("fr");
		address.setPostalCode(RandomStringUtils.random(Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE, TestUtils.charSet));
		address.setStreetAddress(RandomStringUtils
				.random(Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE, TestUtils.charSet));
		return address;
	}

	/**
	 * @return
	 */
	public static FoodSpecialty validFoodSpecialty() {
		final FoodSpecialty specialty = new FoodSpecialty();
		specialty.setCode(RandomStringUtils.random(FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE, TestUtils.charSet));
		specialty.setLabel(RandomStringUtils.random(FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE, TestUtils.charSet));
		return specialty;
	}

	public static Restaurant validRestaurant() {
		final Restaurant restaurant = new Restaurant();
		restaurant.setAddress(TestUtils.validAddress());
		restaurant.setDescription(RandomStringUtils.random(Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE,
				TestUtils.charSet));
		restaurant.setEmail("foo@bar.com");
		restaurant.setHalal(true);
		restaurant.setKosher(false);
		restaurant.setMainOffer(RandomStringUtils.random(Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE, TestUtils.charSet));
		restaurant.setName(RandomStringUtils.random(Restaurant.CONSTRAINT_NAME_MAX_SIZE, TestUtils.charSet));
		restaurant.setPhoneNumber(RandomStringUtils.random(Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE,
				TestUtils.charSet));
		restaurant.setSpecialties(new HashSet<FoodSpecialty>(Arrays.asList(TestUtils.validFoodSpecialty())));
		return restaurant;
	}

	public static <T> T fromJson(final String json, Class<T> clazz) throws JsonParseException, JsonMappingException,
			IOException {

		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(json.getBytes(), clazz);

	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXml(final String xml, Class<T> clazz) throws JAXBException, UnsupportedEncodingException {

		JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { clazz });

		Unmarshaller xmlUnmarshaller = jaxbContext.createUnmarshaller();

		return (T) xmlUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes("UTF-8")));

	}

	public static <T> String toJson(final T object) throws JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		mapper.writeValue(out, object);

		return out.toString();

	}

	public static <T> String toXml(final T object) throws JAXBException, UnsupportedEncodingException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { object.getClass() });

		Marshaller xmlMarshaller = jaxbContext.createMarshaller();

		xmlMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		xmlMarshaller.marshal(object, out);

		return out.toString("UTF-8");

	}

}
