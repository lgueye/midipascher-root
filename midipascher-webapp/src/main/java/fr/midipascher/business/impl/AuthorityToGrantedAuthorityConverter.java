/**
 * 
 */
package fr.midipascher.business.impl;

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
		return new SimpleGrantedAuthority(Authority.ROLE_PREFIX + source.getCode().toUpperCase());
		// return new SimpleGrantedAuthority(source.getCode());
	}

}
