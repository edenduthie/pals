package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Globals;
import pals.entity.DataSetVersion;
import pals.entity.TestEntityFactory;

public class UserServiceJPAImplTest
{
	String testFileName = "testdata/DataSet1.csv";
	String testFileNameInvalidTemplate = "testdata/DataSet1_InvalidTemplateVersion.csv";
	String testFileNameInvalidHeader1 = "testdata/DataSet1_InvalidHeader1.csv";
	String testFileNameInvalidHeader2 = "testdata/DataSet1_InvalidHeader2.csv";
	String testFileNameInvalidSingleLine = "testdata/DataSet1_InvalidSingleLine.csv";
	String testFileNameInvalidDate = "testdata/DataSet1_InvalidDate.csv";
	String testFileNameInvalidNumber = "testdata/DataSet1_InvalidNumber.csv";
	String testFileNameDifferentDateFormat = "testdata/DataSet1_DifferentDateFormat.csv";
	String testFileNameTemplate2 = "testdata/PALSFluxTowerTemplate1.0.2.csv";
	String testFlux = "testdata/flux.nc";
	String testMet = "testdata/met.nc";
	
    @Test
    public void testConvertDataSetCSV2NCDF() throws CSV2NCDFConversionException
    {
    	File file = new File(testFileName);
    	Assert.assertTrue( file.exists() );
    	
    	File fluxOutput = new File(testFlux);
    	File metOutput = new File(testMet);
    	
    	DataSetVersion dsv = TestEntityFactory.dataSetVersion();
    	
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	
    	service.convertDataSetCSV2NCDF(file,fluxOutput,metOutput,dsv);
    	
    	Assert.assertTrue(fluxOutput.exists());
    	Assert.assertTrue(metOutput.exists());
    	Assert.assertTrue(fluxOutput.length() > 0 );
    	Assert.assertTrue(metOutput.length() > 0 );
    	
    	fluxOutput.delete();
    	metOutput.delete();
    }
    
    @Test
    public void testCheckFilename() throws UploadedFileFormatException
    {
    	String filenameGood = "ya." + Globals.ALLOWED_EXTENSION;
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	service.checkFilename(filenameGood);
    }
    
    @Test
    public void testCheckFilenameShort()
    {
    	String filenameBad = "ya";
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.checkFilename(filenameBad);
			Assert.fail();
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testCheckFilenameEmpty()
    {
    	String filenameBad = "";
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.checkFilename(filenameBad);
			Assert.fail();
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testCheckFilenameNull()
    {
    	String filenameBad = null;
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.checkFilename(filenameBad);
			Assert.fail();
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testCheckFilenameLong() throws UploadedFileFormatException
    {
    	String filenameGood = "very.long.filename.with.csv.embeded." + Globals.ALLOWED_EXTENSION;
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
	    service.checkFilename(filenameGood);
    }
    
    @Test
    public void testCheckFilenameWrongExtension() throws UploadedFileFormatException
    {
    	String filenameBad = "very.long.filename.with.csv.embeded.yuck";;
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.checkFilename(filenameBad);
			Assert.fail();
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testValidateUploadedFile() throws UploadedFileFormatException, IOException
    {
    	File file = new File(testFileName);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	service.validateUploadedFile(file);
    }
    
    @Test
    public void testValidateUploadedFileInvalidTemplateVersion() throws IOException
    {
    	File file = new File(testFileNameInvalidTemplate);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.validateUploadedFile(file);
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testValidateUploadedFileTemplate2() throws UploadedFileFormatException, IOException
    {
    	File file = new File(testFileNameTemplate2);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	service.validateUploadedFile(file);
    }
    
    @Test
    public void testValidateUploadedFileInvalidHeader1() throws IOException
    {
    	File file = new File(testFileNameInvalidHeader1);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.validateUploadedFile(file);
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testValidateUploadedFileInvalidHeader2() throws IOException
    {
    	File file = new File(testFileNameInvalidHeader2);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.validateUploadedFile(file);
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testValidateUploadedFileInvalidSingleLine() throws IOException
    {
    	File file = new File(testFileNameInvalidSingleLine);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.validateUploadedFile(file);
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testValidateUploadedFileInvalidDate() throws IOException
    {
    	File file = new File(testFileNameInvalidDate);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.validateUploadedFile(file);
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    @Test
    public void testValidateUploadedFileInvalidNumber() throws IOException
    {
    	File file = new File(testFileNameInvalidNumber);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	try {
			service.validateUploadedFile(file);
		} catch (UploadedFileFormatException e) {
			// what we want
		}
    }
    
    public void testConvertDataSetDifferentDateFormat() throws CSV2NCDFConversionException
    {
    	File file = new File(testFileNameDifferentDateFormat);
    	Assert.assertTrue( file.exists() );
    	
    	File fluxOutput = new File(testFlux);
    	File metOutput = new File(testMet);
    	
    	DataSetVersion dsv = TestEntityFactory.dataSetVersion();
    	
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	
    	service.convertDataSetCSV2NCDF(file,fluxOutput,metOutput,dsv);
    	
    	Assert.assertTrue(fluxOutput.exists());
    	Assert.assertTrue(metOutput.exists());
    	Assert.assertTrue(fluxOutput.length() > 0 );
    	Assert.assertTrue(metOutput.length() > 0 );
    	
    	fluxOutput.delete();
    	metOutput.delete();
    }
    
    @Test
    public void testValidateUploadedFileCorrectYears() throws IOException, UploadedFileFormatException
    {
    	File file = new File(testFileName);
    	UserServiceJPAImpl service = new UserServiceJPAImpl();
    	DataSetVersion dsv = new DataSetVersion();
    	dsv.setUploadedFile(file);
	    service.validateUploadedFile(dsv);
	    Date startDate = dsv.getStartDate();
	    Date endDate = dsv.getEndDate();
	    Assert.assertNotNull(startDate);
	    Assert.assertNotNull(endDate);
	    
	    Calendar startCalendar = Calendar.getInstance();
	    startCalendar.setTime(startDate);
	    Assert.assertEquals(startCalendar.get(Calendar.DAY_OF_MONTH),1);
	    Assert.assertEquals(startCalendar.get(Calendar.MONTH),0);
	    Assert.assertEquals(startCalendar.get(Calendar.YEAR),2002);
	    
	    Calendar endCalendar = Calendar.getInstance();
	    endCalendar.setTime(endDate);
	    Assert.assertEquals(endCalendar.get(Calendar.DAY_OF_MONTH),31);
	    Assert.assertEquals(endCalendar.get(Calendar.MONTH),11);
	    Assert.assertEquals(endCalendar.get(Calendar.YEAR),2003);
    }
}
