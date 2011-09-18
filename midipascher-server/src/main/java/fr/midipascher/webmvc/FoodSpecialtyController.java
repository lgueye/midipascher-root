/**
 * 
 */
package fr.midipascher.webmvc;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Controller
@RequestMapping(value = "/foodspecialty")
public class FoodSpecialtyController {

	@Autowired
	private Facade	facade;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public HttpEntity<String> create(@RequestBody final FoodSpecialty foodSpecialty, final HttpServletRequest request)
			throws Throwable {

		final Long id = this.facade.createFoodSpecialty(foodSpecialty);

		final HttpHeaders headers = new HttpHeaders();

		final String location = request.getRequestURL().append("/").append(id).toString();

		headers.setLocation(URI.create(location));

		final ResponseEntity<String> responseEntity = new ResponseEntity<String>(headers, HttpStatus.CREATED);

		return responseEntity;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public FoodSpecialty get(@PathVariable("id") final Long foodSpecialtyId) {
		return this.facade.readFoodSpecialty(foodSpecialtyId);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<FoodSpecialty> list() {
		return this.facade.listFoodSpecialties();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public HttpEntity<String> update(@RequestBody final FoodSpecialty foodSpecialty) throws Throwable {

		this.facade.updateFoodSpecialty(foodSpecialty);

		final ResponseEntity<String> responseEntity = new ResponseEntity<String>(HttpStatus.OK);

		return responseEntity;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public HttpEntity<String> delete(@PathVariable("id") final Long foodSpecialtyId) {

		this.facade.deleteFoodSpecialty(foodSpecialtyId);

		final ResponseEntity<String> responseEntity = new ResponseEntity<String>(HttpStatus.OK);

		return responseEntity;

	}
}
