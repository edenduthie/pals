package pals.actions.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.dao.CountryDAO;
import pals.dao.DataSetDAO;
import pals.dao.VegetationTypeDAO;
import pals.entity.Country;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Location;
import pals.entity.User;
import pals.entity.VegetationType;
import pals.service.DataSetService;
import pals.service.DataSetVersionService;
import pals.service.PublicServiceInterface;
import pals.service.UserServiceInterface;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;

public class DataSetAction extends UserAwareAction implements Preparable, ModelDriven<DataSet>, Validateable
{
	Long limit;
	Long offset = 0l;

	private static Logger log = Logger.getLogger(DataSetAction.class);

	private DataSet dataSet = new DataSet();
	
	private Integer countryIdSet;
	private String vegTypeSet;

	private String vegTypeNew;
	
	// data access objects
	private CountryDAO countryDAO;
	private VegetationTypeDAO vegetationTypeDAO;
	private DataSetDAO dataSetDAO;
	
	// List of countries for the drop down list
	private List<Country> countries;
	// List of vegetation types for the drop down list
	private List<VegetationType> vegetationTypes;
	// List of Measurement Aggregation Values for the drop down list
	private List<String> measurementAggregations;
	
	private long requestId;
	
	private String actionMethod;
	
	private String ADD = "add";
	private String LIST = "list";
	private String EDIT = "edit";
	private String CANCEL = "cancel";
	private String DELETE = "delete";
	
	private String latString = null; // must use string as struts2 has a bug with negative doubles
	private String lonString = null;
	private String elevationString = null;
	private String utcOffsetHoursString = null;
	
	private String latDeg = null;
	private String latMin = null;
	private String latSec = null;
	
	private String lonDeg = null;
	private String lonMin = null;
	private String lonSec = null;
	
	private DataSetService dataSetService;
	private DataSetVersionService dataSetVersionService;
	private UserServiceInterface userService;
	
	List<DataSet> myDataSets = null;
	
	String message;
	
	String sortColumn = null;
	Boolean asc = false;
	
	@SkipValidation
	public String add()
	{
		return ADD;
	}
	
	@SkipValidation
	public String cancel()
	{
		return CANCEL;
	}
	
	public String save() {
		log.debug("Saving DataSet");
		dataSet.setExperiment(getUser().getCurrentExperiment());
        return saveOrUpdate();
	}
	
	public String saveOrUpdate()
	{
		if( (getLatString() == null || getLatString().length() <= 0) && 
		    (getLatDeg() != null || getLatMin() != null || getLatSec() != null) )
		{
			Location location = new Location();
			location.setLat(getLatDeg(), getLatMin(), getLatSec());
			dataSet.setLatitude(location.getLatRounded());
		}
		else
		{
			dataSet.updateLatString(latString);
		}
		if( (getLonString() == null || getLonString().length() <= 0 ) &&
		    ( getLonDeg() != null || getLonMin() != null || getLonSec() != null ) )
		{
			Location location = new Location();
			location.setLon(getLonDeg(), getLonMin(), getLonSec());
			dataSet.setLongitude(location.getLonRounded());
		}
		else
		{
			dataSet.updateLonString(lonString);
		}
		dataSet.updateElevationString(elevationString);
		dataSet.updateTimezoneOffsetString(utcOffsetHoursString);
		
		log.debug("Saving DataSet");
		for( Country country : getCountries() )
		{
			if( country.getId().equals(countryIdSet) )
			{
				dataSet.setCountry(country);
			}
		}
		vegTypeNew = vegTypeNew.trim();
		VegetationType vegetationType = new VegetationType();
		if( vegTypeNew != null && vegTypeNew.length() > 0) 
		{
			vegetationType.setVegetationType(vegTypeNew);
			vegetationTypeDAO.create(vegetationType);
		}
		else
		{
		    vegetationType.setVegetationType(vegTypeSet);
		}
		dataSet.setVegType(vegetationType);
		dataSet.setOwner(getUser());
		dataSet.setUserName(getUser().getUsername());
		
		// set the latest version because it gets lost
		if( dataSet.getId() != null )
		{
		    DataSet loadedDataSet = dataSetDAO.get(dataSet.getId());
		    getDataSet().setLatestVersion(loadedDataSet.getLatestVersion());
		    getDataSet().setDownloadCount(loadedDataSet.getDownloadCount());
		}
		
		dataSetDAO.create(dataSet);
		
		return LIST;
	}
	
	@SkipValidation
	public String list() 
	{
        return LIST;
	}
	
    @SkipValidation
    public String edit() throws Exception
    {
    	log.debug("Edit Data Set: " + getRequestId());
    	return EDIT;
    }
    
    public String update()
    {
		log.debug("Updating DataSet: " + getDataSet().getId());
		dataSet.setExperiment(getUser().getCurrentExperiment());
        return saveOrUpdate();
    }
    
    @SkipValidation
    public String delete()
    {
    	dataSet = dataSetService.get(new Long(requestId).intValue());
    	if( dataSet != null )
    	{
//	    	if( !dataSet.getOwner().getUsername().equals(getUser().getUsername()) )
//	    	{
//	    		message = "Sorry you are not the owner of the data set and cannot delete it: " + dataSet.getName();
//	    		log.error(message);
//	    		return DELETE;
//	    	}
	    	if( !getUser().isAdmin() )
	    	{
	    		message = "You must be an administrator to delete a data set";
	    		log.error(message);
	    		return DELETE;
	    	}
	    	else
	    	{
	    		dataSetService.delete(dataSet);
	    		message = "Data set, versions, and model outputs deleted: " + dataSet.getName();
	    	}
    	}
    	return DELETE;
    }
	
	private PublicServiceInterface publicService;

	public PublicServiceInterface getPublicService() {
		return publicService;
	}

	public void setPublicService(PublicServiceInterface publicService) {
		this.publicService = publicService;
	}
	
	public List<DataSet> getDataSets() {
		return getPublicService().getDataSets();
	}
	
	public List<DataSetVersion> getDataSetVersions() {
		return dataSetVersionService.getDataSetVersions();
	}
	
	/***
	 * @return List containing the most recent DataSetVersion for every DataSet.
	 */
	public Collection<DataSetVersion> getLatestDataSetVersions() {
		List<DataSetVersion> userDataSetVersions = dataSetService.getDataSetVersions(getUser());
		Map<Integer,DataSetVersion> dsvMap = new HashMap<Integer,DataSetVersion>(); // dataSetId --> dataSetVersion
		Iterator<DataSetVersion> iter = userDataSetVersions.iterator();
		DataSetVersion dsv;
		while (iter.hasNext()) {
			dsv = iter.next();
			if (!dsvMap.containsKey(dsv.getDataSetId()) || dsvMap.get(dsv.getDataSetId()).getId() < dsv.getId()) {
				dsvMap.put(dsv.getDataSetId(), dsv);
			}
		}
		return dsvMap.values();
	}

	public List<DataSet> getMyDataSets() 
	{
		if( myDataSets == null )
		{
			if( sortColumn != null && sortColumn.length() <= 0 ) sortColumn = null;
		    myDataSets = dataSetService.getMyDataSets(getUser(),getLimit().intValue(),
		        getOffset().intValue(), sortColumn, asc);
		}
		return myDataSets;
	}

	public CountryDAO getCountryDAO() {
		return countryDAO;
	}
	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}
	
	@Override
	public void prepare() throws Exception {
		log.debug("Preparing action: " + getClass().getName());
		countries = getCountryDAO().getAll();
		log.debug("Loaded countries: " + countries.size());
		vegetationTypes = getVegetationTypeDAO().getAll();
		log.debug("Loaded vegetation types: " + vegetationTypes.size());
		
		measurementAggregations = new ArrayList<String>();
		measurementAggregations.add("proceeding time period");
		measurementAggregations.add("preceding time period");
		measurementAggregations.add("time either side of time stamp");
		
		log.debug("Request data set id: " + getRequestId());
		
		if( getRequestId() == 0 )
		{
			log.debug("No data set id provided");
			dataSet = new DataSet();
		}
		else
		{
			log.debug("Retrieving data set: " + getRequestId());
			dataSet = dataSetDAO.get(new Long(getRequestId()).intValue());
			latString = dataSet.retrieveLatString();
			lonString = dataSet.retrieveLonString();
			elevationString = dataSet.retrieveElevationString();
			utcOffsetHoursString = dataSet.retrieveTimezoneOffsetHoursString();
			countryIdSet = dataSet.getCountry().getId();
			vegTypeSet = dataSet.getVegType().getVegetationType();
		}
	}
	public List<Country> getCountries() {
		return countries;
	}
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	public List<VegetationType> getVegetationTypes() {
		return vegetationTypes;
	}
	public void setVegetationTypes(List<VegetationType> vegetationTypes) {
		this.vegetationTypes = vegetationTypes;
	}
	public VegetationTypeDAO getVegetationTypeDAO() {
		return vegetationTypeDAO;
	}
	public void setVegetationTypeDAO(VegetationTypeDAO vegetationTypeDAO) {
		this.vegetationTypeDAO = vegetationTypeDAO;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	@Override
	public DataSet getModel() {
		return getDataSet();
	}

	public String getVegTypeNew() {
		return vegTypeNew;
	}

	public void setVegTypeNew(String vegTypeNew) {
		this.vegTypeNew = vegTypeNew;
	}

	public DataSetDAO getDataSetDAO() {
		return dataSetDAO;
	}

	public void setDataSetDAO(DataSetDAO dataSetDAO) {
		this.dataSetDAO = dataSetDAO;
	}

	public Integer getCountryIdSet() {
		return countryIdSet;
	}

	public void setCountryIdSet(Integer countryIdSet) {
		this.countryIdSet = countryIdSet;
	}

	public String getVegTypeSet() {
		return vegTypeSet;
	}

	public void setVegTypeSet(String vegTypeSet) {
		this.vegTypeSet = vegTypeSet;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	/*
	 * Returns the correct error page for the update action
	 */
	public String getUpdate()
	{
		return "edit";
	}
	
	/*
	 * Returns the correct error page for the save action
	 */
	public String getSave()
	{
		return "add";
	}

	public List<String> getMeasurementAggregations() {
		return measurementAggregations;
	}

	public void setMeasurementAggregations(List<String> measurementAggregations) {
		this.measurementAggregations = measurementAggregations;
	}

	public String getLatString() {
		return latString;
	}

	public void setLatString(String latString) {
		this.latString = latString;
	}

	public String getLonString() {
		return lonString;
	}

	public void setLonString(String lonString) {
		this.lonString = lonString;
	}

	public String getElevationString() {
		return elevationString;
	}

	public void setElevationString(String elevationString) {
		this.elevationString = elevationString;
	}

	public String getLatDeg() {
		return latDeg;
	}

	public void setLatDeg(String latDeg) {
		this.latDeg = latDeg;
	}

	public String getLatMin() {
		return latMin;
	}

	public void setLatMin(String latMin) {
		this.latMin = latMin;
	}

	public String getLatSec() {
		return latSec;
	}

	public void setLatSec(String latSec) {
		this.latSec = latSec;
	}

	public String getLonDeg() {
		return lonDeg;
	}

	public void setLonDeg(String lonDeg) {
		this.lonDeg = lonDeg;
	}

	public String getLonMin() {
		return lonMin;
	}

	public void setLonMin(String lonMin) {
		this.lonMin = lonMin;
	}

	public String getLonSec() {
		return lonSec;
	}

	public void setLonSec(String lonSec) {
		this.lonSec = lonSec;
	}

	public String getUtcOffsetHoursString() {
		return utcOffsetHoursString;
	}

	public void setUtcOffsetHoursString(String utcOffsetHoursString) {
		this.utcOffsetHoursString = utcOffsetHoursString;
	}
	
	public void validate()
	{
		if( (latString == null || latString.length() <= 0) &&
		    (latDeg == null || latDeg.length() <= 0) &&
		    (latMin == null || latMin.length() <= 0 ) &&
		    (latSec == null || latSec.length() <= 0 ) )
		{
			addFieldError("latString","Latitude is required");
		}
		if( (lonString == null || lonString.length() <= 0) &&
			    (lonDeg == null || lonDeg.length() <= 0) &&
			    (lonMin == null || lonMin.length() <= 0 ) &&
			    (lonSec == null || lonSec.length() <= 0 ) )
		{
			addFieldError("lonString","Longitude is required");
		}
	}

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}

	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}
	

	public Long getLimit() {
		return new Long(getUser().getItemsPerPageMyDataSetsWithDefault());
	}

	public void setLimit(Long limit) {
		User user = getUser();
		user.setItemsPerPageMyDataSets(limit.intValue());
		userService.saveUser(user);
	}

	public Long getOffset() {
		if( offset == null ) offset = 0l;
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}
	
	public boolean getHasNextPage()
	{
		long count = dataSetService.getMyDataSetsCount(getUser());
		if( offset + getMyDataSets().size() >= count ) return false;
		else return true;
	}
	
	public long getNextOffset()
	{
		return offset + getLimit();
	}
	
	public long getPreviousOffset()
	{
		return offset - getLimit();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public Boolean getAsc() {
		if( asc == null ) return true;
		else return asc;
	}

	public void setAsc(Boolean asc) {
		this.asc = asc;
	}

	public UserServiceInterface getUserService() {
		return userService;
	}

	public void setUserService(UserServiceInterface userService) {
		this.userService = userService;
	}
	
	public List<Integer> getItemsPerPageOptions()
	{
		return Configuration.getInstance().ITEMS_PER_PAGE_OPTIONS;
	}
}
