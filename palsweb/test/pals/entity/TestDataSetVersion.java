package pals.entity;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Configuration;
import pals.Globals;

public class TestDataSetVersion 
{
	@Test
    public void testUploadedFilePathNoSuffix()
    {
    	DataSetVersion dsv = TestEntityFactory.dataSetVersion();
    	dsv.getDataSet().setId(54);
    	dsv.setDataSetId(dsv.getDataSet().getId());
    	String path = dsv.uploadedFilePath();
    	String expected = 
    		Configuration.getInstance().PATH_TO_APP_DATA + File.separator +
		    dsv.getDataSet().getOwner().fileDirectory() + File.separator +
		    Globals.DATA_SET_FILE_PREFIX +
		    dsv.getDataSet().getId() + "." +
		    dsv.getId() + "_orig." + dsv.extension();
    }
	
	@Test 
	public void testExtension()
	{
		DataSetVersion dsv = TestEntityFactory.dataSetVersion();
		Assert.assertEquals(dsv.extension(),"csv");
	}
}
