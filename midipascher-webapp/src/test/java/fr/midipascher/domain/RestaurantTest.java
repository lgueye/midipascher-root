/*
 *
 */
package fr.midipascher.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author louis.gueye@gmail.com
 */
public class RestaurantTest {

	private Restaurant	underTest;

	@Test(expected = IllegalArgumentException.class)
	public void addSpecialtyWillThrowIllegalArgumentExceptionWithNullInput() {
		// Given
		final FoodSpecialty specialty = null;

		// When
		underTest.addSpecialty(specialty);
	}

	@Test
	public void addSpecialtyWontFailWithNullSet() {
		// Given
		final FoodSpecialty specialty = new FoodSpecialty();

		final int countSpecialties = underTest.countSpecialties();
		// When
		underTest.addSpecialty(specialty);

		Assert.assertEquals(countSpecialties + 1, underTest.countSpecialties());

	}

	@Test
	public void clearSpecialtiesWillIgnoreIfNullSpecialties() {
		underTest.setSpecialties(null);
		underTest.clearSpecialties();
		Assert.assertNull(underTest.getSpecialties());
	}

	@Test
	public void clearSpecialtiesWillSucceed() {
		final Set<FoodSpecialty> specialties = new HashSet<FoodSpecialty>();
		specialties.add(new FoodSpecialty());
		underTest.setSpecialties(specialties);
		underTest.clearSpecialties();
		Assert.assertNotNull(underTest.getSpecialties());
		Assert.assertTrue(CollectionUtils.sizeIsEmpty(underTest.getSpecialties()));
	}

	@Test
	public void countSpecialtiesShouldReturnSetSize() {
		final Set<FoodSpecialty> authorities = new HashSet<FoodSpecialty>();
		underTest.setSpecialties(authorities);
		Assert.assertEquals(0, underTest.countSpecialties());
		underTest.addSpecialty(new FoodSpecialty());
		Assert.assertEquals(1, underTest.countSpecialties());
	}

	@Test
	public void countSpecialtiesShouldReturnZeroWithEmptySet() {
		underTest.setSpecialties(new HashSet<FoodSpecialty>());
		Assert.assertEquals(0, underTest.countSpecialties());
	}

	@Test
	public void countSpecialtiesShouldReturnZeroWithNullSet() {
		Assert.assertEquals(0, underTest.countSpecialties());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		underTest = new Restaurant();
	}

}
