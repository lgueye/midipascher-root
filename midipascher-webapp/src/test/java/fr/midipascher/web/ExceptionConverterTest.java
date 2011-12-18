/**
 * 
 */
package fr.midipascher.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.LocaleResolver;

import fr.midipascher.domain.exceptions.BusinessException;
import fr.midipascher.domain.exceptions.NotFoundException;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionConverterTest {

	@Mock
	MessageSource						messageSource;

	@Mock
	LocaleResolver						localeResolver;

	@InjectMocks
	private final ExceptionConverter	underTest	= new ExceptionConverter();

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveHttpStatusShouldMapTo400WithIllegalArgumentException() {
		Throwable th = new IllegalArgumentException();
		int httpStatus = this.underTest.resolveHttpStatus(th);
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpStatus);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveHttpStatusShouldMapTo500WithIllegalStateException() {
		Throwable th = new IllegalStateException();
		int httpStatus = this.underTest.resolveHttpStatus(th);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpStatus);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveHttpStatusShouldMapTo404WithNotFoundException() {
		Throwable th = new NotFoundException();
		int httpStatus = this.underTest.resolveHttpStatus(th);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), httpStatus);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveHttpStatusShouldMapTo200WithNullException() {
		Throwable th = null;
		int httpStatus = this.underTest.resolveHttpStatus(th);
		Assert.assertEquals(HttpStatus.OK.value(), httpStatus);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveMessageShouldReturnEmptyWithBothNullExceptionAndNullRequest() {
		Throwable th = null;
		HttpServletRequest request = null;
		String message = this.underTest.resolveMesage(request, th);
		Assert.assertEquals(StringUtils.EMPTY, message);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveMessageShouldReturnEmptyWithNullException() {
		Throwable th = null;
		HttpServletRequest request = new MockHttpServletRequest();
		String message = this.underTest.resolveMesage(request, th);
		Assert.assertEquals(StringUtils.EMPTY, message);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveMessageShouldReturnUnlocalizedMessageWithNullRequest() {
		String nonLocalizedMessage = "message en francais";
		Throwable th = new IllegalArgumentException(nonLocalizedMessage);
		HttpServletRequest request = null;
		String message = this.underTest.resolveMesage(request, th);
		Assert.assertEquals(nonLocalizedMessage, message);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveMessageShouldReturnUnlocalizedMessageWithNonLocalizedException() {
		String nonLocalizedMessage = "message en francais";
		Throwable th = new IllegalArgumentException(nonLocalizedMessage);
		HttpServletRequest request = new MockHttpServletRequest();
		String message = this.underTest.resolveMesage(request, th);
		Assert.assertEquals(nonLocalizedMessage, message);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveMessageShouldInvokeLocalizedExceptionMessageResolver() {
		BusinessException th = Mockito.mock(BusinessException.class);
		String preferredLanguage = "en";
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Accept-Language")).thenReturn(preferredLanguage);
		this.underTest.resolveMesage(request, th);
		Mockito.verify(th).getMessage(preferredLanguage);
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.web.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveHttpStatusShouldMapTo401WithAuthenticationException() {
		Throwable th = new BadCredentialsException(null);
		int httpStatus = this.underTest.resolveHttpStatus(th);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), httpStatus);
	}

}
