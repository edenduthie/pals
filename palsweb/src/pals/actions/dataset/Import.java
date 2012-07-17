package pals.actions.dataset;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.User;
import pals.service.DataSetService;
import pals.service.DataSetVersionService;
import pals.service.UserServiceInterface;

public class Import extends UserAwareAction
{	
	public static String FORM = "form";
	public static String SUBMIT = "submit";
	
	DataSetVersionService dataSetVersionService;
	DataSetService dataSetService;
	Collection<DataSetVersion> dataSetVersions;
	UserServiceInterface userService;
	
	public static final Logger log = Logger.getLogger(Import.class);
	
	String dataSetIdList;
	Long limit;
	Long offset = 0l;
	
    public String form()
    {
    	return FORM;
    }
    
    public String submit()
    {
    	if( dataSetIdList != null && getUser().getCurrentExperiment() != null )
    	{
    		String[] splitList = dataSetIdList.split("\\,");
    		for( String dataSetId : splitList )
    		{
    			int dataSetIdInt = Integer.parseInt(dataSetId);
    			DataSet dataSet = dataSetService.get(dataSetIdInt);
				Experiment experiment = getUser().getCurrentExperiment();
				DataSet copy;
				try 
				{
					copy = dataSetService.copy(dataSet,experiment);
					dataSetService.setExperiment(copy, experiment);
					dataSetService.prepared(copy);
				} 
				catch (IOException e) 
				{
					log.error(e.getMessage());
				}
    		}
    	}
    	return SUBMIT;
    }
    
	public Collection<DataSetVersion> getLatestDataSetVersions() {
		// load public data sets from the main database with null experiment
		if( dataSetVersions == null )
		{
			dataSetVersions = dataSetVersionService.getPublicDataSetVersions(null,getLimit().intValue(),getOffset().intValue(),null,true);
		}
		return dataSetVersions;
	}

	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}

	public String getDataSetIdList() {
		return dataSetIdList;
	}

	public void setDataSetIdList(String dataSetIdList) {
		this.dataSetIdList = dataSetIdList;
	}

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}
	
	public Long getLimit() {
		//return new Long(Configuration.getInstance().getIntProperty("PUBLIC_DATA_SET_VERSIONS_PER_PAGE"));
		return new Long(getUser().getItemsPerPageImportDataSetWithDefault());
	}

	public void setLimit(Long limit) {
		User user = getUser();
		user.setItemsPerPageImportDataSet(limit.intValue());
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
		long count = dataSetVersionService.getPublicDataSetVersionsCountAllExperiments();
		if( offset + dataSetVersions.size() >= count ) return false;
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
