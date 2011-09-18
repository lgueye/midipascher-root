import org.junit.Assert
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
import org.springframework.web.client.RestTemplate

import fr.midipascher.domain.FoodSpecialty
import fr.midipascher.test.TestUtils

scenario "A valid user should be able to delete a food specialty" , {
	
	def HttpHeaders headers
	def ResponseEntity responseEntity
	def ctx = new ClassPathXmlApplicationContext("classpath:stories-context.xml")
	def RestTemplate restTemplate = ctx.getBean("restTemplate")
	def location = "http://localhost:9080/midipascher-webapp/foodspecialty"
	def requestContentType = "application/xml"
	def responseContentType = "application/xml"
    
	given "I am a valid service user", {
	}

	and "I am allowed to update resources", {
	}
	
	and "I send 'application/xml'", {
		headers	= new HttpHeaders()
		headers.setContentType(MediaType.valueOf(requestContentType))
		headers.setAcceptCharset(Arrays.asList(MappingJacksonHttpMessageConverter.DEFAULT_CHARSET))
	}
	
	and "I receive 'application/xml'", {
		headers.setAccept(Arrays.asList(MediaType.valueOf(responseContentType)))
	}
	
	when "I send a POST request on food specialty with valid body", {

		foodSpecialty = TestUtils.validFoodSpecialty()

		responseEntity = restTemplate.exchange(location, HttpMethod.POST, new HttpEntity<FoodSpecialty>(foodSpecialty, headers), null)

	}
	
	then "the response status code should be 201", {
        Assert.assertNotNull(responseEntity)
		Assert.assertEquals(responseEntity.getStatusCode().value, 201)
		Assert.assertFalse(responseEntity.hasBody())
	}
	
	and "I should be able to successfully read location", {
		location = responseEntity.getHeaders().getLocation()
		Assert.assertNotNull(location)
		responseEntity = restTemplate.getForEntity(location, FoodSpecialty.class)
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(responseEntity.getStatusCode().value, 200)
        Assert.assertTrue(responseEntity.hasBody())
	}
     
    when "I send a DELETE request at that location", {
		responseEntity = restTemplate.exchange(location, HttpMethod.DELETE, null, null)
        Assert.assertNotNull(responseEntity)
		// delete went well
        Assert.assertEquals(200, responseEntity.getStatusCode().value)
    }
            
    and "I send a GET request at that location", {
		responseEntity = restTemplate.exchange(location, HttpMethod.GET, null, null)
    }
	
    then "I should not successfully read the food specialty at this location", {
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(404, responseEntity.getStatusCode().value)
        Assert.assertFalse(responseEntity.hasBody())
    }

}