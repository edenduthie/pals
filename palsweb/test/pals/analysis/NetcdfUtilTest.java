package pals.analysis;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Configuration;
import pals.entity.ModelOutput;
import pals.entity.TestEntityFactory;
import ucar.nc2.NetcdfFile;

public class NetcdfUtilTest 
{
	String filename ="testdata/flux.nc";
	ModelOutput entity;
	File testFile;
	String path;
	
	public void setUp() throws Exception
	{
		entity = TestEntityFactory.modelOutput();
		entity.setId(1);
		File testFileSrc = new File(filename);
		path = entity.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
	}
	
	public void tearDown() throws Exception
	{
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
	}
	
    @Test
    public void testParse() throws Exception
    {
    	setUp();
    	NetcdfFile ncFile = NetcdfUtil.parse(path);
    	Assert.assertNotNull(ncFile);
    	tearDown();
    }
}
