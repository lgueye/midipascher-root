/**
 * 
 */
package fr.midipascher.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import fr.midipascher.domain.validation.Create;
import fr.midipascher.domain.validation.Delete;
import fr.midipascher.domain.validation.Update;

/**
 * @author louis.gueye@gmail.com
 */
@Entity
@Table(name = Restaurant.TABLE_NAME)
@XmlRootElement
public class Restaurant extends AbstractEntity {

	public static final String	TABLE_NAME								= "restaurant";
	public static final String	TABLE_NAME_RESTAURANT_FOOD_SPECIALTY	= "restaurant_food_specialty";

	public static final String	COLUMN_NAME_ID							= "restaurant_id";
	public static final String	COLUMN_NAME_PHONE_NUMBER				= "phone_number";
	public static final String	COLUMN_NAME_MAIN_OFFER					= "main_offer";

	public static final int		CONSTRAINT_NAME_MAX_SIZE				= 50;
	public static final int		CONSTRAINT_DESCRIPTION_MAX_SIZE			= 200;
	public static final int		CONSTRAINT_EMAIL_MAX_SIZE				= 100;
	public static final int		CONSTRAINT_PHONE_NUMBER_MAX_SIZE		= 20;
	public static final int		CONSTRAINT_MAIN_OFFER_MAX_SIZE			= 200;

	/**
	 * 
	 */
	private static final long	serialVersionUID						= -5952533696555432772L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Restaurant.COLUMN_NAME_ID)
	@NotNull(message = "{restaurant.id.required}", groups = { Update.class, Delete.class })
	private Long				id;

	@NotEmpty(message = "{restaurant.name.required}", groups = { Create.class, Update.class })
	@Size(max = Restaurant.CONSTRAINT_NAME_MAX_SIZE, message = "{restaurant.name.max.size}", groups = { Create.class,
			Update.class })
	private String				name;

	@Size(max = Restaurant.CONSTRAINT_DESCRIPTION_MAX_SIZE, message = "{restaurant.description.max.size}", groups = {
			Create.class, Update.class })
	private String				description;

	@NotNull(message = "{restaurant.email.required}", groups = { Create.class, Update.class })
	@Email(message = "{restaurant.email.valid.format.required}", groups = { Create.class, Update.class })
	@Size(max = Restaurant.CONSTRAINT_EMAIL_MAX_SIZE, message = "{restaurant.email.max.size}", groups = { Create.class,
			Update.class })
	private String				email;

	@Column(name = Restaurant.COLUMN_NAME_PHONE_NUMBER)
	@NotEmpty(message = "{restaurant.phoneNumber.required}", groups = { Create.class, Update.class })
	@Size(max = Restaurant.CONSTRAINT_PHONE_NUMBER_MAX_SIZE, message = "{restaurant.phoneNumber.max.size}", groups = {
			Create.class, Update.class })
	private String				phoneNumber;

	@Column(name = Restaurant.COLUMN_NAME_MAIN_OFFER)
	@Size(max = Restaurant.CONSTRAINT_MAIN_OFFER_MAX_SIZE, message = "{restaurant.mainOffer.max.size}", groups = {
			Create.class, Update.class })
	private String				mainOffer;

	private boolean				kosher;

	private boolean				halal;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = Restaurant.TABLE_NAME_RESTAURANT_FOOD_SPECIALTY, joinColumns = { @JoinColumn(name = Restaurant.COLUMN_NAME_ID) }, inverseJoinColumns = { @JoinColumn(name = FoodSpecialty.ID_COLUMN_NAME) })
	@Valid
	@NotEmpty(message = "{restaurant.specialties.required}", groups = { Create.class, Update.class })
	private Set<FoodSpecialty>	specialties;

	@Valid
	@NotNull(message = "{restaurant.address.required}", groups = { Create.class, Update.class })
	private Address				address;

	/**
	 * 
	 */
	public Restaurant() {
		super();
		setAddress(new Address());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Restaurant other = (Restaurant) obj;
		if (this.id == null) {
			if (other.id != null) return false;
		} else if (!this.id.equals(other.id)) return false;
		return true;
	}

	public Address getAddress() {
		return this.address;
	}

	public String getDescription() {
		return this.description;
	}

	public String getEmail() {
		return this.email;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public String getMainOffer() {
		return this.mainOffer;
	}

	public String getName() {
		return this.name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public Set<FoodSpecialty> getSpecialties() {
		return this.specialties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}

	public boolean isHalal() {
		return this.halal;
	}

	public boolean isKosher() {
		return this.kosher;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setHalal(final boolean halal) {
		this.halal = halal;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setKosher(final boolean kosher) {
		this.kosher = kosher;
	}

	public void setMainOffer(final String mainOffer) {
		this.mainOffer = mainOffer;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setSpecialties(final Set<FoodSpecialty> specialties) {
		this.specialties = specialties;
	}

}
