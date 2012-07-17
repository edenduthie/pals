package pals.actions.user;

import java.util.List;

import pals.Configuration;
import pals.Globals;
import pals.actions.UserAwareAction;
import pals.entity.User;
import pals.service.ModelOutputService;
import pals.service.UserServiceInterface;

public class ListModelOutputsBase extends UserAwareAction
{
	UserServiceInterface userService;
	ModelOutputService modelOutputService;
	
	Integer modelId = null;
	String username = null;
	Integer dsvId = null;
	String access = null;
	
	Long limit;
	Long offset = 0l;
	
	String modelOutputNameSort = Globals.SORT_ASC;
	String ownerSort = Globals.SORT_NONE;
	String modelSort = Globals.SORT_NONE;
	String dataSetSort = Globals.SORT_NONE;
	String uploadedSort = Globals.SORT_NONE;
	String statusSort = Globals.SORT_NONE;
	String accessSort  = Globals.SORT_NONE;
	
	String firstPref = null;
	String secondPref = null;
	
	public List<Integer> getItemsPerPageOptions()
	{
		return Configuration.getInstance().ITEMS_PER_PAGE_OPTIONS;
	}
	
	public String getModelOutputNameSort() {
		return modelOutputNameSort;
	}

	public void setModelOutputNameSort(String modelOutputNameSort) {
		this.modelOutputNameSort = modelOutputNameSort;
	}
	
	public UserServiceInterface getUserService() {
		return userService;
	}

	public void setUserService(UserServiceInterface userService) {
		this.userService = userService;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getDsvId() {
		return dsvId;
	}

	public void setDsvId(Integer dsvId) {
		this.dsvId = dsvId;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getOwnerSort() {
		return ownerSort;
	}

	public void setOwnerSort(String ownerSort) {
		this.ownerSort = ownerSort;
	}

	public String getModelSort() {
		return modelSort;
	}

	public void setModelSort(String modelSort) {
		this.modelSort = modelSort;
	}

	public String getDataSetSort() {
		return dataSetSort;
	}

	public void setDataSetSort(String dataSetSort) {
		this.dataSetSort = dataSetSort;
	}

	public String getUploadedSort() {
		return uploadedSort;
	}

	public void setUploadedSort(String uploadedSort) {
		this.uploadedSort = uploadedSort;
	}

	public String getStatusSort() {
		return statusSort;
	}

	public void setStatusSort(String statusSort) {
		this.statusSort = statusSort;
	}

	public String getAccessSort() {
		return accessSort;
	}

	public void setAccessSort(String accessSort) {
		this.accessSort = accessSort;
	}

	public String getFirstPref() {
		return firstPref;
	}

	public void setFirstPref(String firstPref) {
		this.firstPref = firstPref;
	}

	public String getSecondPref() {
		return secondPref;
	}

	public void setSecondPref(String secondPref) {
		this.secondPref = secondPref;
	}
}
