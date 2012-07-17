package pals.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LocationTest {
	
	@Test
	public void testConvertToDecimal()
	{
		Location location = new Location();
		String degString = "145";
		String minString = "32";
		String secString = "23.24";
		
		Double decimal = location.convertToDecimal(degString, minString, secString);
		Assert.assertEquals(decimal,145.5397888888889);
	}
}
