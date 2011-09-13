import org.junit.Assert
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
import org.springframework.web.client.RestTemplate

import fr.midipascher.domain.FoodSpecialty
import fr.midipascher.test.TestUtils

scenario "A valid user should be able to create a food specialty" , {
	
	def HttpHeaders headers
	def ResponseEntity<String> responseEntity
	def ctx = new ClassPathXmlApplicationContext("classpath:stories-context.xml")
	def RestTemplate restTemplate = ctx.getBean("restTemplate")
    def foodSpecialty
    def location
    
    def updatedCode = "New Code"
    def updatedLabel = "Brand New label"
    
	given "I am a valid service user", {
	}

	and "I am allowed to update resources", {
	}
	
	and "I send 'application/xml'", {
		headers	= new HttpHeaders()
		headers.setContentType(MediaType.valueOf("application/xml"))
		headers.setAcceptCharset(Arrays.asList(MappingJacksonHttpMessageConverter.DEFAULT_CHARSET))
	}
	
	and "I receive 'application/xml'", {
		headers.setAccept(Arrays.asList(MediaType.valueOf("application/xml")))
	}
	
	when "I send a POST request on food specialty with valid body", {

		foodSpecialty = TestUtils.validFoodSpecialty()

		responseEntity = restTemplate.postForEntity("http://localhost:9080/midipascher-webapp/foodspecialty",
				new HttpEntity<FoodSpecialty>(foodSpecialty, headers), String.class)

	}
	
	then "the response status code should be 201", {
        Assert.assertNotNull(responseEntity)
		Assert.assertEquals(responseEntity.getStatusCode().value, 201)
	}
	
	and "I should be able to successfully read location", {
		location = responseEntity.getHeaders().getLocation()
		Assert.assertNotNull(location)
		responseEntity = restTemplate.getForEntity(location, FoodSpecialty.class)
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(responseEntity.getStatusCode().value, 200)
        Assert.assertNotNull(responseEntity.body)
	}
     
    when "I send a PUT request on food specialty with valid body", {
        
        foodSpecialty = responseEntity.body
        foodSpecialty.setCode(updatedCode)
        foodSpecialty.setLabel(updatedLabel)
        
        restTemplate.put(location, foodSpecialty)
        
    }
            
    when "I get a representation", {
        responseEntity = restTemplate.getForEntity(location, FoodSpecialty.class)
        Assert.assertNotNull(responseEntity)
        Assert.assertEquals(responseEntity.getStatusCode().value, 200)
        Assert.assertNotNull(responseEntity.body)
        Assert.assertEquals(responseEntity.body.code, updatedCode)
        Assert.assertEquals(responseEntity.body.label, updatedLabel)
    }
     

}