/**
 * 
 */
package fr.midipascher.webmvc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import fr.midipascher.domain.exceptions.NotFoundException;

/**
 * @author louis.gueye@gmail.com
 */
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

}
