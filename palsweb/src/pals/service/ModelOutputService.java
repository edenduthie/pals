package pals.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pals.Configuration;
import pals.Globals;
import pals.dao.DAO;
import pals.entity.Analysable;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.entity.User;
import pals.utils.PalsFileUtils;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

@Transactional
public class ModelOutputService 
{
	DAO dao;
	AnalysisService analysisEntityService;
	FileService fileService;
	
	private static final Logger log = Logger.getLogger(ModelOutput.class);
	
	static final String DATE_FORMAT_OUT = "yyyy-MM-dd HH:mm";
	static final String DATE_FORMAT_FIRST_LAST = "MM/dd/yyyy";
	
	public static final String MODEL_OUTPUT_NAME = "modelOutputName";
	public static final String OWNER = "owner";
	public static final String MODEL = "model";
	public static final String DATA_SET = "dataSet";
	public static final String UPLOADED = "uploaded";
	public static final String STATUS = "status";
	public static final String ACCESS = "access";
	
	public String parseDate(String date)
	{
		if( date == null || date.trim().length() <= 0 ) return null;
		String[] splitLine = date.trim().split("\\,");
		if( splitLine.length == 3 )
		{
			String year = splitLine[0];
			String month = splitLine[1];
			if( month.length() <= 1 ) month = '0' + month;
			String day = splitLine[2];
			if( day.length() <= 1 ) day = '0' + day;
			String dateString = year + "-" + month + "-" + day + " 00:00";
			return dateString;
		}
		else return null;
	}
	
	@Transactional(rollbackFor = { IOException.class } )
	public ModelOutput newModelOutput(User user, File uploadedFile,
			String name, Integer modelId, DataSetVersion dataSetVersion,
			String stateSelection, String parameterSelection,
			String userComments, Boolean allowDownload) 
	    throws IOException, InvalidInputException 
	{
		return newModelOutput(user, uploadedFile,
				name, modelId, dataSetVersion,
				stateSelection, parameterSelection,
				userComments,
				null,null,null,allowDownload);
	}
	
	@Transactional(rollbackFor = { IOException.class } )
	public ModelOutput newModelOutput(User user, File uploadedFile,
			String name, Integer modelId, DataSetVersion dataSetVersion,
			String stateSelection, String parameterSelection,
			String userComments) 
	    throws IOException, InvalidInputException 
	{
		return newModelOutput(user, uploadedFile,
				name, modelId, dataSetVersion,
				stateSelection, parameterSelection,
				userComments,
				null,null,null,true);
	}
	
	@Transactional(rollbackFor = { IOException.class } )
	public ModelOutput newModelOutput(User user, File uploadedFile,
			String name, Integer modelId, DataSetVersion dataSetVersion,
			String stateSelection, String parameterSelection,
			String userComments,
			List<File> uploads, List<String> uploadFileNames, List<String> uploadContentTypes,
			Boolean allowDownload) 
	    throws IOException, InvalidInputException 
	{
	    checkNameNotTaken(name);
		
		log.info("Creating new model output...");
		ModelOutput modelOutput = new ModelOutput();
		modelOutput.setDataSetVersionId(dataSetVersion.getId());
		modelOutput.setDataSetVersion(dataSetVersion);
		modelOutput.setDate(new Date());
		modelOutput.setModelId(modelId);
		modelOutput.setUpoadDate(new Date());
		modelOutput.setUserName(user.getUsername());
		modelOutput.setOwner(user);
		modelOutput.setName(name);
		modelOutput.setStatus(Analysable.STATUS_NEW);
		//modelOutput.setVisible(true);
		modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);
		modelOutput.setStateSelection(stateSelection);
		modelOutput.setParameterSelection(parameterSelection);
		modelOutput.setUserComments(userComments);
		modelOutput.setStatus(ModelOutput.STATUS_PREPARED);
		modelOutput.setAllowDownload(allowDownload);
		
		Experiment e = user.getCurrentExperiment();
		if( e != null )
		{
			e.updateActivity();
			dao.update(e);
		}
		
		modelOutput.setExperiment(user.getCurrentExperiment());
		
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
		    modelOutput.setFiles(fileList);
		}
		
		dao.persist(modelOutput);
		
		String dirPath = PalsFileUtils.getModelOutputDirPath(modelOutput);
		File dir = new File(dirPath);
		dir.mkdirs();
		dir.setWritable(true);
		String newFilePath = modelOutput.retrieveOutputFilePath();
		FileUtils.copyFile(new File(uploadedFile.getAbsolutePath()), new File(newFilePath));
		
		return modelOutput;
	}
	
	@Transactional(rollbackFor = { SecurityException.class } )
	public void deleteModelOutput(User user, ModelOutput modelOutput)
	throws SecurityException {
        checkUserCanEditModelOutput(user, modelOutput);
        deleteModelOutput(modelOutput);
    }
	
	public void deleteModelOutput(ModelOutput modelOutput)
	{
        dao.delete(ModelOutput.class.getName(), "id", modelOutput.getId());
        File file = new File(modelOutput.retrieveOutputFilePath());
        file.delete();
        analysisEntityService.deleteAllAnalysisForAnalysable(modelOutput);
	}
	
	private void checkUserCanEditModelOutput(User user,
			ModelOutput modelOutput) throws SecurityException {
		if (!user.getUsername().equals(modelOutput.getUserName())) {
			throw new SecurityException("User " + user.getUsername()
					+ " cannot delete modeloutput "
					+ modelOutput.getId() + " as they don't own it.");
		}
	}

	public ModelOutput getModelOutput(User user, Integer modelOutputId)
			throws SecurityException {
		Query query = dao.getEntityManager().createQuery(
				"from ModelOutput where id = :modelOutputId").setParameter(
				"modelOutputId", modelOutputId);
		ModelOutput modelOutput = (ModelOutput) query.getSingleResult();
		checkUserCanViewModelOutput(user, modelOutput);
		return modelOutput;
	}
	
	private void checkUserCanViewModelOutput(User user, ModelOutput modelOutput) throws SecurityException
	{
		if( modelOutput.getAccessLevel().equals(ModelOutput.ACCESS_LEVEL_PRIVATE) )
		{
			if( ! modelOutput.getOwner().equals(user) )
			{
				throw new SecurityException("User " + user.getUsername()
						+ " cannot view modeloutput "
						+ modelOutput.getId() + " as they don't own it.");
			}
		}
		else if( modelOutput.getAccessLevel().equals(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER) )
		{
			if( modelOutput.getDataSetVersion().getOwner().equals(user) ||
				modelOutput.getOwner().equals(user))
			{
			}
			else
			{
				throw new SecurityException("User " + user.getUsername()
						+ " cannot view modeloutput "
						+ modelOutput.getId() + " as they don't own the data set.");
			}
		}
	}
	
	public void checkNameNotTaken(String name) throws InvalidInputException
	{
		String queryString = "from ModelOutput where name=:newName";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("newName", name);
		List<ModelOutput> results = query.getResultList();
		if( results.size() > 0 )
		{
			throw new InvalidInputException("That name has already been taken");
		}
	}
	
	public void updateModelOutput(User user, ModelOutput modelOutput,
			String modelOutputName, Integer modelId, Integer dataSetId,
			String stateSelection, String parameterSelection,
			String userComments,
			List<File> uploads, List<String> uploadFileNames, List<String> uploadContentTypes
			) throws SecurityException, InvalidInputException, IOException 
			{
		updateModelOutput(user, modelOutput, modelOutputName, modelId, dataSetId,
				stateSelection, parameterSelection, userComments,
				uploads, uploadFileNames, uploadContentTypes,
				true);
	}
	
	/***
	 * Update attributes for a Model Output. If any of the attribute variables
	 * passed through are null, they are not updated. If DataSet changes we need
	 * to rerun the analyses.
	 * 
	 * @throws SecurityException
	 * @throws InvalidInputException 
	 * @throws IOException 
	 */
	public void updateModelOutput(User user, ModelOutput modelOutput,
			String modelOutputName, Integer modelId, Integer dataSetId,
			String stateSelection, String parameterSelection,
			String userComments,
			List<File> uploads, List<String> uploadFileNames, List<String> uploadContentTypes,
			Boolean allowDownload) throws SecurityException, InvalidInputException, IOException {
		checkUserCanEditModelOutput(user, modelOutput);
		modelOutput = get(modelOutput.getId());
		if( modelOutputName != null && !modelOutputName.equals(modelOutput.getName()) )
		{
		    checkNameNotTaken(modelOutputName);
		}
		boolean isModified = false;
		if (modelOutputName != null
				&& !modelOutputName.equals(modelOutput.getName())) {
			modelOutput.setName(modelOutputName);
			isModified = true;
		}
		if (dataSetId != null
				&& dataSetId.intValue() != modelOutput.getDataSetVersionId()
						.intValue()) {
			modelOutput.setDataSetVersionId(dataSetId);
			DataSetVersion dataSetVersion = (DataSetVersion)
			    dao.get(DataSetVersion.class.getName(), "id",dataSetId);
			modelOutput.setDataSetVersion(dataSetVersion);
			isModified = true;
			// dataset changed. We must rerun analyses on this modeloutput.
			rerunAnalyses(modelOutput);
		}
		if (modelId != null
				&& modelId.intValue() != modelOutput.getModelId().intValue()) {
			modelOutput.setModelId(modelId);
			isModified = true;
		}
		if (stateSelection != null) {
			modelOutput.setStateSelection(stateSelection);
			isModified = true;
		}
		if (parameterSelection != null) {
			modelOutput.setParameterSelection(parameterSelection);
			isModified = true;
		}
		if (userComments != null) {
			modelOutput.setUserComments(userComments);
			isModified = true;
		}
		
		if( uploads != null && uploads.size() > 0 )
		{
			List<PalsFile> fileList = modelOutput.getFiles();
		    for( int i=0; i < uploads.size(); ++i )
		    {
		    	PalsFile palsFile = fileService.createFile(uploads.get(i));
		    	palsFile.setName(uploadFileNames.get(i));
		    	palsFile.setContentType(uploadContentTypes.get(i));
		    	fileList.add(palsFile);
		    	fileService.save(palsFile);
		    	isModified = true;
		    }
		    modelOutput.setFiles(fileList);
		}
		
		if( allowDownload != null )
		{
			modelOutput.setAllowDownload(allowDownload);
			isModified = true;
		}
		
		if (isModified) {
			dao.update(modelOutput);
			Experiment e = modelOutput.getExperiment();
			if( e != null )
			{
				e.updateActivity();
				dao.update(e);
			}
		}
	}
	
	public Integer getNumFiles(Integer id)
	{
		ModelOutput mo = get(id);
		return mo.getFiles().size();
	}
	
	public ModelOutput get(Integer id)
	{
		return (ModelOutput) dao.get(ModelOutput.class.getName(),"id",id);
	}
	
	private void rerunAnalyses(ModelOutput modelOutput) {
		analysisEntityService.deleteAllAnalysisForAnalysable(modelOutput);
		modelOutput.setStatus(ModelOutput.STATUS_PREPARED);
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public AnalysisService getAnalysisEntityService() {
		return analysisEntityService;
	}

	public void setAnalysisEntityService(AnalysisService analysisEntityService) {
		this.analysisEntityService = analysisEntityService;
	}
	
	/**
	 * Returns all the model outputs that the given user is an owner of and have access level
	 * DATA_SET_PROVIDER
	 * @return - the list of ModelOutputs
	 */
	public List<ModelOutput> getDataSetOwnerModelOutputs(User user)
	{
		String queryString = "from ModelOutput where (accessLevel=:ownerAccess " +
		    " or accessLevel=:publicAccess)" +
		    " and dataSetVersion.owner.username=:username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL ";
		}
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		query.setParameter("ownerAccess", ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		query.setParameter("publicAccess", ModelOutput.ACCESS_LEVEL_PUBLIC);
		query.setParameter("username", user.getUsername());
		return query.getResultList();
	}
	
	public List<ModelOutput> getDataSetOwnerModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access)
	{
		int limit = Configuration.getInstance().getIntProperty("MODEL_OUTPUTS_PER_PAGE");
		return getDataSetOwnerModelOutputs(user, modelId, username, dsvId, access, limit, 0);
	}
	
	public List<ModelOutput> getDataSetOwnerModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access,
		    Integer limit, Integer offset)
    {
		return getDataSetOwnerModelOutputs(user, modelId, username, dsvId, access, limit, offset, null, null, null, null, null, null, null, null, null);
	}
	
	public List<ModelOutput> getDataSetOwnerModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access,
	    Integer limit, Integer offset, String modelOutputNameSort, String ownerSort, String modelSort, String dataSetSort,
	    String uploadedSort, String statusSort, String accessSort, String firstPref, String secondPref)
	{
		String queryString = "from ModelOutput mo where (accessLevel=:ownerAccess " +
		    " or accessLevel=:publicAccess)" +
		    " and dataSetVersion.owner.username=:username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL ";
		}
		if( modelId != null )
		{
			queryString += " and mo.modelId=:modelId ";
		}
		if( username != null )
		{
			queryString += " and mo.owner.username=:username";
		}
		if( dsvId != null )
		{
			 queryString += " and mo.dataSetVersionId=:dsvId";
		}
		if( access != null )
		{
			queryString += " and mo.accessLevel=:access";
		}
		
		queryString = addSort(queryString,modelOutputNameSort,ownerSort,modelSort,
		        dataSetSort,uploadedSort,statusSort,accessSort,firstPref,secondPref);
		
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		if( modelId != null )
		{
			query.setParameter("modelId", modelId);
		}
		if( username != null )
		{
			query.setParameter("username",username);
		}
		if( dsvId != null )
		{
			 query.setParameter("dsvId", dsvId);
		}
		if( access != null )
		{
			query.setParameter("access",access);
		}
		query.setParameter("ownerAccess", ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		query.setParameter("publicAccess", ModelOutput.ACCESS_LEVEL_PUBLIC);
		query.setParameter("username", user.getUsername());
		return query.getResultList();
	}
	
	public Long getDataSetOwnerModelOutputsCount(User user, Integer modelId, String username, Integer dsvId, String access)
		{
			String queryString = "select count(mo.id) from ModelOutput mo where (accessLevel=:ownerAccess " +
			    " or accessLevel=:publicAccess)" +
			    " and dataSetVersion.owner.username=:username";
			if( user.getCurrentExperiment() != null )
			{
				queryString += " and experiment.id=:eid";
			}
			else
			{
				queryString += " and experiment = NULL ";
			}
			if( modelId != null )
			{
				queryString += " and mo.modelId=:modelId ";
			}
			if( username != null )
			{
				queryString += " and mo.owner.username=:username";
			}
			if( dsvId != null )
			{
				 queryString += " and mo.dataSetVersionId=:dsvId";
			}
			if( access != null )
			{
				queryString += " and mo.accessLevel=:access";
			}
			Query query = dao.getEntityManager().createQuery(queryString);
			if( user.getCurrentExperiment() != null )
			{
				query.setParameter("eid", user.getCurrentExperiment().getId());
			}
			if( modelId != null )
			{
				query.setParameter("modelId", modelId);
			}
			if( username != null )
			{
				query.setParameter("username",username);
			}
			if( dsvId != null )
			{
				 query.setParameter("dsvId", dsvId);
			}
			if( access != null )
			{
				query.setParameter("access",access);
			}
			query.setParameter("ownerAccess", ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
			query.setParameter("publicAccess", ModelOutput.ACCESS_LEVEL_PUBLIC);
			query.setParameter("username", user.getUsername());
			return (Long) query.getSingleResult();
		}
	
	/**
	 * Returns all model outputs that have the given model and dataSet and do not have
	 * accessLevel private and are owned by the given user
	 */
	public List<ModelOutput> getModelOutputsForModelDataSet(Model model, DataSet dataSet,
	    User user)
	{
		String queryString = "from ModelOutput where modelId=:modelId" +
		    " and dataSetVersion.dataSet.id=:dataSetId" +
		    " and accessLevel!=:privateAccessLevel" +
		    " and owner.username=:username";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("modelId",model.getId());
		query.setParameter("dataSetId",dataSet.getId());
		query.setParameter("username",user.getUsername());
	    query.setParameter("privateAccessLevel", ModelOutput.ACCESS_LEVEL_PRIVATE);
		return query.getResultList();
	}
	
	public List<ModelOutput> getModelOutputs(User user) {
		String queryString = "from ModelOutput mo"
		    + " where mo.userName = :username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and mo.experiment.id=:eid";
		}
		else
		{
			queryString += " and mo.experiment = NULL ";
		}
		queryString += " order by mo.id DESC";
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		query.setParameter("username", user.getUsername());
		return query.getResultList();
	}
	
	public List<ModelOutput> getModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access) 
	{
		int limit = Configuration.getInstance().getIntProperty("MODEL_OUTPUTS_PER_PAGE");
		return getModelOutputs(user, modelId, username, dsvId, access, limit, 0);
	}
	
	public List<ModelOutput> getModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access,
	        Integer limit, Integer offset)
    {
		return getModelOutputs(user, modelId, username, dsvId, access, limit, offset, null, null, null, null, null, null, null, null, null);
	}
	
	public List<ModelOutput> getModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access,
        Integer limit, Integer offset, String modelOutputNameSort, String ownerSort, String modelSort, String dataSetSort,
        String uploadedSort, String statusSort, String accessSort, String firstPref, String secondPref) 
    {
		String queryString = "from ModelOutput mo"
		    + " where mo.userName = :username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and mo.experiment.id=:eid";
		}
		else
		{
			queryString += " and mo.experiment = NULL ";
		}
		if( modelId != null )
		{
			queryString += " and mo.modelId=:modelId ";
		}
		if( username != null )
		{
			queryString += " and mo.owner.username=:username";
		}
		if( dsvId != null )
		{
			 queryString += " and mo.dataSetVersionId=:dsvId";
		}
		if( access != null )
		{
			queryString += " and mo.accessLevel=:access";
		}
		
		queryString = addSort(queryString,modelOutputNameSort,ownerSort,modelSort,
		        dataSetSort,uploadedSort,statusSort,accessSort,firstPref,secondPref);
		
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		if( modelId != null )
		{
			query.setParameter("modelId", modelId);
		}
		if( username != null )
		{
			query.setParameter("username",username);
		}
		if( dsvId != null )
		{
			 query.setParameter("dsvId", dsvId);
		}
		if( access != null )
		{
			query.setParameter("access", access);
		}
		query.setParameter("username", user.getUsername());
		return query.getResultList();
	}
	
	public Long getModelOutputsCount(User user, Integer modelId, String username, Integer dsvId, String access) 
	{
			String queryString = "select count(id) from ModelOutput mo"
			    + " where mo.userName = :username";
			if( user.getCurrentExperiment() != null )
			{
				queryString += " and mo.experiment.id=:eid";
			}
			else
			{
				queryString += " and mo.experiment = NULL ";
			}
			if( modelId != null )
			{
				queryString += " and mo.modelId=:modelId ";
			}
			if( username != null )
			{
				queryString += " and mo.owner.username=:username";
			}
			if( dsvId != null )
			{
				 queryString += " and mo.dataSetVersionId=:dsvId";
			}
			if( access != null )
			{
				queryString += " and mo.accessLevel=:access";
			}
			Query query = dao.getEntityManager().createQuery(queryString);
			if( user.getCurrentExperiment() != null )
			{
				query.setParameter("eid", user.getCurrentExperiment().getId());
			}
			if( modelId != null )
			{
				query.setParameter("modelId", modelId);
			}
			if( username != null )
			{
				query.setParameter("username",username);
			}
			if( dsvId != null )
			{
				 query.setParameter("dsvId", dsvId);
			}
			if( access != null )
			{
				query.setParameter("access", access);
			}
			query.setParameter("username", user.getUsername());
			return (Long) query.getSingleResult();
		}
	
	public List<ModelOutput> getPublicModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access)
	{
		int limit = Configuration.getInstance().getIntProperty("MODEL_OUTPUTS_PER_PAGE");
		return getPublicModelOutputs(user, modelId, username, dsvId, access, limit, 0, null, null, null, null, null, null, null, null, null);
	}
	
	public List<ModelOutput> getPublicModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access,
		    Integer limit, Integer offset)
    {
		return getPublicModelOutputs(user, modelId, username, dsvId, access, limit, offset, null, null, null, null, null, null, null, null, null);
	}
	
	public List<ModelOutput> getPublicModelOutputs(User user, Integer modelId, String username, Integer dsvId, String access,
	    Integer limit, Integer offset, String modelOutputNameSort, String ownerSort, String modelSort,
	    String dataSetSort, String uploadedSort, String statusSort, String accessSort, String firstPref, String secondPref) 
	{
		String queryString = "from ModelOutput mo where accessLevel=:accessLevel";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL";
		}
		if( modelId != null )
		{
			queryString += " and mo.modelId=:modelId ";
		}
		if( username != null )
		{
			queryString += " and mo.owner.username=:username";
		}
		if( dsvId != null )
		{
			 queryString += " and mo.dataSetVersionId=:dsvId";
		}
		if( access != null )
		{
			queryString += " and mo.accessLevel=:access";
		}
		queryString = addSort(queryString,modelOutputNameSort,ownerSort,modelSort,
	        dataSetSort,uploadedSort,statusSort,accessSort,firstPref,secondPref);
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		if( modelId != null )
		{
			query.setParameter("modelId", modelId);
		}
		if( username != null )
		{
			query.setParameter("username",username);
		}
		if( dsvId != null )
		{
			 query.setParameter("dsvId", dsvId);
		}
		if( access != null )
		{
			query.setParameter("access",access);
		}
		query.setParameter("accessLevel", ModelOutput.ACCESS_LEVEL_PUBLIC);
		List<ModelOutput> results = query.getResultList();
		
		if( user.getCurrentExperiment() == null )
		{
			queryString = "from ModelOutput mo where accessLevel=:accessLevel";
			queryString += " and experiment.shareWithAll = true";
			queryString = addSort(queryString,modelOutputNameSort,ownerSort,modelSort,
			        dataSetSort,uploadedSort,statusSort,accessSort,firstPref,secondPref);
			query = dao.getEntityManager().createQuery(queryString);
			query.setParameter("accessLevel", ModelOutput.ACCESS_LEVEL_PUBLIC);
			List<ModelOutput> shareWithAllResults = query.getResultList();
			for( ModelOutput mo : shareWithAllResults )
			{
				results.add(mo);
			}
		}
		
		return results;
	}
	
	public String addSort(String queryString, String modelOutputNameSort, String ownerSort, String modelSort, 
		String dataSetSort, String uploadedSort, String statusSort, String accessSort, String firstPref, String secondPref)
	{
		String sortString = "";
		
		List<String> sortFields = new ArrayList<String>();
		if( firstPref != null && firstPref.length() > 0 ) sortFields.add(firstPref);
		if( secondPref != null && secondPref.length() > 0 ) sortFields.add(secondPref);
		if( modelOutputNameSort != null && modelOutputNameSort.length() > 0 && !sortFields.contains(MODEL_OUTPUT_NAME))
		{
			sortFields.add(MODEL_OUTPUT_NAME);
		}
		if( ownerSort != null && ownerSort.length() > 0 && !sortFields.contains(OWNER))
		{
			sortFields.add(OWNER);
		}
		if( modelSort != null && modelSort.length() > 0 && !modelSort.contains(MODEL))
		{
			sortFields.add(MODEL);
		}
		if( dataSetSort != null && dataSetSort.length() > 0 && !dataSetSort.contains(DATA_SET))
		{
			sortFields.add(DATA_SET);
		}
		if( uploadedSort != null && uploadedSort.length() > 0 && !uploadedSort.contains(UPLOADED))
		{
			sortFields.add(UPLOADED);
		}
		if( statusSort != null && statusSort.length() > 0 && !statusSort.contains(UPLOADED))
		{
			sortFields.add(STATUS);
		}
		if( accessSort != null && accessSort.length() > 0 && !accessSort.contains(ACCESS))
		{
			sortFields.add(ACCESS);
		}
		
		for( String fieldName : sortFields )
		{
			sortString = addSortItem(sortString,fieldName,modelOutputNameSort,ownerSort,modelSort, 
					dataSetSort,uploadedSort,statusSort,accessSort);
		}
		
		
		if( sortString.length() > 0 ) sortString = " order by " + sortString.substring(0,sortString.length()-1);
		
		
		return queryString + sortString;
	}
	
	public String addSortItem(String sortString, String fieldName, String modelOutputNameSort, String ownerSort, String modelSort, 
			String dataSetSort, String uploadedSort, String statusSort, String accessSort)
	{
		if( fieldName.equals(MODEL_OUTPUT_NAME) )
		{
			if( !modelOutputNameSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
			    sortString  += " mo.name " + modelOutputNameSort + ",";
			}
		}
		else if( fieldName.equals(OWNER) )
		{
			if( !ownerSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
			    sortString  += " mo.owner.fullName " + ownerSort + ",";
			}
		}
		else if( fieldName.equals(MODEL) )
		{
			if( !modelSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
				sortString  += " mo.model.modelName " + modelSort;
				sortString += ", mo.model.version " + modelSort + ",";
			}
		}
		else if( fieldName.equals(DATA_SET) )
		{
			if( !dataSetSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
				sortString  += " mo.dataSetVersion.dataSet.name " + dataSetSort;
				sortString += ", mo.dataSetVersion.name " + dataSetSort + ",";
			}
		}
		else if( fieldName.equals(UPLOADED) )
		{
			if( !uploadedSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
				sortString  += " mo.uploadDate " + uploadedSort + ",";
			}
		}
		else if( fieldName.equals(STATUS) )
		{
			if( !statusSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
				sortString  += " mo.status " + statusSort + ",";
			}
		}
		else if( fieldName.equals(ACCESS) )
		{
			if( !accessSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
				sortString  += " mo.accessLevel " + accessSort + ",";
			}
		}
		return sortString;
	}
	
	public Long getPublicModelOutputsCount(User user, Integer modelId, String username, Integer dsvId, String access) {
			String queryString = "select count(mo.id) from ModelOutput mo where accessLevel=:accessLevel";
			if( user.getCurrentExperiment() != null )
			{
				queryString += " and experiment.id=:eid";
			}
			else
			{
				queryString += " and experiment = NULL";
			}
			if( modelId != null )
			{
				queryString += " and mo.modelId=:modelId ";
			}
			if( username != null )
			{
				queryString += " and mo.owner.username=:username";
			}
			if( dsvId != null )
			{
				 queryString += " and mo.dataSetVersionId=:dsvId";
			}
			if( access != null )
			{
				queryString += " and mo.accessLevel=:access";
			}
			Query query = dao.getEntityManager().createQuery(queryString);
			if( user.getCurrentExperiment() != null )
			{
				query.setParameter("eid", user.getCurrentExperiment().getId());
			}
			if( modelId != null )
			{
				query.setParameter("modelId", modelId);
			}
			if( username != null )
			{
				query.setParameter("username",username);
			}
			if( dsvId != null )
			{
				 query.setParameter("dsvId", dsvId);
			}
			if( access != null )
			{
				query.setParameter("access",access);
			}
			query.setParameter("accessLevel", ModelOutput.ACCESS_LEVEL_PUBLIC);
			Long count = (Long) query.getSingleResult();
			
			if( user.getCurrentExperiment() == null )
			{
				queryString = "select count(id) from ModelOutput where accessLevel=:accessLevel";
				queryString += " and experiment.shareWithAll = true";
				query = dao.getEntityManager().createQuery(queryString);
				query.setParameter("accessLevel", ModelOutput.ACCESS_LEVEL_PUBLIC);
				count += (Long) query.getSingleResult();
			}
			
			return count;
		}
	
	public List<ModelOutput> getPublicModelOutputs(User user) {
		String queryString = "from ModelOutput where accessLevel=:accessLevel";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL";
		}
		queryString += " order by id desc";
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		query.setParameter("accessLevel", ModelOutput.ACCESS_LEVEL_PUBLIC);
		List<ModelOutput> results = query.getResultList();
		
		if( user.getCurrentExperiment() == null )
		{
			queryString = "from ModelOutput where accessLevel=:accessLevel";
			queryString += " and experiment.shareWithAll = true";
			queryString += " order by id desc";
			query = dao.getEntityManager().createQuery(queryString);
			query.setParameter("accessLevel", ModelOutput.ACCESS_LEVEL_PUBLIC);
			List<ModelOutput> shareWithAllResults = query.getResultList();
			for( ModelOutput mo : shareWithAllResults )
			{
				results.add(mo);
			}
		}
		
		return results;
	}
	
	public void removeAllFiles()
	{
		List<ModelOutput> list = dao.getAll(ModelOutput.class.getName());
		for( ModelOutput mo : list )
		{
			mo.setFiles(new ArrayList<PalsFile>());
			dao.update(mo);
		}
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	
	public List<Model> getUniqueModels(List<ModelOutput> modelOutputs)
	{
		List<Model> models = new ArrayList<Model>();
		for( ModelOutput mo : modelOutputs )
		{
			boolean have = false;
			for( Model model : models )
			{
				if( model.getId().equals(mo.getModel().getId()) ) 
				{
					have = true;
				}
			}
			if( !have ) models.add(mo.getModel());
		}
		return models;
	}
	
	public List<User> getUniqueUsers(List<ModelOutput> modelOutputs)
	{
		List<User> users = new ArrayList<User>();
		for( ModelOutput mo : modelOutputs )
		{
			boolean have = false;
			for( User user : users )
			{
				if( user.getUsername().equals(mo.getOwner().getUsername()) ) 
				{
					have = true;
					break;
				}
			}
			if( !have ) users.add(mo.getOwner());
		}
		return users;
	}
	
	public List<DataSetVersion> getUniqueDataSetVersions(List<ModelOutput> modelOutputs)
	{
		List<DataSetVersion> dsvs = new ArrayList<DataSetVersion>();
		for( ModelOutput mo : modelOutputs )
		{
			boolean have = false;
			for( DataSetVersion dsv : dsvs )
			{
				if( dsv.getId() == mo.getDataSetVersion().getId() ) 
				{
					have = true;
					break;
				}
			}
			if( !have ) dsvs.add(mo.getDataSetVersion());
		}
		return dsvs;
	}
	
	public List<String> getUniqueAccess(List<ModelOutput> modelOutputs)
	{
		List<String> accessVersions = new ArrayList<String>();
		for( ModelOutput mo : modelOutputs )
		{
			boolean have = false;
			for( String access : accessVersions )
			{
				if( mo.getAccessLevel().equals(access) ) 
				{
					have = true;
					break;
				}
			}
			if( !have ) accessVersions.add(mo.getAccessLevel());
		}
		return accessVersions;
	}
	
	public void removeFile(ModelOutput mo, PalsFile file)
	{
		ModelOutput result = (ModelOutput) dao.get(ModelOutput.class.getName(), "id", mo.getId());
		result.getFiles().remove(file);
		dao.update(result);
		PalsFile nonDetached = fileService.getNoData(file.getId());
		dao.remove(nonDetached);
	}
	
	public List<String> getCSV(Integer id, String compareDate, Integer limit) throws IOException, SecurityException
	{
		compareDate = parseDate(compareDate);
		
		List<String> csv = new ArrayList<String>();
		ModelOutput mo = (ModelOutput) dao.get(ModelOutput.class.getName(), "id", id);
		if( !mo.getAccessLevel().equals(ModelOutput.ACCESS_LEVEL_PUBLIC) )
			throw new SecurityException("Model Output is not public");
		String filePath = mo.retrieveOutputFilePath();
		SimpleDateFormat sdfOut = new SimpleDateFormat(DATE_FORMAT_OUT);
		NetcdfFile ncFile = null;
	    int offset = 0;
		
		try 
		{
			ncFile = NetcdfFile.open(filePath);
			
			//List<Calendar> dates = new ArrayList<Calendar>();
			
			Variable v = ncFile.findVariable("time");
			if( v != null )
			{
				Array data = v.read();
				double[] timeDouble = (double []) data.get1DJavaArray( double.class);
				int i = 0;
				int added = 0;
				for( double value : timeDouble )
				{
					Calendar referenceDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					referenceDate.set(Calendar.YEAR,2002);
					referenceDate.set(Calendar.DAY_OF_MONTH,1);
					referenceDate.set(Calendar.MONTH,0);
					referenceDate.set(Calendar.HOUR,0);
					referenceDate.set(Calendar.MINUTE,1);
					referenceDate.set(Calendar.SECOND,0);
					referenceDate.set(Calendar.MILLISECOND,0);
					referenceDate.add(Calendar.SECOND,new Double(value).intValue());
					String outDate = sdfOut.format(referenceDate.getTime());
					
					if( (compareDate == null || outDate.compareTo(compareDate) >= 0) && added < limit)
					{
						if( added == 0 ) offset = i;
						csv.add(outDate);	
						++added;
					}
					++i;
				}
			}
			
			for( String variableName : Configuration.getInstance().MODEL_OUTPUT_VARIABLES )
			{
				Variable variable = ncFile.findVariable(variableName);
				if( variable == null )
				{
					log.error("Variable missing in netcdf: " + variableName);
					int j = 0;
					for(String line : csv )
					{
						csv.set(j,line + ",0.0");
						++j;
					}
				}
				if( variable != null )
				{
					Array data = variable.read();
					double[] vData = (double []) data.get1DJavaArray( double.class);
					int i = 0;
					int added = 0;
					for( double value : vData )
					{
						if( i >= offset && added < limit )
						{
							String csvValue = csv.get(added);
							csvValue = csvValue + "," + new Double(value).toString();
							csv.set(added,csvValue);
							++added;
						}
						++i;
					}
			    }
			}
			for( int k=0; k < csv.size(); ++k )
			{
				String line = csv.get(k);
				line = line + "\n";
				csv.set(k,line);
			}
		} 
		finally 
		{ 
			if (null != ncFile) try {
				ncFile.close();
			} catch (IOException ioe) {
			}
		}
		
		return csv;
	}

	public String getFirstDate(ModelOutput mo) throws IOException 
	{
		String filePath = mo.retrieveOutputFilePath();
		SimpleDateFormat sdfOut = new SimpleDateFormat(DATE_FORMAT_FIRST_LAST);
		NetcdfFile ncFile = null;
	    int offset = 0;
		
		try 
		{
			ncFile = NetcdfFile.open(filePath);
			
			//List<Calendar> dates = new ArrayList<Calendar>();
			
			Variable v = ncFile.findVariable("time");
			if( v != null )
			{
				Array data = v.read();
				double[] timeDouble = (double []) data.get1DJavaArray( double.class);
				int i = 0;
				int added = 0;
				if( timeDouble != null && timeDouble.length > 0 )
				{
				    double value = timeDouble[0];
					Calendar referenceDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					referenceDate.set(Calendar.YEAR,2002);
					referenceDate.set(Calendar.DAY_OF_MONTH,1);
					referenceDate.set(Calendar.MONTH,0);
					referenceDate.set(Calendar.HOUR,0);
					referenceDate.set(Calendar.MINUTE,1);
					referenceDate.set(Calendar.SECOND,0);
					referenceDate.set(Calendar.MILLISECOND,0);
					referenceDate.add(Calendar.SECOND,new Double(value).intValue());
					String outDate = sdfOut.format(referenceDate.getTime());
					return outDate;
				}
			}
		} 
		finally 
		{ 
			if (null != ncFile) try {
				ncFile.close();
			} catch (IOException ioe) {
			}
		}
		
		return null;
	}
	
	public String getLastDate(ModelOutput mo) throws IOException 
	{
		String filePath = mo.retrieveOutputFilePath();
		SimpleDateFormat sdfOut = new SimpleDateFormat(DATE_FORMAT_FIRST_LAST);
		NetcdfFile ncFile = null;
	    int offset = 0;
		
		try 
		{
			ncFile = NetcdfFile.open(filePath);
			
			//List<Calendar> dates = new ArrayList<Calendar>();
			
			Variable v = ncFile.findVariable("time");
			if( v != null )
			{
				Array data = v.read();
				double[] timeDouble = (double []) data.get1DJavaArray( double.class);
				int i = 0;
				int added = 0;
				if( timeDouble != null && timeDouble.length > 0 )
				{
				    double value = timeDouble[timeDouble.length-1];
					Calendar referenceDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					referenceDate.set(Calendar.YEAR,2002);
					referenceDate.set(Calendar.DAY_OF_MONTH,1);
					referenceDate.set(Calendar.MONTH,0);
					referenceDate.set(Calendar.HOUR,0);
					referenceDate.set(Calendar.MINUTE,1);
					referenceDate.set(Calendar.SECOND,0);
					referenceDate.set(Calendar.MILLISECOND,0);
					referenceDate.add(Calendar.SECOND,new Double(value).intValue());
					String outDate = sdfOut.format(referenceDate.getTime());
					return outDate;
				}
			}
		} 
		finally 
		{ 
			if (null != ncFile) try {
				ncFile.close();
			} catch (IOException ioe) {
			}
		}
		
		return null;
	}
}
