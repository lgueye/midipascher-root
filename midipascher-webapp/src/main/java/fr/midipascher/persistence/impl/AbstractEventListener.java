/**
 * 
 */
package fr.midipascher.persistence.impl;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.domain.validation.ValidationContext;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class AbstractEventListener {

	@Autowired
	private Validator	validator;

	/**
	 * @see fr.midipascher.domain.business.Facade#validate(fr.midipascher.domain.AbstractEntity,
	 *      fr.midipascher.domain.validation.ValidationContext)
	 */
	protected final <T extends AbstractEntity> void validate(final T type, final ValidationContext context) {

		Preconditions.checkArgument(type != null, "Illegal call to validate, object is required");

		Preconditions.checkArgument(context != null, "Illegal call to validate, validation context is required");

		final Set<ConstraintViolation<T>> constraintViolations = this.validator.validate(type, context.getContext());

		if (CollectionUtils.isEmpty(constraintViolations)) return;

		final Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
				constraintViolations.size());

		for ( final ConstraintViolation<?> violation : constraintViolations )
			propagatedViolations.add(violation);

		throw new ConstraintViolationException(propagatedViolations);

	}

}