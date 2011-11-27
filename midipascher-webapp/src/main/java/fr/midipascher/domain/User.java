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
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import fr.midipascher.domain.validation.Create;
import fr.midipascher.domain.validation.Delete;
import fr.midipascher.domain.validation.Update;

/**
 * @author louis.gueye@gmail.com
 */
@Entity
@Table(name = User.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { User.COLUMN_NAME_EMAIL }) })
@XmlRootElement
public class User extends AbstractEntity {

	public static final String	TABLE_NAME						= "users";
	public static final String	TABLE_NAME_USER_AUTHORITY		= "users_authority";

	public static final String	COLUMN_NAME_ID					= "user_id";
	public static final String	COLUMN_NAME_LAST_CONNECTION		= "last_connection";
	public static final String	COLUMN_NAME_FIRST_NAME			= "first_name";
	public static final String	COLUMN_NAME_LAST_NAME			= "last_name";
	public static final String	COLUMN_NAME_EMAIL				= "email";

	public static final int		CONSTRAINT_FIRST_NAME_MAX_SIZE	= 50;
	private static final int	CONSTRAINT_LAST_NAME_MAX_SIZE	= 50;
	public static final int		CONSTRAINT_EMAIL_MAX_SIZE		= 100;
	public static final int		CONSTRAINT_PASSWORD_MAX_SIZE	= 200;

	/**
	 * 
	 */
	private static final long	serialVersionUID				= -5952533696555432772L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = User.COLUMN_NAME_ID)
	@NotNull(message = "{user.id.required}", groups = { Update.class, Delete.class })
	private Long				id;

	@Column(name = User.COLUMN_NAME_FIRST_NAME)
	@NotEmpty(message = "{user.firstName.required}", groups = { Create.class, Update.class })
	@Size(max = User.CONSTRAINT_FIRST_NAME_MAX_SIZE, message = "{user.firstName.max.size}", groups = { Create.class,
			Update.class })
	private String				firstName;

	@Column(name = User.COLUMN_NAME_LAST_NAME)
	@NotEmpty(message = "{user.lastName.required}", groups = { Create.class, Update.class })
	@Size(max = User.CONSTRAINT_LAST_NAME_MAX_SIZE, message = "{user.lastName.max.size}", groups = { Create.class,
			Update.class })
	private String				lastName;

	@NotNull(message = "{user.email.required}", groups = { Create.class, Update.class })
	@Email(message = "{user.email.valid.format.required}", groups = { Create.class, Update.class })
	@Size(max = User.CONSTRAINT_EMAIL_MAX_SIZE, message = "{user.email.max.size}", groups = { Create.class,
			Update.class })
	private String				email;

	@NotEmpty(message = "{user.password.required}", groups = { Create.class, Update.class })
	@Size(max = User.CONSTRAINT_PASSWORD_MAX_SIZE, message = "{user.password.max.size}", groups = { Create.class,
			Update.class })
	private String				password;

	private boolean				locked;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime			created;

	@Column(name = User.COLUMN_NAME_LAST_CONNECTION)
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime			lastConnection;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = User.TABLE_NAME_USER_AUTHORITY, joinColumns = { @JoinColumn(name = User.COLUMN_NAME_ID) }, inverseJoinColumns = { @JoinColumn(name = Authority.COLUMN_NAME_ID) })
	@Valid
	@NotEmpty(message = "{user.authorities.required}", groups = { Create.class, Update.class })
	private Set<Authority>		authorities;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final User other = (User) obj;
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

}
