/**
 * 
 */
package fr.midipascher.webmvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.midipascher.domain.exceptions.BusinessException;
import fr.midipascher.domain.exceptions.NotFoundException;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionResolverTest {

	private ExceptionResolver	underTest;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.underTest = new ExceptionResolver();
	}

	/**
	 * Test method for
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
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
	 * {@link fr.midipascher.webmvc.ExceptionResolver#resolveHttpStatus(Throwable)}
	 * .
	 */
	@Test
	public final void resolveMessageShouldInvokeLocalizedExceptionMessageResolver() {
		BusinessException th = Mockito.mock(BusinessException.class);
		String preferredLanguage = "es";
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Accept-Language")).thenReturn(preferredLanguage);
		this.underTest.resolveMesage(request, th);
		Mockito.verify(th).getMessage(preferredLanguage);
	}

}
