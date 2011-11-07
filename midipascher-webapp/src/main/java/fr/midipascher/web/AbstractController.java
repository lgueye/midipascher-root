/**
 * 
 */
package fr.midipascher.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	protected HttpEntity<ResponseError> handleThrowable(HttpServletRequest request, Throwable exception) {

		ResponseError response = this.exceptionResolver.resolve(exception, request);

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Resolved "
				+ response);

		HttpHeaders headers = new HttpHeaders();

		String accept = request.getHeader("Accept");

		if (StringUtils.isEmpty(accept)) headers.setContentType(MediaType.APPLICATION_JSON);
		else
			headers.setContentType(MediaType.valueOf(accept));

		final ResponseEntity<ResponseError> responseEntity = new ResponseEntity<ResponseError>(response, headers,
				HttpStatus.valueOf(response.getHttpStatus()));

		return responseEntity;
	}
}
