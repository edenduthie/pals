package pals.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.entity.User;

public class AnalysisDAO extends DAO
{
	public List<Analysis> getAnalysisByAnalysisType(String analysisType, User user)
    {
    	return getAnalysisByAnalysisType(analysisType, Analysis.STATUS_COMPLETE, user);
    }
	
	public List<Analysis> getAnalysisByAnalysisType(String analysisType, char status)
    {
    	return getAnalysisByAnalysisType(analysisType, status, null);
    }
	
    public List<Analysis> getAnalysisByAnalysisType(String analysisType)
    {
    	return getAnalysisByAnalysisType(analysisType, Analysis.STATUS_COMPLETE, null);
    }
    
    public List<Analysis> getAnalysisByAnalysisType(String analysisTypeName, char status, User user)
    {
    	String queryString = "SELECT a.id, at.analysisTypeName, at.variableName, anal.id, anal.name "
    	+ ", ds.id, ds.name, o.username, anal.isPublic"
    	+ " from Analysis a"
    	+ " inner join a.analysisType at" 
    	+ " inner join a.analysable anal"
    	+ " left join a.analysable.experiment e"
    	+ " inner join a.analysable.dataSet ds"
    	+ " inner join a.analysable.owner o"
    	+ " where a.analysisType.type=:type and a.status=:status";
    	
    	if( user != null && user.getCurrentExperiment() != null )
    	{
    		queryString += " and anal.experiment.id=:experimentId";
    	}
    	else
    	{
    		queryString += " and (anal.experiment = NULL";
    		queryString += " or e.shareWithAll=:shareWithAll)";
    	}
    	
    	Query query = getEntityManager().createQuery(queryString);
    	query.setParameter("type",analysisTypeName);
    	query.setParameter("status",status);
    	if( user != null && user.getCurrentExperiment() != null )
    	{
    		query.setParameter("experimentId",user.getCurrentExperiment().getId());
    	}
    	else
    	{
    		query.setParameter("shareWithAll", true);
    	}
    	List<Object[]> results = query.getResultList();
    	List<Analysis> analysisList = new ArrayList<Analysis>();
    	for( Object[] result : results )
    	{
    		int i=0;
    		Analysis analysis = new Analysis();
    		analysis.setId((Integer)result[i++]);
    		AnalysisType analysisType = new AnalysisType();
    		analysisType.setAnalysisTypeName((String)result[i++]);
    		analysisType.setVariableName((String)result[i++]);
    		analysis.setAnalysisType(analysisType);
    		DataSetVersion dsv = new DataSetVersion();
    		dsv.setId((Integer)result[i++]);
    		dsv.setName((String)result[i++]);
    		DataSet dataSet = new DataSet();
    		dataSet.setId((Integer)result[i++]);
    		dataSet.setName((String)result[i++]);
    		dsv.setDataSet(dataSet);
    		analysis.setAnalysable(dsv);
    		User owner = new User();
    		owner.setUsername((String)result[i++]);
    		dsv.setOwner(owner);
    		dsv.setIsPublic((Boolean)result[i++]);
    		analysisList.add(analysis);
    	}
    	return analysisList;
    }
    
    public List<Analysis> getAnalysisByModelId(Integer modelId, char status, User user)
    {
    	List<Analysis> analysisList = new ArrayList<Analysis>();
    	
    	// first we load all model outputs for the model
    	String queryString = "select id from ModelOutput as mo";
    	queryString += " where mo.modelId=:modelId";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL ";
		}
    	Query query = getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
    	query.setParameter("modelId",modelId);
    	List<Integer> modelOutputIds = query.getResultList();
    	
    	// load those from shared experiments
    	if( user.getCurrentExperiment() == null )
    	{
	    	queryString = "select id from ModelOutput as mo";
	    	queryString += " where mo.modelId=:modelId";
			queryString += " and experiment.shareWithAll = true ";
	    	query = getEntityManager().createQuery(queryString);
	    	query.setParameter("modelId",modelId);
	    	List<Integer> sharedModelOutputIds = query.getResultList();
	    	for( Integer i : sharedModelOutputIds )
	    	{
	    		modelOutputIds.add(i);
	    	}
    	}
    	
    	for( Integer modelOutputId : modelOutputIds )
    	{
    		// load the analysis for this model output
    		List<Analysis> modelOutputAnalysisList = getAnalysisByModelOutputId(modelOutputId,status);
    		analysisList.addAll(modelOutputAnalysisList);
    	}
    	
    	return analysisList;
    }
    
    public List<Analysis> getAnalysisByModelOutputId(Integer modelOutputId,char status)
    {
    	String queryString = "SELECT a.id, at.analysisTypeName, at.variableName, anal.id, anal.name "
    	+ ", o.username, anal.modelId, dsv.id, dsv.dataSetId, ds.id, ds.name, anal.accessLevel, dsvO.username, dsv.name"
    	+ " from Analysis a"
    	+ " inner join a.analysisType at" 
    	+ " inner join a.analysable anal"
    	+ " inner join a.analysable.owner o"
    	+ " inner join a.analysable.dataSetVersion dsv"
    	+ " inner join a.analysable.dataSetVersion.dataSet ds"
    	+ " inner join a.analysable.dataSetVersion.owner dsvO"
    	+ " where a.analysable.id=:modelOutputId and a.status=:status";
    	Query query = getEntityManager().createQuery(queryString);
    	query.setParameter("modelOutputId",modelOutputId);
    	query.setParameter("status",status);
    	List<Object[]> results = query.getResultList();
    	List<Analysis> analysisList = new ArrayList<Analysis>();
    	for( Object[] result : results )
    	{
    		int i=0;
    		Analysis analysis = new Analysis();
    		analysis.setId((Integer)result[i++]);
    		AnalysisType analysisType = new AnalysisType();
    		analysisType.setAnalysisTypeName((String)result[i++]);
    		analysisType.setVariableName((String)result[i++]);
    		analysis.setAnalysisType(analysisType);
    		ModelOutput mo = new ModelOutput();
    		mo.setId((Integer)result[i++]);
    		mo.setName((String)result[i++]);
    		analysis.setAnalysable(mo);
    		User owner = new User();
    		owner.setUsername((String)result[i++]);
    		mo.setOwner(owner);
    		mo.setModelId((Integer)result[i++]);
    		DataSetVersion dsv = new DataSetVersion();
    		dsv.setId((Integer)result[i++]);
    		dsv.setDataSetId((Integer)result[i++]);
    		DataSet dataSet = new DataSet();
    		dataSet.setId((Integer)result[i++]);
    		dataSet.setName((String)result[i++]);
    		dsv.setDataSet(dataSet);
    		mo.setDataSetVersion(dsv);
    		mo.setAccessLevel((String)result[i++]);
    		User dsvOwner = new User();
    		dsvOwner.setUsername((String) result[i++]);
    		dsv.setOwner(dsvOwner);
    		dsv.setName((String)result[i++]);
    		analysisList.add(analysis);
    	}
    	return analysisList;
    }
}
