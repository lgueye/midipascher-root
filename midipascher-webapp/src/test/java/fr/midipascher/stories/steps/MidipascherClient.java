/**
 * 
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
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

/**
 * @author louis.gueye@gmail.com
 */
public class MidipascherClient {

	private static final String	baseEndPoint	= ResourceBundle.getBundle("stories-context").getString("baseEndPoint");

	private static Client		jerseyClient;

	static {
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
	}

	public static void setCredentials(String uid, String password) {
		jerseyClient.removeAllFilters();
		jerseyClient.addFilter(new LoggingFilter());
		jerseyClient.addFilter(new HTTPBasicAuthFilter(uid, password));
	}

	public static ClientResponse readURI(String relativePath, String responseFormat, String responseLanguage) {
		final URI uri = URI.create(baseEndPoint + relativePath);
		return jerseyClient.resource(uri).accept(responseFormat).acceptLanguage(responseLanguage)
				.get(ClientResponse.class);

	}

	public static ClientResponse readLocation(URI location, String responseFormat, String responseLanguage) {
		return jerseyClient.resource(location).accept(responseFormat).acceptLanguage(responseLanguage)
				.get(ClientResponse.class);

	}

	public static <T> ClientResponse createEntity(T body, String relativePath, String requestFormat,
			String responseFormat, String responseLanguage) {
		final URI uri = URI.create(baseEndPoint + relativePath);
		return jerseyClient.resource(uri).type(requestFormat).accept(responseFormat).acceptLanguage(responseLanguage)
				.post(ClientResponse.class, body);
	}

	public static void expectedCode(int expected, int actual) {
		Assert.assertEquals(expected, actual);
	}

	public static void expectedMessage(String expected, String actual) {
		Assert.assertEquals(expected, actual);
	}

	/**
	 * @param path
	 * @param queryString
	 * @param requestContentType
	 * @param responseContentType
	 * @return
	 */
	public static ClientResponse findEntityByCriteria(String relativePath, String queryString, String requestFormat,
			String responseFormat, String responseLanguage) {
		final URI uri = URI.create(baseEndPoint + relativePath);
		return jerseyClient.resource(uri).type(requestFormat).accept(responseFormat).acceptLanguage(responseLanguage)
				.post(ClientResponse.class, queryString);
	}

	/**
	 * @param resources
	 */
	public static void deleteResources(List<String> resources) {
		if (CollectionUtils.isEmpty(resources)) return;
		for ( final String resource : resources ) {
			final WebResource webResource = jerseyClient.resource(resource);
			webResource.delete();
		}
	}
}
