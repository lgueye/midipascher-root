/**
 *
 */
package fr.midipascher.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PlainTextBasicAuthenticationEntryPoint.BEAN_ID)
public class PlainTextBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	public static final String	BEAN_ID	= "defaultEntryPoint";

	@Autowired
	private ExceptionConverter	exceptionConverter;

	public PlainTextBasicAuthenticationEntryPoint() {
		setRealmName("midipascher.fr");
	}

	/**
	 * @see org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		response.setStatus(this.exceptionConverter.resolveHttpStatus(authException));
		response.setContentType("text/html");
		OutputStream os = response.getOutputStream();
		final String i18nMessage = this.exceptionConverter.resolveMesage(request, authException);
		os.write(i18nMessage.getBytes("UTF-8"));
	}

}
