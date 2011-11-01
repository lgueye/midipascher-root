/**
 * 
 */
package fr.midipascher.webmvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.ResponseError;
import fr.midipascher.domain.exceptions.LocalizedException;
import fr.midipascher.domain.exceptions.NotFoundException;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class ExceptionResolver {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(ExceptionResolver.class);

	public ResponseError resolve(Throwable th, HttpServletRequest request) {
		String message = resolveMesage(request, th);
		int httpStatus = resolveHttpStatus(th);
		ResponseError responseError = new ResponseError(message, httpStatus);
		ExceptionResolver.LOGGER.debug("Resolved response error = " + responseError);
		return responseError;
	}

	/**
	 * @param th
	 * @return
	 */
	int resolveHttpStatus(Throwable th) {
		if (th == null) return HttpStatus.OK.value();
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

		if (th == null && request == null) return StringUtils.EMPTY;
		if (th == null) return StringUtils.EMPTY;
		if (request == null) return th.getMessage();
		if (!(th instanceof LocalizedException)) return th.getMessage();

		String preferredLanguage = request.getHeader("Accept-Language");
		return ((LocalizedException) th).getMessage(preferredLanguage);

	}

}
