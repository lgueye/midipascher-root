/**
 * 
 */
package fr.midipascher.web;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(AuthenticationFilter.BEAN_ID)
public class AuthenticationFilter implements Filter {

	public static final String	BEAN_ID	= "authenticationFilter";

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Context
	SecurityContext	sctx;

	@Context
	UriInfo			uriInfo;

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		if (authenticationRequired(this.uriInfo.getPath()))
		 check credentials
		 try {
		 extractCredentialsFromHeader();
		 } catch (Throwable th) {
		 sendLocalized401Message();
		 }
		 try {
		 authenticationService.checkCredentials(uid, password);
		 } catch (Throwable th) {
		 sendLocalized401Message();
		 }
		 Extract from header
		 if (ok) chain.doFilter();
		 else {
		 Locale locale = localeResolver.resolveLocale(request);
		 String i18nMessage = this.messageSource.getMessage("401", null,
		 locale);

		// (response.sendError(401, i18nMessage))
		// }

	}

	private UsernamePasswordCredentials extractCredentialsFromHeaders(HttpServletRequest request) {

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isEmpty(authHeader)) return null;

		StringTokenizer st = new StringTokenizer(authHeader);

		if (!st.hasMoreTokens()) return null;

		String basic = st.nextToken();

		if (!basic.equalsIgnoreCase("Basic")) throw new UnsupportedOperationException("BASIC Auth only supported");

		String credentials = st.nextToken();

		Base64 decoder = new Base64();

		String userPass = new String(decoder.decode(credentials));

		if (StringUtils.isEmpty(userPass) || userPass.indexOf(":") < 0) return null;

		return new UsernamePasswordCredentials(userPass);

	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
