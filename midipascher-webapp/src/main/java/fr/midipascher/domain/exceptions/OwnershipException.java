/**
 * 
 */
package fr.midipascher.domain.exceptions;

/**
 * @author louis.gueye@gmail.com
 */
public class OwnershipException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * @param messageCode
     * @param messageArgs
     * @param defaultMessage
     */
    public OwnershipException(String messageCode, Object[] messageArgs, String defaultMessage) {
        super(messageCode, messageArgs, defaultMessage);
    }

}
