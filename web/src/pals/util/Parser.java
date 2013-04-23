package pals.util;

public class Parser 
{
	public static String removeSpecialCharacters(String input)
	{
		if( input == null ) return null;
		input = input.trim();
		if( input.length() <=0 ) return null;
		return input.replaceAll("[^0-9a-zA-Z .,?;:]", "");
	}
	
    public static String parseSearchString(String searchString) 
    {
    	if( searchString == null ) return null;
		String result = searchString.replaceAll("[^\\d\\w\\s]+", "");
		result = result.replaceAll("[_]+", "");
		result = result.replaceAll("\\s+"," | ");
		return result;
	}
    
	public static String removeNonAlphaSpaceOrDash(String input)
	{
		if( input == null ) return null;
		input = input.trim();
		if( input.length() <=0 ) return null;
		return input.replaceAll("[^0-9a-zA-Z _-]", "");
	}
	
	public static String removeSingleQuote(String input)
	{
		if( input == null ) return null;
		input = input.trim();
		if( input.length() <=0 ) return null;
		return input.replaceAll("'","");
	}
}
