package pals.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ucar.nc2.NetcdfFile;

public class AnalysisTest
{   
	String filename ="testdata/flux1.nc";
	DataSetVersion entity;
	File testFile;
	String path;
	
	Analysis analysis;
	AnalysisType analysisType;
	
	String executablePath;
	
	@BeforeTest
    public void setUp() throws IOException
    {
    	entity = TestEntityFactory.dataSetVersion();
    	
		File testFileSrc = new File(filename);
		path = entity.retrieveOutputFilePath();
		testFile = new File(path);
		FileUtils.copyFile(testFileSrc, testFile);
		
		analysisType = TestEntityFactory.analysisType();
		analysisType.setExecutablePath(executablePath);
		analysisType.setId(1);
		analysisType.setType(AnalysisType.DATA_SET_ANALYSIS_TYPE);
		analysisType.setVariableName("Qbe");
		
		analysis = TestEntityFactory.analysis();
		analysis.setAnalysable(entity);
		analysis.setAnalysisType(analysisType);
    }
    
	@AfterTest
	public void tearDown() throws IOException
	{
		if( testFile != null && testFile.exists() )
		{
			testFile.deleteOnExit();
		}
	}
}
