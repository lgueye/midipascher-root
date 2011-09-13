/**
 * 
 */
package fr.midipascher.domain.exceptions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import junit.framework.Assert;

import org.junit.Test;

import fr.midipascher.domain.Constants;

/**
 * @author louis.gueye@gmail.com
 */
public class BusinessExceptionTest {

    /**
     * Test method for {@link fr.midipascher.domain.exceptions.deepintoj2ee.wishlister.domain.exceptions.BusinessException#getMessage()}.
     */
    @Test
    public final void testGetMessage() {
        final String messageCode = "test.code";
        final Object[] messageArgs = new Object[] { "sdfsdf", 5L };
        final String defaultMessage = "default message";
        final Locale locale = Locale.ENGLISH;
        final Throwable cause = new Throwable("bla bla");
        BusinessException e = null;

        e = new BusinessException(null, messageArgs, defaultMessage, cause, locale);
        Assert.assertEquals(e.getDefaultMessage(), e.getMessage());

        final String rawMessage = ResourceBundle.getBundle(Constants.MESSAGES_BUNDLE_NAME).getString(messageCode);
        e = new BusinessException(messageCode, null, defaultMessage, cause, locale);
        Assert.assertEquals(rawMessage, e.getMessage());

        e = new BusinessException(messageCode, messageArgs, defaultMessage, cause, locale);
        Assert.assertEquals(MessageFormat.format(rawMessage, messageArgs), e.getMessage());
    }

}
