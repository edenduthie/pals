package pals.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.util.Parser;

public class ParserTest 
{
	@Test
    public void testRemoveSpecialCharacters()
    {
    	String withSpecialCharacters = "`~@#$%^&*()+=[]{}|\\\"'<>/\t\n\r";
    	String without = Parser.removeSpecialCharacters(withSpecialCharacters);
    	Assert.assertEquals(without.length(),0);
    }
	
	@Test
    public void testRemoveSpecialCharactersAllowSome()
    {
    	String withSpecialCharacters = "`~@#$%^&*()+=[]{}|\\\"'<>/\t\n\r ,.?:;";
    	String without = Parser.removeSpecialCharacters(withSpecialCharacters);
    	Assert.assertEquals(without," ,.?:;");
    }
	
	@Test
    public void testRemoveSpecialCharactersCapitalLetter()
    {
    	String withSpecialCharacters = "A`~@#$%^&*()+=[]{}|\\\"'<>/\t\n\r";
    	String without = Parser.removeSpecialCharacters(withSpecialCharacters);
    	Assert.assertEquals(without.length(),1);
    }
	
	@Test
	public void testRemoveNonAlphaSpaceOrDash()
	{
		String allGood = "abc123 _-";
		String without = Parser.removeNonAlphaSpaceOrDash(allGood);
		Assert.assertEquals(without,allGood);
	}
	
	@Test
	public void testRemoveNonAlphaSpaceOrDashSomeRemoved()
	{
		String someRemoved = "abc123 _-*#&#&#&#";
		String without = Parser.removeNonAlphaSpaceOrDash(someRemoved);
		Assert.assertEquals(without,"abc123 _-");
	}
	
	@Test
	public void testRemoveSingleQuote()
	{
		String withQuote = "that's";
		Assert.assertEquals(Parser.removeSingleQuote(withQuote),"thats");
	}
}
