/*
 *
 */
package fr.midipascher.domain.business;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.validation.ValidationContext;

/**
 * @author louis.gueye@gmail.com
 */
public interface Validator {

    /**
     * @param type
     * @param context
     */
    <T extends AbstractEntity> void validate(T type, ValidationContext context);

}
