/**
 * 
 */
package fr.midipascher.webmvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import fr.midipascher.domain.ResponseError;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class AbstractController {

	@Autowired
	private ExceptionResolver	exceptionResolver;

	@ExceptionHandler
	protected HttpEntity<String> handleThrowable(HttpServletRequest request, Throwable exception) {

		ResponseError response = this.exceptionResolver.resolve(exception, request);

		final ResponseEntity<String> responseEntity = new ResponseEntity<String>(response.getMessage(),
				HttpStatus.valueOf(response.getHttpStatus()));

		return responseEntity;
	}

}
