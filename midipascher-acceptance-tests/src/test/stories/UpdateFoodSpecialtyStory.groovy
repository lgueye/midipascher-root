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

scenario "A valid user should be able to update a food specialty" , {
	
	def HttpHeaders headers
	def ResponseEntity responseEntity
	def ctx = new ClassPathXmlApplicationContext("classpath:stories-context.xml")
	def RestTemplate restTemplate = ctx.getBean("restTemplate")
    def foodSpecialty
	def location = "http://localhost:9080/midipascher-webapp/foodspecialty"
	def requestContentType = "application/xml"
	def responseContentType = "application/xml"
    
    def updatedCode = "New Code"
    def updatedLabel = "Brand New label"
    
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
		responseEntity = restTemplate.exchange(location, HttpMethod.GET, new HttpEntity<FoodSpecialty>(headers), FoodSpecialty.class)
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(responseEntity.getStatusCode().value, 200)
        Assert.assertNotNull(responseEntity.body)
	}
     
    when "I send a PUT request on food specialty with valid body", {
        
        foodSpecialty = responseEntity.body
        foodSpecialty.setCode(updatedCode)
        foodSpecialty.setLabel(updatedLabel)
        
		restTemplate.exchange(location, HttpMethod.PUT, new HttpEntity<FoodSpecialty>(foodSpecialty, headers), null)
		
    }
            
    and "I send a GET request at that location", {
		headers	= new HttpHeaders()
		headers.setContentType(MediaType.valueOf(requestContentType))
		headers.setAcceptCharset(Arrays.asList(MappingJacksonHttpMessageConverter.DEFAULT_CHARSET))
		headers.setAccept(Arrays.asList(MediaType.valueOf(responseContentType)))
		requestEntity = new HttpEntity(headers)
		responseEntity = restTemplate.exchange(location, HttpMethod.GET, requestEntity, FoodSpecialty.class)
    }
	
    then "I sould successfully read the food specialty at this location", {
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(responseEntity.getStatusCode().value, 200)
        Assert.assertNotNull(responseEntity.body)
    }
	
	and "the food specialty properties should have been successfully updated", {
		Assert.assertEquals(responseEntity.body.code, updatedCode)
		Assert.assertEquals(responseEntity.body.label, updatedLabel)
	}


}