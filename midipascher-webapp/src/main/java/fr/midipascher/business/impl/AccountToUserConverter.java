/**
 * 
 */
package fr.midipascher.business.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.Account;

/**
 * @author louis.gueye@gmail.com
 */
@Component(AccountToUserConverter.BEAN_ID)
public class AccountToUserConverter implements Converter<Account, User> {

	public static final String						BEAN_ID	= "accountToUserConverter";

	@Autowired
	private AuthorityToGrantedAuthorityConverter	authorityToGrantedAuthorityConverter;

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public User convert(Account source) {

		if (source == null) return null;

		String username = source.getEmail();
		String password = source.getPassword();
		boolean enabled = !source.isLocked();
		boolean accountNonExpired = enabled;
		boolean credentialsNonExpired = enabled;
		boolean accountNonLocked = enabled;
		Collection<? extends GrantedAuthority> grantedAuthorities = this.authorityToGrantedAuthorityConverter
				.fromAuthorities(source.getAuthorities());
		return new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
				grantedAuthorities);

	}

}
