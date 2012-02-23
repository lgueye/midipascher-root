/*
 *
 */
package fr.midipascher.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.midipascher.domain.Account;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.Authority;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class TestUtils {

	public static final String	STANDARD_CHARSET	= "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBNéèçàù7894561230";
	private static final String	EMAIL_CHARSET		= "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN";
	private static final String	baseEndPoint		= ResourceBundle.getBundle("stories-context").getString(
															"baseEndPoint");

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
	public static Account validUser() {
		final Account account = new Account();
		account.setAuthorities(new HashSet<Authority>(Arrays.asList(TestUtils.validAuthority())));
		account.setCreated(new DateTime());
		account.setEmail(RandomStringUtils.random(20, TestUtils.EMAIL_CHARSET) + "@"
				+ RandomStringUtils.random(20, TestUtils.EMAIL_CHARSET) + ".com");
		account.setFirstName(RandomStringUtils.random(Account.CONSTRAINT_FIRST_NAME_MAX_SIZE,
				TestUtils.STANDARD_CHARSET));
		account.setLastConnection(new DateTime());
		account.setLastName(RandomStringUtils.random(Account.CONSTRAINT_LAST_NAME_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		account.setLocked(false);
		account.setPassword(RandomStringUtils.random(Account.CONSTRAINT_PASSWORD_MAX_SIZE, TestUtils.STANDARD_CHARSET));
		return account;
	}

	public static String createAccount() {

		createAuthority();

		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		String uri = "/accounts";
		final WebResource webResource = jerseyClient.resource(baseEndPoint + uri);
		String format = "application/json";
		String language = "fr";
		String requestContentType = "application/xml";
		Account account = TestUtils.validUser();
		ClientResponse response = webResource.accept(MediaType.valueOf(format))
				.acceptLanguage(new String[] { language }).header("Content-Type", requestContentType)
				.post(ClientResponse.class, account);
		return response.getLocation().toString();

	}

	public static void createAuthority() {
		final String path = "/admin/authorities";
		final URI uri = URI.create(baseEndPoint + path);
		final String requestContentType = "application/json";
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter("admin", "secret"));
		final WebResource webResource = jerseyClient.resource(uri);

		final String format = "application/json";
		final String language = "en";

		final Authority account = new Authority();
		account.setCode(Authority.RMGR);
		account.setActive(true);
		account.setLabel("Restaurant manager role");
		webResource.accept(MediaType.valueOf(format)).acceptLanguage(new String[] { language })
				.header("Content-Type", requestContentType).post(ClientResponse.class, account);

	}

}
