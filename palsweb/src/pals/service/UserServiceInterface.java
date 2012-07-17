package pals.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import pals.PalsException;
import pals.analysis.NetcdfException;
import pals.entity.Analysis;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.User;


public interface UserServiceInterface {

	public User 				authenticateUser(String username, String password);
	
	
	public User 				newUser(String username, String password, String fullName, String shortName, String email);
	
	public DataSetVersion 		newDataSetVersion(User user, DataSet dataSet, File uploadedRawDataSet, String versionName, String description, String originalFileName, List<File> uploads, List<String> uploadFileNames, List<String> uploadContentTypes)  throws CSV2NCDFConversionException, IOException, UploadedFileFormatException;

	public void 				convertDataSetCSV2NCDF(File csvDataSetFile, File outputFluxFile, File outputMetFile, DataSetVersion dsv) throws CSV2NCDFConversionException;
	
	public Analysis			getAnalysisRun(User user, Integer analysisRunId); //  throws SecurityException;
	
	public DataSet				getDataSet(Integer dataSetId);

	public List<Analysis>	getAnalysisRuns(User user, Integer modelOutputId); //  throws SecurityException;
	
	public List<Analysis>	getCompletedAnalysisRunsForUser(User user); //  throws SecurityException;
	
	public long					getPublicModelOutputCount(User user);
	
	public void					setModelOutputPublic(User user, ModelOutput modelOutput, String accessLevel) throws SecurityException;
	
	public void					updateDataSet(User user, DataSet dataSet) throws SecurityException;
	
	public void					incrementDataSetDownloadCount(DataSet dataSet);
	
	public List<User> getAllUsers();
	
	public void saveUser(User user);
}
