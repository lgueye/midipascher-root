/**
 *
 */
package fr.midipascher.persistence.search;

import fr.midipascher.domain.Restaurant;
import fr.midipascher.persistence.search.RestaurantToJsonByteArrayConverter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class RestaurantToJsonByteArrayConverterTest {

    @Mock
    ObjectMapper jsonMapper;

    @InjectMocks
    RestaurantToJsonByteArrayConverter underTest = new RestaurantToJsonByteArrayConverter();

    /**
     */
    @Test
    public final void convertShoulReturnNullWithNullInput() {

        // Variables
        Restaurant source;

        // Given
        source = null;

        // When
        byte[] jsonByteArray = this.underTest.convert(source);

        // Then
        assertNull(jsonByteArray);
    }

    /**
     * Test method for
     * .
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @Test
    public final void convertShouldSucceed() throws JsonGenerationException, JsonMappingException, IOException {

        // Variables
        Restaurant source;
        SerializationConfig serializationConfig;
        String jsonString;
        byte[] jsonByteArray;

        // Given
        source = new Restaurant();
        serializationConfig = Mockito.mock(SerializationConfig.class);
        jsonString = "{}";
        jsonByteArray = jsonString.getBytes("utf-8");

        // When
        Mockito.when(this.jsonMapper.getSerializationConfig()).thenReturn(serializationConfig);
        Mockito.when(serializationConfig.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS)).thenReturn(
                serializationConfig);
        Mockito.when(this.jsonMapper.writeValueAsString(source)).thenReturn(jsonString);
        byte[] result = this.underTest.convert(source);

        // Then
        Mockito.verify(this.jsonMapper).getSerializationConfig();
        Mockito.verify(serializationConfig).without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        Mockito.verify(this.jsonMapper).writeValueAsString(source);

        Mockito.verifyNoMoreInteractions(this.jsonMapper, serializationConfig);

        assertTrue(Arrays.equals(jsonByteArray, result));

    }
}
