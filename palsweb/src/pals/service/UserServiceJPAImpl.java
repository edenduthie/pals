package pals.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pals.Configuration;
import pals.Globals;
import pals.analysis.AnalysisException;
import pals.entity.Analysable;
import pals.entity.Analysis;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.entity.User;
import pals.utils.PalsFileUtils;

@Transactional
public class UserServiceJPAImpl implements UserServiceInterface 
{	
	private static Logger log = Logger.getLogger(UserServiceJPAImpl.class);
	private EntityManager entityManager;
	
	FileService fileService;

	private void checkUserOwnsModelOutput(User user,
			ModelOutput modelOutput) throws SecurityException {
		if (!user.getUsername().equals(modelOutput.getUserName())) {
			throw new SecurityException("User " + user.getUsername()
					+ " cannot delete modeloutput "
					+ modelOutput.getId() + " as they don't own it.");
		}
	}

	private void checkUserCanAccessDataSet(User user, DataSet dataSet)
			throws SecurityException {
		if (!user.getUsername().equals(dataSet.getUserName())) {
			throw new SecurityException("User " + user.getUsername()
					+ " cannot delete dataset " + dataSet.getId());
		}
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public User authenticateUser(String username, String password) {
		if (username == null || password == null)
			return null;
		log.info("Authenticating user: " + username);
		Query query = entityManager.createQuery(
				"from User where username = :username").setParameter(
				"username", username);
		List result = query.getResultList();
		if (!result.isEmpty()) {
			User user = (User) result.get(0);
			MessageDigest mdEnc = null;
			try {
				mdEnc = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				log.error(e);
			}
			mdEnc.update(password.getBytes(), 0, password.length());
			String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
			if (user != null && user.correctPassword(password)) {
				return user;
			}
		}
		return null;
	}

	public User newUser(String username, String password, String fullName,
			String shortName, String email) {
		log.info("Creating new user with username: " + username);
		User user = new User();
		user.setUsername(username);
		MessageDigest mdEnc = null;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		}
		mdEnc.update(password.getBytes(), 0, password.length());
		String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
		user.setPassword(md5);
		user.setFullName(fullName);
		user.setShortName(shortName);
		user.setEmail(email);
		entityManager.persist(user);
		return user;
	}

	public void updateDataSet(User user, DataSet dataSet)
			throws SecurityException {
		checkUserCanAccessDataSet(user, dataSet);
		entityManager.merge(dataSet);
	}

	public void incrementDataSetDownloadCount(DataSet dataSet) {
		dataSet.incrementDownloadCount();
		entityManager.merge(dataSet);
	}

	public Analysis getAnalysisRun(User user, Integer analysisRunId) {
		// @fix check User has access to this analysis
		Query query = entityManager.createQuery(
				"from Analysis where id = :analysisRunId")
				.setParameter("analysisRunId", analysisRunId);
		return (Analysis) query.getSingleResult();
	}

	public DataSet getDataSet(Integer dataSetId) {
		Query query = entityManager.createQuery(
				"from DataSet where id = :dataSetId").setParameter(
				"dataSetId", dataSetId);
		return (DataSet) query.getSingleResult();
	}

	public List<Analysis> getCompletedAnalysisRunsForUser(User user) {
		Query query = entityManager
				.createQuery(
						"select a from AnalysisRun a, ModelOutput m where a.modelOutputId = m.modelOutputId and m.userName = :userName and a.status = :status and m.isVisible = true and a.visible = true order by a.endTime DESC")
				.setParameter("userName", user.getUsername());
		query.setParameter("status", Analysis.STATUS_COMPLETE);
		// Query query = entityManager.createQuery (
		// "from AnalysisRun where status = :status"
		// ).setParameter("status",AnalysisRun.STATUS_COMPLETE);
		return query.getResultList();
	}

	public List<Analysis> getAnalysisRuns(User user, Integer modelOutputId) {
		Query query = entityManager
				.createQuery(
						"from Analysis where analysable.id = :modelOutputId and visible = true")
				.setParameter("modelOutputId", modelOutputId);
		// .setParameter("status",AnalysisRun.STATUS_COMPLETE);
		return query.getResultList();
	}

	// function that access the file system..

	@Transactional(rollbackFor = { CSV2NCDFConversionException.class, UploadedFileFormatException.class } )
	public DataSetVersion newDataSetVersion(User user, DataSet dataSet,
			File uploadedCSVFile, String versionName, String description,
			String originalFileName,  List<File> uploads, List<String> uploadFileNames, List<String> uploadContentTypes)
			throws CSV2NCDFConversionException, IOException, UploadedFileFormatException {
		
		File storeUploadedFile = null;
		String message = dataSet.getName() + "." + versionName;
		log.info("Creating new data set version: " + message);
		DataSetVersion dsv = new DataSetVersion();
		
        try
        {
			dsv.setDataSetId(dataSet.getId());
			dsv.setDataSet(dataSet);
			dsv.setName(versionName);
			dsv.setDescription(description);
			dsv.setUploadDate(new Date());
			dsv.setStatus(Analysable.STATUS_UNVERIFIED);
			dsv.setOriginalFileName(originalFileName);
			dsv.setOwner(user);
			dsv.setExperiment(user.getCurrentExperiment());
			if( uploads != null && uploads.size() > 0 )
			{
				List<PalsFile> fileList = new ArrayList<PalsFile>();
			    for( int i=0; i < uploads.size(); ++i )
			    {
			    	PalsFile palsFile = fileService.createFile(uploads.get(i));
			    	palsFile.setName(uploadFileNames.get(i));
			    	palsFile.setContentType(uploadContentTypes.get(i));
			    	fileList.add(palsFile);
			    	fileService.save(palsFile);
			    }
			    dsv.setFiles(fileList);
			}
			// must write to database first so we can get a DataSetVersion id,
			// used
			// for creating file paths etc below
			log.debug("About to persist new data set version: " + message);
			entityManager.persist(dsv);
			log.debug("Persisted data set version: " + message);
			log.debug("Uploaded file path: "
					+ uploadedCSVFile.getAbsolutePath());

            checkFilename(originalFileName);
            dsv.setUploadedFile(uploadedCSVFile);
            validateUploadedFile(dsv);
            
            // if we are on the first day of the year, go back a day
            if( dsv.getEndDate() != null )
            {
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(dsv.getEndDate());
                if( endDate.get(Calendar.DAY_OF_YEAR) == 1 )
                {
                	endDate.add(Calendar.DAY_OF_YEAR,-1);
                	dsv.setEndDate(endDate.getTime());
                }
            }
        
            storeUploadedFile = new File(dsv.uploadedFilePath());
            log.debug("About to backup file to: " + storeUploadedFile.getAbsolutePath());
            FileUtils.copyFile(uploadedCSVFile, storeUploadedFile);
			
	        convertDataSetCSV2NCDF(uploadedCSVFile,
			    PalsFileUtils.getDataSetVersionFluxFile(dsv),
			    PalsFileUtils.getDataSetVersionMetFile(dsv),dsv);
        }
        catch( CSV2NCDFConversionException e )
        {
        	storeUploadedFile.deleteOnExit();
        	throw e;
        }

		return dsv;
	}
	
	public void validateUploadedFile(File file) throws 
    UploadedFileFormatException, IOException
    {
		DataSetVersion dsv = new DataSetVersion();
		dsv.setUploadedFile(file);
		validateUploadedFile(dsv);
    }
	
	public void validateUploadedFile(DataSetVersion dsv) throws 
	    UploadedFileFormatException, IOException
	{
		File uploadedFile = dsv.getUploadedFile();
		
		log.debug("Validating file: " + uploadedFile.getAbsolutePath());
		
		BufferedReader in = new BufferedReader(new FileReader(uploadedFile));
		String line = null;
		int i = 0;
		String templateVersion = "";
		while( (line = in.readLine()) != null )
		{
			++i;
			if( i == 1 )
			{
				if( !line.contains(Configuration.getInstance().TEMPLATE_VERSION) &&
				    !line.contains(Configuration.getInstance().getStringProperty("TEMPLATE_VERSION_2")))
				{
				    throw new UploadedFileFormatException("Uploaded template version is not supported");
				}
				else
				{
					if( line.contains(Configuration.getInstance().TEMPLATE_VERSION) )
					    templateVersion = Configuration.getInstance().TEMPLATE_VERSION.replace('.','_');
					else
						templateVersion = Configuration.getInstance().getStringProperty("TEMPLATE_VERSION_2").replace('.','_');
				}
			}
			else if( i == 2 && templateVersion != null )
			{
				if( !line.trim().equals(Configuration.getInstance().getStringProperty("T"+templateVersion+"_1").trim()) )
				{
					log.error("Original: " + line.trim());
					log.error("Template: " + Configuration.getInstance().getStringProperty("T"+templateVersion+"_1"));
			        throw new UploadedFileFormatException("Second header line is incorrect");	
				}
			}
			else if( i == 3 && templateVersion != null)
			{
				if( !line.trim().equals(Configuration.getInstance().getStringProperty("T"+templateVersion+"_2").trim()) )
				{
					log.error("Original: " + line.trim());
					log.error("Template: " + Configuration.getInstance().getStringProperty("T"+templateVersion+"_2"));
					throw new UploadedFileFormatException("Third header line is incorrect");
				}
			}
			else
			{
				if( line.length() <= 0 )
				{
					log.debug("Empty line at index: " + i );
				}
				else
				{
				    String[] splitLine = line.split(Globals.SEPARATOR);
				    if( splitLine.length != 
				    	Configuration.getInstance().getIntProperty("TEMPLATE_NUM_ROWS_"+templateVersion) )
				    {
				    	throw new UploadedFileFormatException("Line " + i + " in file has " + 
				    			splitLine.length + " rows, " + 
				    			Configuration.getInstance().getIntProperty("TEMPLATE_NUM_ROWS_"+templateVersion) + " are required");
				    }
				    else
				    {
				    	SimpleDateFormat sdf = new SimpleDateFormat(
				    	    Configuration.getInstance().TEMPLATE_DATE_FORMAT);
				    	String dateString = splitLine[Configuration.getInstance().TEMPLATE_DATE_COLUMN];
				    	try 
				    	{
							Date date = sdf.parse(dateString);
							Date startDate = dsv.getStartDate();
							Date endDate = dsv.getEndDate();
							if( startDate == null )
							{
								dsv.setStartDate(date);
							}
							else if( date.before(startDate) )
							{
								dsv.setStartDate(date);
							}
							if( endDate == null )
							{
								dsv.setEndDate(date);
							}
							else if( date.after(endDate) )
							{
								dsv.setEndDate(date);
							}
						} 
				    	catch (ParseException e) 
						{
							throw new UploadedFileFormatException("Invalid date in line " + i +
							": " + dateString + " required format: " + Configuration.getInstance().TEMPLATE_DATE_FORMAT);
						}
				    	// all other columns are numbers
				    	for( int j=0; j < splitLine.length; ++j )
				    	{
				    	    if( j != Configuration.getInstance().TEMPLATE_DATE_COLUMN )
				    	    {
				    	    	try
				    	    	{
				    	            Double.valueOf(splitLine[j]);
				    	    	}
				    	    	catch( NumberFormatException e )
				    	    	{
				    	    		throw new UploadedFileFormatException("Value is not a number: " +
				    	    		    splitLine[j] + " line: " + i + " column: " + j);
				    	    	}
				    	    }
				    	}
				    }
				}
			}
		}
	}
	
	public void checkFilename(String originalFilename) throws UploadedFileFormatException
	{
		if( originalFilename == null || originalFilename.length() <=0 )
		{
			throw new UploadedFileFormatException("Original filename invalid");
		}
		else
		{
		    String[] splitString = originalFilename.split("\\.");
		    if(splitString.length <= 1 )
		    {
		    	throw new UploadedFileFormatException("Uploaded file must have a csv extension");
		    }
		    else if( !splitString[splitString.length-1].equals(Globals.ALLOWED_EXTENSION) )
		    {
		    	throw new UploadedFileFormatException("Uploaded file must have a csv extension");
		    }
		}		
	}

	public void convertDataSetCSV2NCDF(File csvFile, File fluxOutput,
			File metOutput, DataSetVersion dsv) throws CSV2NCDFConversionException {
		String conversionScriptExecPath = Configuration.getInstance().CONVERT_NETFLUX_TO_NC_CMD;
		String exec = conversionScriptExecPath + " "
				+ csvFile.getAbsolutePath() + " " + metOutput.getAbsolutePath()
				+ " " + fluxOutput.getAbsolutePath()
				+ " " + dsv.getMetadata()
				;
		try {
			log.debug("About to execute command to convert to netcdf");
			PalsFileUtils.executeCommand(exec);
			log.debug("Finished converting to netcdf");
		} catch (InterruptedException e) {
			throw new CSV2NCDFConversionException(e.getMessage());
		} catch (IOException e) {
			throw new CSV2NCDFConversionException(e.getMessage());
		} catch (AnalysisException e) {
			throw new CSV2NCDFConversionException(e.getMessage());
		}

	}

	// other...

	public long getPublicModelOutputCount(User user) {
		Query query = entityManager
				.createQuery(
						"select count(*) from ModelOutput where userName = :username and isPublic=TRUE")
				.setParameter("username", user.getUsername());
		return ((Long) query.getSingleResult()).longValue();
	}

//	public void deleteModelOutput(User user, ModelOutput modelOutput)
//			throws SecurityException {
//		checkUserCanAccessModelOutput(user, modelOutput);
//		modelOutput.setVisible(false);
//		entityManager.merge(modelOutput);
//	}

	public void setModelOutputPublic(User user, ModelOutput modelOutput,
			String accessLevel) throws SecurityException {
		checkUserOwnsModelOutput(user, modelOutput);
		modelOutput.setAccessLevel(accessLevel);
		entityManager.merge(modelOutput);
		Experiment e = modelOutput.getExperiment();
		if( e != null )
		{
			e.updateActivity();
			entityManager.merge(e);
		}
	}

//	/***
//	 * Update attributes for a Model Output. If any of the attribute variables
//	 * passed through are null, they are not updated. If DataSet changes we need
//	 * to rerun the analyses.
//	 * 
//	 * @throws SecurityException
//	 */
//	public void updateModelOutput(User user, ModelOutput modelOutput,
//			String modelOutputName, Integer modelId, Integer dataSetId,
//			String stateSelection, String parameterSelection,
//			String userComments) throws SecurityException {
//		checkUserCanAccessModelOutput(user, modelOutput);
//		boolean isModified = false;
//		if (modelOutputName != null
//				&& !modelOutputName.equals(modelOutput.getName())) {
//			modelOutput.setName(modelOutputName);
//			isModified = true;
//		}
//		if (dataSetId != null
//				&& dataSetId.intValue() != modelOutput.getDataSetVersionId()
//						.intValue()) {
//			modelOutput.setDataSetVersionId(dataSetId);
//			isModified = true;
//			// dataset changed. We must rerun analyses on this modeloutput.
//			rerunAnalyses(modelOutput);
//		}
//		if (modelId != null
//				&& modelId.intValue() != modelOutput.getModelId().intValue()) {
//			modelOutput.setModelId(modelId);
//			isModified = true;
//		}
//		if (stateSelection != null) {
//			modelOutput.setStateSelection(stateSelection);
//			isModified = true;
//		}
//		if (parameterSelection != null) {
//			modelOutput.setParameterSelection(parameterSelection);
//			isModified = true;
//		}
//		if (userComments != null) {
//			modelOutput.setUserComments(userComments);
//			isModified = true;
//		}
//		if (isModified) {
//			entityManager.merge(modelOutput);
//		}
//	}
//
//	private void rerunAnalyses(ModelOutput modelOutput) {
//		modelOutput.setStatus(ModelOutput.STATUS_RERUN);
//	}
	
	public boolean usernameExists(String username)
	{
		String queryString = "SELECT username from User where username = :username";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("username", username);
		String result = (String) query.getSingleResult();
		if( result == null ) return false;
		else return true;
	}
	
	public List<User> getAllUsers()
	{
		String queryString = "from User order by username";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList();
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	
	public void saveUser(User user)
	{
		entityManager.merge(user);
	}
	
}
