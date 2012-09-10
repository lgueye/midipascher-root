/**
 * 
 */
package fr.midipascher.domain;

import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ext.JodaSerializers.DateTimeSerializer;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.google.common.base.Preconditions;

import fr.midipascher.domain.validation.Create;
import fr.midipascher.domain.validation.Update;
import fr.midipascher.domain.validation.ValidEmail;

/**
 * @author louis.gueye@gmail.com
 */
@Entity
@Table(name = Account.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { Account.COLUMN_NAME_EMAIL }) })
@XmlRootElement
public class Account extends AbstractEntity {

	public static final String	TABLE_NAME						= "account";
	public static final String	TABLE_NAME_USER_AUTHORITY		= "accounts_authorities";
	public static final String	TABLE_NAME_USER_RESTAURANT		= "accounts_restaurants";

	public static final String	COLUMN_NAME_ID					= "account_id";
	public static final String	COLUMN_NAME_LAST_CONNECTION		= "last_connection";
	public static final String	COLUMN_NAME_FIRST_NAME			= "first_name";
	public static final String	COLUMN_NAME_LAST_NAME			= "last_name";
	public static final String	COLUMN_NAME_EMAIL				= "email";

	public static final int		CONSTRAINT_FIRST_NAME_MAX_SIZE	= 50;
	public static final int		CONSTRAINT_LAST_NAME_MAX_SIZE	= 50;
	public static final int		CONSTRAINT_EMAIL_MAX_SIZE		= 100;
	public static final int		CONSTRAINT_PASSWORD_MAX_SIZE	= 200;

	/**
	 * 
	 */
	private static final long	serialVersionUID				= -5952533696555432772L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = Account.COLUMN_NAME_ID)
	private Long				id;

	@Column(name = Account.COLUMN_NAME_FIRST_NAME)
	@NotEmpty(message = "{account.firstName.required}", groups = { Create.class, Update.class })
	@Size(max = Account.CONSTRAINT_FIRST_NAME_MAX_SIZE, message = "{account.firstName.max.size}", groups = {
			Create.class, Update.class })
	private String				firstName;

	@Column(name = Account.COLUMN_NAME_LAST_NAME)
	@NotEmpty(message = "{account.lastName.required}", groups = { Create.class, Update.class })
	@Size(max = Account.CONSTRAINT_LAST_NAME_MAX_SIZE, message = "{account.lastName.max.size}", groups = {
			Create.class, Update.class })
	private String				lastName;

	@NotEmpty(message = "{account.email.required}", groups = { Create.class, Update.class })
	@ValidEmail(message = "{account.email.valid.format.required}", groups = { Create.class, Update.class })
	@Size(max = Account.CONSTRAINT_EMAIL_MAX_SIZE, message = "{account.email.max.size}", groups = { Create.class,
			Update.class })
	private String				email;

	@NotEmpty(message = "{account.password.required}", groups = { Create.class, Update.class })
	@Size(max = Account.CONSTRAINT_PASSWORD_MAX_SIZE, message = "{account.password.max.size}", groups = { Create.class,
			Update.class })
	private String				password;

	private boolean				locked;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = DateTimeSerializer.class)
	private DateTime			created;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = DateTimeSerializer.class)
	private DateTime			updated;

	@Column(name = Account.COLUMN_NAME_LAST_CONNECTION)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@JsonSerialize(using = DateTimeSerializer.class)
	private DateTime			lastConnection;

	@ManyToMany
	@JoinTable(name = Account.TABLE_NAME_USER_AUTHORITY, joinColumns = { @JoinColumn(name = Account.COLUMN_NAME_ID) }, inverseJoinColumns = { @JoinColumn(name = Authority.COLUMN_NAME_ID) })
	@NotEmpty(message = "{account.authorities.required}", groups = { Create.class, Update.class })
	private Set<Authority>		authorities;

	/**
	 * From JBoss documentation<br>
	 * http://docs.jboss.org/hibernate/annotations/3.5/reference/en/html/entity.
	 * html#entity-mapping-association<br/>
	 * A unidirectional one to many using a foreign key column in the owned
	 * entity is not that common and not <br/>
	 * really recommended. We strongly advise you to use a join table for this
	 * kind of association <br/>
	 * (as explained in the next section). This kind of association is described
	 * through a @JoinColumn<br/>
	 * <br/>
	 * A unidirectional one to many with join table is much preferred. This
	 * association is described through an @JoinTable <br/>
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = Account.TABLE_NAME_USER_RESTAURANT, joinColumns = { @JoinColumn(name = Account.COLUMN_NAME_ID) }, inverseJoinColumns = { @JoinColumn(name = Restaurant.COLUMN_NAME_ID) })
	private Set<Restaurant>		restaurants;

	/**
	 * @param authority
	 */
	public void addAuthority(final Authority authority) {
		Preconditions.checkArgument(authority != null, "Illegal call to addAuthority, authority is required");
		if (this.authorities == null) this.authorities = new HashSet<Authority>();
		this.authorities.add(authority);
	}

	/**
	 * @param restaurant
	 */
	public void addRestaurant(final Restaurant restaurant) {
		Preconditions.checkArgument(restaurant != null, "Illegal call to addRestaurant, restaurant is required");
		if (this.restaurants == null) this.restaurants = new HashSet<Restaurant>();
		this.restaurants.add(restaurant);
	}

	/**
	 * 
	 */
	public void clearAuthorities() {
		if (this.authorities == null) return;
		this.authorities.clear();
	}

	/**
	 * @return
	 */
	public int countAuthorities() {
		if (CollectionUtils.isEmpty(this.authorities)) return 0;
		return this.authorities.size();
	}

	/**
	 * @return
	 */
	public int countRestaurants() {
		if (CollectionUtils.isEmpty(this.restaurants)) return 0;
		return this.restaurants.size();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Account other = (Account) obj;
		if (this.id == null) {
			if (other.id != null) return false;
		} else if (!this.id.equals(other.id)) return false;
		return true;
	}

	/**
	 * @return the authorities
	 */
	public Set<Authority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * @return the created
	 */
	public DateTime getCreated() {
		return this.created;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the lastConnection
	 */
	public DateTime getLastConnection() {
		return this.lastConnection;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @return the restaurants
	 */
	public Set<Restaurant> getRestaurants() {
		return this.restaurants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return this.locked;
	}

	/**
	 * @param restaurantId
	 */
	public void removeRestaurant(final Long restaurantId) {

		if (restaurantId != null && countRestaurants() > 0) {

			final Iterator<Restaurant> restaurantsIterator = this.restaurants.iterator();

			while (restaurantsIterator.hasNext()) {

				final Restaurant restaurant = restaurantsIterator.next();

				if (restaurant != null && restaurantId.equals(restaurant.getId())) {

					restaurantsIterator.remove();

					break;

				}

			}

		}

	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(final Set<Authority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(final DateTime created) {
		this.created = created;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @param lastConnection
	 *            the lastConnection to set
	 */
	public void setLastConnection(final DateTime lastConnection) {
		this.lastConnection = lastConnection;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @param restaurants
	 *            the restaurants to set
	 */
	public void setRestaurants(final Set<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}

	/**
	 * @return the updated
	 */
	public DateTime getUpdated() {
		return this.updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}

}
