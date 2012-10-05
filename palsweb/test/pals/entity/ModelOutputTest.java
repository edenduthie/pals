package pals.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Configuration;

public class ModelOutputTest 
{
	@Test
    public void testRetrieveFilePath()
    {
    	ModelOutput modelOutput = TestEntityFactory.modelOutput();
    	modelOutput.setId(1);
    	String filePath = modelOutput.retrieveOutputFilePath();
    	Assert.assertEquals(filePath,Configuration.getInstance().PATH_TO_APP_DATA + "/username/mo1.nc");
    }
}
