/**
 *
 */
package fr.midipascher.persistence.search;

import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.JsonByteArrayToRestaurantConverter;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonByteArrayToRestaurantConverterTest {

    @Mock
    ObjectMapper jsonMapper;

    @InjectMocks
    JsonByteArrayToRestaurantConverter underTest = new JsonByteArrayToRestaurantConverter();

    /**
     * Test method for
     * {@link org.diveintojee.poc.jbehave.persistence.JsonByteArrayToRestaurantConverter#convert(byte[])}
     * .
     */
    @Test
    public final void convertShoulReturnNullWithNullInput() {

        // Variables
        byte[] source;
        Restaurant result;

        // Given
        source = null;

        // When
        result = this.underTest.convert(source);

        // Then
        assertNull(result);

        // Given
        source = new byte[]{};

        // When
        result = this.underTest.convert(source);

        // Then
        assertNull(result);

    }

    /**
     * Test method for
     * {@link org.diveintojee.poc.jbehave.persistence.JsonByteArrayToRestaurantConverter#convert(byte[])}
     * .
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Test
    public final void convertShouldSucceed() throws JsonParseException, JsonMappingException, IOException {

        // Variables
        byte[] source;
        Restaurant result;
        DeserializationConfig deserializationConfig;
        Restaurant fromJson;

        // Given
        source = "{}".getBytes("utf-8");
        deserializationConfig = Mockito.mock(DeserializationConfig.class);
        fromJson = Mockito.mock(Restaurant.class);

        // When
        Mockito.when(this.jsonMapper.getDeserializationConfig()).thenReturn(deserializationConfig);
        Mockito.when(deserializationConfig.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES))
                .thenReturn(deserializationConfig);
        Mockito.when(this.jsonMapper.readValue(source, Restaurant.class)).thenReturn(fromJson);
        result = this.underTest.convert(source);

        // Then
        Mockito.verify(this.jsonMapper).getDeserializationConfig();
        Mockito.verify(deserializationConfig).without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        Mockito.verify(this.jsonMapper).readValue(source, Restaurant.class);

        Mockito.verifyNoMoreInteractions(this.jsonMapper, deserializationConfig);

        assertSame(fromJson, result);

    }

}
