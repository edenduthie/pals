package pals.utils;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.analysis.AnalysisException;

public class TestRError 
{
	String script = "R -f testdata/TestErrorScript.R";
	String scriptWithError = "R -f testdata/TestErrorScriptError.R";
	
    @Test
    public void testGetCorrectErrorMessage() throws IOException, InterruptedException
    {
    	try 
    	{
			PalsFileUtils.executeCommand(script);
			Assert.fail();
		} 
    	catch (AnalysisException e) {
			Assert.assertEquals(e.getMessage(),"R script failure");
		}
    }
    
    @Test
    public void testGetCorrectErrorMessageWithError() throws IOException, InterruptedException
    {
    	try 
    	{
			PalsFileUtils.executeCommand(scriptWithError);
			Assert.fail();
		} 
    	catch (AnalysisException e) {
    		System.out.println("Message:' " + e.getMessage() + "'");
			Assert.assertEquals(e.getMessage(),"AnnualCycle analysis requires a whole number of years of data.");
		}
    }
}
