/**
 * 
 */
package fr.midipascher.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import fr.midipascher.domain.validation.Create;
import fr.midipascher.domain.validation.Delete;
import fr.midipascher.domain.validation.Update;

/**
 * @author louis.gueye@gmail.com
 */
@Entity
@Table(name = FoodSpecialty.TABLE_NAME)
@XmlRootElement
public class FoodSpecialty extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID			= 8989705365648473442L;
	public static final String	TABLE_NAME					= "food_specialty";
	public static final String	ID_COLUMN_NAME				= "food_specialty_id";

	public static final int		CONSTRAINT_CODE_MAX_SIZE	= 10;
	public static final int		CONSTRAINT_LABEL_MAX_SIZE	= 50;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = FoodSpecialty.ID_COLUMN_NAME)
	@NotNull(message = "{foodSpecialty.id.required}", groups = { Update.class, Delete.class })
	private Long				id;

	@NotEmpty(message = "{foodSpecialty.code.required}", groups = { Create.class, Update.class })
	@Size(max = FoodSpecialty.CONSTRAINT_CODE_MAX_SIZE, message = "{foodSpecialty.code.max.size}", groups = {
			Create.class, Update.class })
	private String				code;

	@NotEmpty(message = "{foodSpecialty.label.required}", groups = { Create.class, Update.class })
	@Size(max = FoodSpecialty.CONSTRAINT_LABEL_MAX_SIZE, message = "{foodSpecialty.label.max.size}", groups = {
			Create.class, Update.class })
	private String				label;

	private boolean				active;

	public FoodSpecialty() {
		super();
		setActive(true);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FoodSpecialty other = (FoodSpecialty) obj;
		if (this.id == null) {
			if (other.id != null) return false;
		} else if (!this.id.equals(other.id)) return false;
		return true;
	}

	public String getCode() {
		return this.code;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

}
