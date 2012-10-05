package pals.actions.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import pals.Globals;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.User;
import pals.service.ModelOutputService;
import pals.service.UserServiceInterface;

public class ListPublicModelOutputs extends ListModelOutputsBase {
	
	private static final long serialVersionUID = -7270191679580607582L;
	public static final Logger log = Logger.getLogger(ListPublicModelOutputs.class);
	
	List<Model> models = new ArrayList<Model>();
	List<User> users = new ArrayList<User>();
	List<DataSetVersion> dsvs = new ArrayList<DataSetVersion>();
	List<String> accessVersions = new ArrayList<String>();
	List<ModelOutput> publicModelOutputs;
	
	public void retrieveModelOutputsAndDropDownListItems()
	{
		publicModelOutputs = modelOutputService.getPublicModelOutputs(getUser(),modelId,username,dsvId,access,
		    getLimit().intValue(),getOffset().intValue(), modelOutputNameSort, ownerSort, modelSort, dataSetSort,
		    uploadedSort,statusSort,accessSort,firstPref,secondPref);
		
		// revist, this is very inefficient, use custom queries instead
		List<ModelOutput> allModelOutputsNoPagination = modelOutputService.getPublicModelOutputs(getUser(),modelId,username,dsvId,access,
			    1000000,0);
		models = modelOutputService.getUniqueModels(allModelOutputsNoPagination);
		users = modelOutputService.getUniqueUsers(allModelOutputsNoPagination);
		dsvs = modelOutputService.getUniqueDataSetVersions(allModelOutputsNoPagination);
		accessVersions = modelOutputService.getUniqueAccess(allModelOutputsNoPagination);
	}
	
	@SkipValidation
	public String execute()
	{
		log.debug("Execute public model outputs " +
			    "modelId: " + modelId + " username: " + username + " dsvId: " + dsvId + " access: " + access);
		
		retrieveModelOutputsAndDropDownListItems();
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String input()
	{
		log.debug("Input public model outputs " +
			    "modelId: " + modelId + " username: " + username + " dsvId: " + dsvId + " access: " + access);
		
		retrieveModelOutputsAndDropDownListItems();
		
		return INPUT;
	}

	public List<ModelOutput> getModelOutputs() {
		return publicModelOutputs;
	}
	
	public String getAccessLevel() {
		return ModelOutput.ACCESS_LEVEL_PUBLIC;
	}

	public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<DataSetVersion> getDsvs() {
		return dsvs;
	}

	public void setDsvs(List<DataSetVersion> dsvs) {
		this.dsvs = dsvs;
	}

	public List<String> getAccessVersions() {
		return accessVersions;
	}

	public void setAccessVersions(List<String> accessVersions) {
		this.accessVersions = accessVersions;
	}
	
	public Long getLimit() {
		return new Long(getUser().getItemsPerPageListPublicModelOutputsWithDefault());
	}

	public void setLimit(Long limit) {
		User user = getUser();
		user.setItemsPerPageListPublicModelOutputs(limit.intValue());
		userService.saveUser(user);
	}
	
	public boolean getHasNextPage()
	{
		long count = modelOutputService.getPublicModelOutputsCount(getUser(),modelId,username,dsvId,access);
		if( offset + publicModelOutputs.size() >= count ) return false;
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
	
	public String getMainAccessLevel()
	{
		return getAccessLevel();
	}
}
