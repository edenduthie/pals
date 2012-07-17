package pals.utils;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Configuration;
import pals.analysis.AnalysisException;

public class FileUtilsTest 
{
	String command = "R -f D:\\code\\r\\ConvertNetfluxToNcdf.R C:\\tomcat\\work\\Catalina\\localhost\\pals\\upload__2ef13f0b_12b0938090f__8000_00000013.tmp D:\\code\\webappdata\\eduthie\\ds8.16_met.nc D:\\code\\webappdata\\eduthie\\ds8.16_flux.nc";
	String TEST_FILE = "testinput1.tmp";
	String TEST_MET = "met.nc";
	String TEST_FLUX = "flux.nc";
	
	/**
	 * Tests that the R script to convert from csv to netcdf runs correctly on this system.
	 * Note that the R executable must be installed and also the ncdf and pals R packages.
	 * 
	 * @throws AnalysisException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
    public void testExecuteCommandConvertNetfluxToNcdf() throws AnalysisException, IOException, InterruptedException
    {	
		String curDir = System.getProperty("user.dir"); 
		
		String command = Configuration.getInstance().CONVERT_NETFLUX_TO_NC_CMD;
		String inputFileName = curDir + "/" + BaseTest.TEST_DATA_DIR + "/" + TEST_FILE;
		String outputMet = curDir + "/" + BaseTest.TEST_DATA_DIR + "/" + TEST_MET;
		String outputFlux = curDir + "/" + BaseTest.TEST_DATA_DIR + "/" + TEST_FLUX;
		
		command += " " + inputFileName + " " + outputMet + " " + outputFlux;
		
		PalsFileUtils.executeCommand(command);
		
		File fileMet = new File(outputMet);
		Assert.assertTrue(fileMet.exists());
		fileMet.deleteOnExit();
		
		File fileFlux = new File(outputFlux);
		Assert.assertTrue(fileFlux.exists());
		fileFlux.deleteOnExit();
    }
	
	@Test
	public void testCorrectSlashesForR()
	{
		String input = "D:/code/webappdata";
		String result = PalsFileUtils.correctSlashesForR(input);
		Assert.assertEquals(result,"D:\\\\code\\\\webappdata");
	}
}
