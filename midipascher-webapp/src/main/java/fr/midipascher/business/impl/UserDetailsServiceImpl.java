/**
 *
 */
package fr.midipascher.business.impl;

import fr.midipascher.domain.Account;
import fr.midipascher.persistence.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author louis.gueye@gmail.com
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private AccountToUserConverter accountToUserConverter;

    /**
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Account exampleInstance = new Account();
        exampleInstance.setEmail(username);
        exampleInstance.setLocked(false);
        Account account = DataAccessUtils.uniqueResult(this.baseDao.findByExample(exampleInstance));
        return this.accountToUserConverter.convert(account);
    }

}
