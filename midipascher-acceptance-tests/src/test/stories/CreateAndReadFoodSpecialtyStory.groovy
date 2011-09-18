import org.junit.Assert
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
import org.springframework.web.client.RestTemplate

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant
import fr.midipascher.test.TestUtils

scenario "A valid user should be able to create a restaurant" , {
	
	def HttpHeaders headers
	def ResponseEntity<String> responseEntity
	def ctx = new ClassPathXmlApplicationContext("classpath:stories-context.xml")
	def RestTemplate restTemplate = ctx.getBean("restTemplate")
    def foodSpecialty
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
		responseEntity = restTemplate.exchange(location, HttpMethod.GET, new HttpEntity<FoodSpecialty>(headers), FoodSpecialty.class)
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(responseEntity.getStatusCode().value, 200)
        Assert.assertTrue(responseEntity.hasBody())
        Assert.assertEquals(foodSpecialty.code, responseEntity.body.code)
        Assert.assertEquals(foodSpecialty.label, responseEntity.body.label)
	} 

}