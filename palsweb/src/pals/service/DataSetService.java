package pals.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import pals.Configuration;
import pals.Globals;
import pals.dao.DAO;
import pals.entity.Analysable;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.entity.User;

@Transactional
public class DataSetService 
{
	DAO dao;
	DataSetVersionService dataSetVersionService;
	ModelOutputService modelOutputService;
	
	public static final Logger log = Logger.getLogger(DataSetService.class);
	
	public static String COLUMN_NAME = "name";
	public static String COLUMN_COUNTRY = "country";
	public static String COLUMN_VEG_TYPE = "vegType";


	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
	
    public DataSet get(Integer id)
    {
    	Object object = dao.get(DataSet.class.getName(), "id", id);
    	return (DataSet) object;
    }
    
    public void update(DataSet dataSet)
    {
    	Experiment e = dataSet.getExperiment();
    	if( e != null )
    	{
    		e.updateActivity();
    		dao.update(e);
    	}
    	dao.update(dataSet);
    }
    
    public List<DataSetVersion> getDataSetVersions(Integer dataSetId,Boolean publicOnly)
    {
    	String queryString = "from DataSetVersion where dataSetId=:id";
    	if( publicOnly )
    	{
    		queryString += " and isPublic=:publicOnly";
    	}
    	Query query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("id", dataSetId);
    	if( publicOnly )
    	{
    		query.setParameter("publicOnly", publicOnly);
    	}
    	return query.getResultList();
    }
    
    public void setLatestVersion(Integer dataSetVersionId, Integer dataSetId)
    {
    	DataSetVersion dsv = dataSetVersionService.get(dataSetVersionId);
    	dsv.setIsPublic(true);
    	dataSetVersionService.update(dsv);
    	DataSet dataSet = get(dataSetId);
    	dataSet.setLatestVersion(dsv);
    	update(dataSet);
    }
	
	/**
	 * Returns true if the given data set version name already exists for this data set
	 * @param name - the name to check for
	 * @return - true if the version name exists, false otherwise
	 */
	public boolean existsVersionName(String name, DataSet dataSet)
	{
		String queryString = "from DataSetVersion where name=:name and dataSetId=:dataSetId";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("name", name);
		query.setParameter("dataSetId", dataSet.getId());
		List<DataSetVersion> versions = query.getResultList();
		if( versions.size() > 0 ) return true;
		else return false;
	}
	
	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}
	
	public List<DataSet> getMyDataSetsAdvancedSorting(User user, Integer limit, Integer offset,
	    Integer countryId, Integer vegTypeId, String nameSort,
	    String countrySort, String vegTypeSort,
	    String firstSortColumn, String secondSortColumn)
	{
		String queryString = "from DataSet ds where userName = :username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL ";
		}
		
		queryString = addSort(queryString,nameSort,countrySort,vegTypeSort,firstSortColumn,
		    secondSortColumn);
		
		Query query = dao.getEntityManager()
				.createQuery(queryString);
		query.setParameter("username", user.getUsername());
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		if( offset != null ) query.setFirstResult(offset);
		if( limit != null ) query.setMaxResults(limit);
		List<DataSet> list = query.getResultList();
		if (list == null)
			list = new LinkedList<DataSet>();
		return list;	
	}
	
	public String addSort(String queryString, String nameSort, String countrySort, String vegTypeSort,
	    String firstPref, String secondPref)
	{
		if( queryString == null ) return null;
		
		String sortString = "";
		
		List<String> sortFields = new ArrayList<String>();
		if( firstPref != null && firstPref.length() > 0 ) sortFields.add(firstPref);
		if( secondPref != null && secondPref.length() > 0 ) sortFields.add(secondPref);
		if( nameSort != null && nameSort.length() > 0 && !sortFields.contains(COLUMN_NAME))
		{
			sortFields.add(COLUMN_NAME);
		}
		if( countrySort != null && countrySort.length() > 0 && !sortFields.contains(COLUMN_COUNTRY))
		{
			sortFields.add(COLUMN_COUNTRY);
		}
		if( vegTypeSort != null && vegTypeSort.length() > 0 && !sortFields.contains(COLUMN_VEG_TYPE))
		{
			sortFields.add(COLUMN_VEG_TYPE);
		}
		
		for( String fieldName : sortFields )
		{
			sortString = addSortItem(sortString,fieldName,nameSort,countrySort,vegTypeSort);
		}
		
		if( sortString.length() > 0 ) sortString = " order by " + sortString.substring(0,sortString.length()-1);
		else sortString = " ORDER BY ds.name ASC";
		
		return queryString + sortString;
	}
	
	public String addSortItem(String sortString, String fieldName, String nameSort, String countrySort, String vegTypeSort)
	{
		if( fieldName == null ) return sortString;
		if( fieldName.equals(COLUMN_NAME) )
		{
			if( !nameSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
			    sortString  += " ds.name " + nameSort + ",";
			}
		}
		if( fieldName.equals(COLUMN_COUNTRY) )
		{
			if( !countrySort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
			    sortString  += " ds.country.name " + countrySort + ",";
			}
		}
		if( fieldName.equals(COLUMN_VEG_TYPE) )
		{
			if( !vegTypeSort.equalsIgnoreCase(Globals.SORT_NONE) )
			{
			    sortString  += " ds.vegType.vegetationType " + vegTypeSort + ",";
			}
		}
		return sortString;
	}
	
	public List<DataSet> getMyDataSets(User user)
	{
		int limit = Configuration.getInstance().getIntProperty("MY_DATA_SETS_NUM_PER_PAGE");
		return getMyDataSets(user,limit,0,null,true);
	}

	public List<DataSet> getMyDataSets(User user, Integer limit, Integer offset, String sortColumn,
	    boolean asc) {
		String queryString = "from DataSet where userName = :username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL ";
		}
		if( sortColumn == null )
		{
		    queryString += " order by id desc";
		}
		else if( sortColumn != null )
		{ 
			if( sortColumn.equals(COLUMN_NAME) )
			{
			    queryString += " order by name";
			}
			else if( sortColumn.equals(COLUMN_COUNTRY) )
			{
				queryString += " order by country.name";
			}
			else if( sortColumn.equals(COLUMN_VEG_TYPE) )
			{
				queryString += " order by vegType.vegetationType";
			}
			else
			{
				log.error("Sort Column code not found: " + sortColumn);
				queryString += " order by id";
			}
			if( asc )
			{
				queryString += " asc";
			}
			else
			{
				queryString += " desc";
			}
		}
		Query query = dao.getEntityManager()
				.createQuery(queryString);
		query.setParameter("username", user.getUsername());
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		List<DataSet> list = query.getResultList();
		if (list == null)
			list = new LinkedList<DataSet>();
		return list;
	}
	
	public long getMyDataSetsCount(User user) {
		String queryString = "select count(id) from DataSet where userName = :username";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " and experiment.id=:eid";
		}
		else
		{
			queryString += " and experiment = NULL ";
		}
		Query query = dao.getEntityManager()
				.createQuery(queryString);
		query.setParameter("username", user.getUsername());
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		return (Long) query.getSingleResult();
	}
	
	public List<DataSetVersion> getDataSetVersions(User user) {
		String queryString = "from DataSet";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " where experiment.id=:eid";
		}
		else
		{
			queryString += " where experiment = null";
		}
		queryString += " order by id desc";
        log.debug("Query: " + queryString);
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		List<DataSet> list = query.getResultList();
		if( user.getCurrentExperiment() == null )
		{
			queryString = "from DataSet where experiment.shareWithAll=:shareWithAll";
			query = dao.getEntityManager().createQuery(queryString);
			query.setParameter("shareWithAll",true);
			List<DataSet> sharedWithAll = query.getResultList();
			for( DataSet dataSet : sharedWithAll )
			{
				list.add(dataSet);
			}
		}
        log.debug("Data sets retrieved: " + list.size());
		ArrayList<DataSetVersion> dataSetVersions = new ArrayList();
		for( DataSet dataSet : list )
		{
			if( dataSet.getLatestVersion() != null )
			{
			    dataSetVersions.add(dataSet.getLatestVersion());
			}
		}
		return dataSetVersions;
	}
	
	public List<DataSet> getDataSets(User user) {
		String queryString = "from DataSet";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " where experiment.id=:eid";
		}
		else
		{
			queryString += " where experiment = null";
		}
		queryString += " order by id desc";
        log.debug("Query: " + queryString);
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
		List<DataSet> list = query.getResultList();
		if( user.getCurrentExperiment() == null )
		{
			queryString = "from DataSet where experiment.shareWithAll=:shareWithAll";
			query = dao.getEntityManager().createQuery(queryString);
			query.setParameter("shareWithAll",true);
			List<DataSet> sharedWithAll = query.getResultList();
			for( DataSet dataSet : sharedWithAll )
			{
				list.add(dataSet);
			}
		}
        log.debug("Data sets retrieved: " + list.size());
		ArrayList<DataSet> result = new ArrayList<DataSet>();
		for( DataSet dataSet : list )
		{
			if( dataSet.getLatestVersion() != null )
			{
			    result.add(dataSet);
			}
		}
		return result;
	}
	
	/**
	 * Deletes this data set and all its versions
	 */
	public void delete(DataSet dataSet)
	{
		log.info("Deleting data set: " + dataSet.getId());
		// first we delete any associated Model Outputs
		log.debug("Deleting model outputs for data set: " + dataSet.getId());
		String queryString = "from ModelOutput where dataSetVersion.dataSet.id=:dataSetId";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("dataSetId",dataSet.getId());
		List<ModelOutput> moList = query.getResultList();
		for(ModelOutput mo : moList)
		{
			modelOutputService.deleteModelOutput(mo);
		}
		
		// we need to set the latest version to null here otherwise we can't
		// delete all the data set versions
		dataSet = get(dataSet.getId());
		dataSet.setLatestVersion(null);
		dao.update(dataSet);
		
		List<DataSetVersion> dataSetVersions = getDataSetVersions(dataSet.getId(),false);
		log.info("Deleting all data set versions for data set: " + dataSet.getId());
		for( DataSetVersion dsv : dataSetVersions )
		{
			log.info("Deleting data set version: " + dsv.getId());
			dataSetVersionService.delete(dsv.getId());
			dao.getEntityManager().flush();
		}
		
		dao.delete(DataSet.class.getName(),"id", dataSet.getId());
		dao.getEntityManager().flush();
	}
	
	/**
	 * Creates a copy of this data set along with its data set versions
	 * @return
	 * @throws IOException - If there is an error copying the files of the data set version
	 */
	public DataSet copy(DataSet old, Experiment experiment) throws IOException
	{
		DataSet copy = new DataSet();
		BeanUtils.copyProperties(old, copy);
		copy.setId(null);
		copy.setStatus(Analysable.STATUS_NEW);
		copy.setDownloadCount(0);
		copy.setFiles(new ArrayList<PalsFile>());
		copy.setCopiedFrom(old);
		copy.setCopiedTo(new ArrayList<Experiment>());
		dao.persist(copy);
		copyDataSetVersions(old,copy);
		
		old.getCopiedTo().add(experiment);
		dao.update(old);
		return copy;
	}
	
	public void copyDataSetVersions(DataSet src, DataSet dest) throws IOException
	{
		List<DataSetVersion> dsvList = getDataSetVersions(src.getId(), false);
		for( DataSetVersion dsv : dsvList )
		{
			DataSetVersion copy = dataSetVersionService.copy(dsv,dest);
			// check to see if we are making a copy of the latest version, if we are set the copy
			// of it as the latest version of the destination DataSet
			if( src.getLatestVersion() != null && src.getLatestVersion().getId().equals(dsv.getId()) )
			{
				dest.setLatestVersion(copy);
			    dao.update(dest);
			}
		}
	}
	
    /**
     * Sets the given Experiment as the current experiment for the given DataSet and all
     * its corresponding DataSetVersions
     * @param dataSet
     * @param experiment
     */
    public void setExperiment(DataSet dataSet, Experiment experiment)
    {
    	dataSet.setExperiment(experiment);
    	dao.update(dataSet);
    	List<DataSetVersion> dsvList = getDataSetVersions(dataSet.getId(),false);
    	for( DataSetVersion dsv : dsvList )
    	{
    		dsv.setExperiment(experiment);
    		dao.update(dsv);
    	}
    }
    
    public void prepared(DataSet dataSet)
    {
    	dataSet.setStatus(Analysable.STATUS_NEW);
    	dao.update(dataSet);
    	List<DataSetVersion> dsvList = getDataSetVersions(dataSet.getId(),false);
    	for( DataSetVersion dsv : dsvList )
    	{
    		dsv.setStatus(Analysable.STATUS_PREPARED);
    		dao.update(dsv);
    	}
    }

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}
}
