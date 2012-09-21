/*
 *
 */
package fr.midipascher.persistence.store;

import fr.midipascher.domain.Authority;
import fr.midipascher.test.TestFixtures;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Authority database integration testing<br/>
 * CRUD operations are tested<br>
 * Finders are tested<br/>
 *
 * @author louis.gueye@gmail.com
 */
public class AuthorityPersistenceTestIT extends BasePersistenceTestIT {

    /**
     * @param longs
     */
    private void assertResultContainsAuthorityIds(final List<Authority> result, final Set<Long> ids) {
        if (CollectionUtils.isEmpty(result) && ids == null)
            return;

        final Set<Long> authorityIds = new HashSet<Long>();
        for (final Authority authority : result)
            authorityIds.add(authority.getId());

        Assert.assertTrue(authorityIds.containsAll(ids));
    }

    /**
     * Given : a valid authority<br/>
     * When : one persists the above authority<br/>
     * Then : system should retrieve it in database<br/>
     */
    @Test
    public void shouldCreateAuthority() {
        // Given
        final Authority authority = TestFixtures.validAuthority();

        // When
        this.baseDao.persist(authority);
        this.baseDao.flush();

        // Then
        Assert.assertNotNull(authority.getId());
        Assert.assertEquals(authority, this.baseDao.get(Authority.class, authority.getId()));
    }

    /**
     * Given : a valid authority<br/>
     * When : one persists the above authority and then delete it<br/>
     * Then : system should not retrieve it in database<br/>
     */
    @Test
    public void shouldDeleteAuthority() {
        // Given
        final Authority authority = TestFixtures.validAuthority();

        // When
        this.baseDao.persist(authority);
        this.baseDao.flush();
        this.baseDao.delete(Authority.class, authority.getId());
        this.baseDao.flush();

        // Then
        final Authority persistedAuthority = this.baseDao.get(Authority.class, authority.getId());
        Assert.assertNull(persistedAuthority);
    }

    /**
     * Given : one authority with label property valued<br/>
     * When : one searches by the above criterion<br/>
     * Then : system should return authority {id = 1}<br/>
     */
    @Test
    public void shouldFindAuthorityByLabel() {
        // Given
        final Authority authority = new Authority();
        authority.setLabel("anag");

        // When
        final List<Authority> results = this.baseDao.findByExample(authority);

        // Then
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        assertResultContainsAuthorityIds(results, new HashSet<Long>(Arrays.asList(2L)));

    }

    /**
     * Given : a valid authority<br/>
     * When : one updates that authority<br/>
     * Then : system should persist changes<br/>
     */
    @Test
    public void shouldUpdateAuthority() {
        // Given
        final Authority authority = TestFixtures.validAuthority();
        this.baseDao.persist(authority);
        this.baseDao.flush();
        this.baseDao.evict(authority);
        final String label = RandomStringUtils.random(50);
        authority.setLabel(label);

        // When
        this.baseDao.merge(authority);
        this.baseDao.flush();
        final Authority persistedAuthority = this.baseDao.get(Authority.class, authority.getId());

        // Then
        Assert.assertEquals(authority.getLabel(), persistedAuthority.getLabel());
    }

}
