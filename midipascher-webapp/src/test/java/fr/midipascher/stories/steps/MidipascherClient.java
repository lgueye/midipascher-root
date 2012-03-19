/**
 * 
 */
package fr.midipascher.stories.steps;

import java.net.URI;
import java.util.ResourceBundle;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
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

}
