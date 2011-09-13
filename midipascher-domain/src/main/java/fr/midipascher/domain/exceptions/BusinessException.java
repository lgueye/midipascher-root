/**
 * 
 */
package fr.midipascher.domain.exceptions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.midipascher.domain.Constants;

/**
 * @author louis.gueye@gmail.com
 */
public class BusinessException extends RuntimeException {

	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(BusinessException.class);

	private String				messageCode;
	private Object[]			messageArgs;
	private String				defaultMessage;
	private Locale				locale;
	private Throwable			cause;

	public BusinessException(final String message) {
		super(message);
	}

	/**
	 * @param messageCode
	 * @param messageArgs
	 * @param defaultMessage
	 */
	public BusinessException(final String messageCode, final Object[] messageArgs, final String defaultMessage) {
		setMessageCode(messageCode);
		setMessageArgs(messageArgs);
		setDefaultMessage(defaultMessage);
	}

	/**
	 * @param messageCode
	 * @param messageArgs
	 * @param defaultMessage
	 * @param locale
	 */
	public BusinessException(final String messageCode, final Object[] messageArgs, final String defaultMessage,
			final Locale locale) {
		this(messageCode, messageArgs, defaultMessage);
		setLocale(locale);
	}

	/**
	 * @param messageCode
	 * @param messageArgs
	 * @param defaultMessage
	 * @param cause
	 */
	public BusinessException(final String messageCode, final Object[] messageArgs, final String defaultMessage,
			final Throwable cause) {
		this(messageCode, messageArgs, defaultMessage);
		setCause(cause);
	}

	/**
	 * @param messageCode
	 * @param messageArgs
	 * @param defaultMessage
	 * @param cause
	 * @param locale
	 */
	public BusinessException(final String messageCode, final Object[] messageArgs, final String defaultMessage,
			final Throwable cause, final Locale locale) {
		this(messageCode, messageArgs, defaultMessage, locale);
		setCause(cause);
	}

	/**
	 * @return the cause
	 */
	@Override
	public Throwable getCause() {
		return this.cause;
	}

	/**
	 * @return the defaultMessage
	 */
	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {

		if (StringUtils.isEmpty(getMessageCode())) return getDefaultMessage();

		final Locale locale = getLocale() == null ? Locale.getDefault() : getLocale();

		ResourceBundle bundle = null;

		try {

			bundle = ResourceBundle.getBundle(Constants.MESSAGES_BUNDLE_NAME, locale);

		} catch (final MissingResourceException e) {

			BusinessException.LOG.debug("Bundle 'message' not found for locale '" + locale.getLanguage()
					+ "'. Using default message");

			return getDefaultMessage();

		}

		String message = null;

		try {

			message = bundle.getString(getMessageCode());

		} catch (final MissingResourceException e) {

			BusinessException.LOG.debug("Message not found for key '" + getMessageCode() + "'. Using default message");

			return getDefaultMessage();

		}

		if (!ArrayUtils.isEmpty(getMessageArgs())) message = MessageFormat.format(message, getMessageArgs());

		return message;
	}

	/**
	 * @return the messageArgs
	 */
	public Object[] getMessageArgs() {
		return this.messageArgs;
	}

	/**
	 * @return the messageCode
	 */
	public String getMessageCode() {
		return this.messageCode;
	}

	/**
	 * @param cause
	 *            the cause to set
	 */
	private void setCause(final Throwable cause) {
		this.cause = cause;
	}

	/**
	 * @param defaultMessage
	 *            the defaultMessage to set
	 */
	private void setDefaultMessage(final String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	private void setLocale(final Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param messageArgs
	 *            the messageArgs to set
	 */
	private void setMessageArgs(final Object[] messageArgs) {
		this.messageArgs = messageArgs;
	}

	/**
	 * @param messageCode
	 *            the messageCode to set
	 */
	private void setMessageCode(final String messageCode) {
		this.messageCode = messageCode;
	}
}
