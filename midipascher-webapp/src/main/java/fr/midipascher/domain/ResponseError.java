/**
 * 
 */
package fr.midipascher.domain;

/**
 * @author louis.gueye@gmail.com
 */
public class ResponseError extends AbstractObject {

	private String	message;
	private int		httpStatus;

	/**
	 * @param errorCode
	 * @param message
	 * @param httpStatus
	 */
	public ResponseError(String message, int httpStatus) {
		setMessage(message);
		setHttpStatus(httpStatus);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	private void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the httpStatus
	 */
	public int getHttpStatus() {
		return this.httpStatus;
	}

	/**
	 * @param httpStatus
	 *            the httpStatus to set
	 */
	private void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

}
