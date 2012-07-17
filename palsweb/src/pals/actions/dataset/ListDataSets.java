package pals.actions.dataset;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.User;
import pals.service.DataSetVersionService;
import pals.service.PublicServiceInterface;
import pals.service.UserServiceInterface;


public class ListDataSets extends UserAwareAction {

	Long limit;
	Long offset = 0l;
	String sortColumn = null;
	Boolean asc = false;
	UserServiceInterface userService;
	
	private Map<Integer,List<DataSetVersion>> dsvMap;
	
	private List<DataSetVersion> dataSetVersions;
	
	public String execute() {
		return SUCCESS;
	}
	
	private PublicServiceInterface publicService;
	
	private DataSetVersionService dataSetVersionService;

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
		if( dataSetVersions == null )
		{
			if( sortColumn != null && sortColumn.length() <= 0 ) sortColumn = null;
		    dataSetVersions = dataSetVersionService.getPublicDataSetVersions(getUser().getCurrentExperiment(),
		        getLimit().intValue(),getOffset().intValue(),sortColumn,asc);
		}
		return dataSetVersions;
	}

	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}

	public Long getLimit() {
		//return new Long(Configuration.getInstance().getIntProperty("PUBLIC_DATA_SET_VERSIONS_PER_PAGE"));
		return new Long(getUser().getItemsPerPageListPublicDataSetsWithDefault());
	}

	public void setLimit(Long limit) {
		User user = getUser();
		user.setItemsPerPageListPublicDataSets(limit.intValue());
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
		long count = dataSetVersionService.getPublicDataSetVersionsCount(getUser().getCurrentExperiment());
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
	
	public List<Integer> getItemsPerPageOptions()
	{
		return Configuration.getInstance().ITEMS_PER_PAGE_OPTIONS;
	}

	public UserServiceInterface getUserService() {
		return userService;
	}

	public void setUserService(UserServiceInterface userService) {
		this.userService = userService;
	}
}
