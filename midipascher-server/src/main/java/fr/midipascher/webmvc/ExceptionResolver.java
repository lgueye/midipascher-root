/**
 * 
 */
package fr.midipascher.webmvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;

import fr.midipascher.domain.ResponseError;
import fr.midipascher.domain.exceptions.NotFoundException;

/**
 * @author louis.gueye@gmail.com
 */
public class ExceptionResolver {

	public ResponseError resolve(Throwable th, HttpServletRequest request) {
		String message = resolveMesage(request, th);
		int httpStatus = resolveHttpStatus(th);
		return new ResponseError(message, httpStatus);
	}

	/**
	 * @param th
	 * @return
	 */
	int resolveHttpStatus(Throwable th) {
		if (th instanceof IllegalArgumentException) return HttpStatus.BAD_REQUEST.value();
		if (th instanceof IllegalStateException) return HttpStatus.INTERNAL_SERVER_ERROR.value();
		if (th instanceof NotFoundException) return HttpStatus.NOT_FOUND.value();
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

	/**
	 * @param request
	 * @param errorCode
	 * @param th
	 * @return
	 */
	String resolveMesage(HttpServletRequest request, Throwable th) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
