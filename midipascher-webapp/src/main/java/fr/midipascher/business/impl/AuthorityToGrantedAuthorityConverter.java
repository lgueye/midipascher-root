/**
 * 
 */
package fr.midipascher.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import fr.midipascher.domain.Authority;

/**
 * @author louis.gueye@gmail.com
 */
@Component(AuthorityToGrantedAuthorityConverter.BEAN_ID)
public class AuthorityToGrantedAuthorityConverter implements Converter<Authority, GrantedAuthority> {

	public static final String	BEAN_ID	= "authorityToGrantedAuthorityConverter";

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public GrantedAuthority convert(Authority source) {
		if (source == null) return null;
		return new SimpleGrantedAuthority(Authority.ROLE_PREFIX + source.getCode().toUpperCase());
	}

	/**
	 * @param authorities
	 * @return
	 */
	public Collection<GrantedAuthority> fromAuthorities(Set<Authority> authorities) {

		Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();

		if (authorities != null) for ( Authority authority : authorities ) {
			GrantedAuthority grantedAuthority = this.convert(authority);
			if (grantedAuthority != null) collection.add(grantedAuthority);
		}

		return collection;
	}

}
