/**
 *
 */
package fr.midipascher.steps;

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

    private static final String baseEndPoint = ResourceBundle.getBundle("stories-context").getString("baseEndPoint");

    private static Client jerseyClient;

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

    /**
     * @param relativePath
     * @param responseFormat
     * @param responseLanguage
     * @return
     */
    public static ClientResponse readURI(String relativePath, String responseFormat, String responseLanguage) {
        final URI uri = URI.create(baseEndPoint + relativePath);
        return jerseyClient.resource(uri).accept(responseFormat).acceptLanguage(responseLanguage)
                .get(ClientResponse.class);

    }

    /**
     * @param location
     * @param responseFormat
     * @param responseLanguage
     * @return
     */
    public static ClientResponse readLocation(URI location, String responseFormat, String responseLanguage) {
        return jerseyClient.resource(location).accept(responseFormat).acceptLanguage(responseLanguage)
                .get(ClientResponse.class);

    }

    /**
     * @param body
     * @param relativePath
     * @param requestFormat
     * @param responseFormat
     * @param responseLanguage
     * @return
     */
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
     * @param relativePath
     * @param queryString
     * @param requestFormat
     * @param responseFormat
     * @param responseLanguage
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
        for (final String resource : resources) {
            final WebResource webResource = jerseyClient.resource(resource);
            webResource.delete();
        }
    }

    /**
     * @param body
     * @param relativePath
     * @param requestFormat
     * @param responseFormat
     * @param responseLanguage
     * @param <T>
     * @return
     */
    public static <T> ClientResponse updateEntity(T body, String relativePath, String requestFormat,
                                                  String responseFormat, String responseLanguage) {
        final URI uri = URI.create(baseEndPoint + relativePath);
        return jerseyClient.resource(uri).type(requestFormat).accept(responseFormat).acceptLanguage(responseLanguage)
                .put(ClientResponse.class, body);
    }

    /**
     * @param relativePath
     * @param requestContentType
     * @param responseContentType
     * @param responseLanguage
     * @return
     */
    public static ClientResponse inactivateEntity(String relativePath, String requestContentType,
                                                  String responseContentType, String responseLanguage) {
        final URI uri = URI.create(baseEndPoint + relativePath);
        return jerseyClient.resource(uri).accept(responseContentType)
                .acceptLanguage(responseLanguage).post(ClientResponse.class);
    }

    /**
     * @param relativePath
     * @param requestContentType
     * @param responseContentType
     * @param responseLanguage
     * @return
     */
    public static ClientResponse lockEntity(String relativePath, String requestContentType, String responseContentType,
                                            String responseLanguage) {
        final URI uri = URI.create(baseEndPoint + relativePath);
        return jerseyClient.resource(uri).type(requestContentType).accept(responseContentType)
                .acceptLanguage(responseLanguage).post(ClientResponse.class);
    }

    /**
     * @param relativePath
     * @param responseFormat
     * @param responseLanguage
     * @return
     */
    public static ClientResponse deleteEntity(String relativePath, String responseFormat, String responseLanguage) {
        final URI uri = URI.create(baseEndPoint + relativePath);
        return jerseyClient.resource(uri).accept(responseFormat).acceptLanguage(responseLanguage)
                .delete(ClientResponse.class);
    }

}
