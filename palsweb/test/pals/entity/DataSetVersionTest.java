package pals.entity;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pals.Configuration;
import pals.Globals;
import pals.analysis.AnalysisException;

public class DataSetVersionTest
{
	DataSetVersion entity;
	Integer dataSetId = 2;
	Integer versionId = 1;
	
	String filenameToCopy = "testdata/ds3.264_met.nc";
	
	@BeforeTest
    public void setUp() throws IOException
    {
    	entity = TestEntityFactory.dataSetVersion();
    }
    
	@AfterTest
	public void tearDown() throws IOException
	{
	}
    
    @Test
    public void testRetrieveOutputFilePath() throws IOException
    {
    	entity.getDataSet().setId(dataSetId);
    	entity.setId(versionId);
    	String filePath = entity.retrieveOutputFilePath();
    	Assert.assertEquals(filePath,
            Configuration.getInstance().PATH_TO_APP_DATA+"/username/ds2.1_flux.nc");
    }
    
    @Test
    public void testGenerateQCPlots() throws IOException, AnalysisException, InterruptedException
    {
//    	File source = new File(filenameToCopy);
//    	
//    	entity.getDataSet().setId(dataSetId);
//    	entity.setId(versionId);
//    	Configuration.getInstance().PATH_TO_APP_DATA = "testdata";
//    	
//    	File dest = new File(entity.retrieveOutputFilePath());
//    	FileUtils.copyFile(source, dest);
//    	
//    	File qcPlotsFile = entity.generateQCPlots();
//    	Assert.assertTrue(qcPlotsFile.exists());
//    	Assert.assertTrue(qcPlotsFile.getName().endsWith(Globals.PNG_FILE_SUFFIX));
//    	
//    	qcPlotsFile.deleteOnExit();
//    	dest.delete();
    }
}
