package pals.actions;

import java.util.List;
import java.util.LinkedList;

import pals.entity.DataSet;
import pals.service.PublicServiceInterface;


import com.opensymphony.xwork2.ActionSupport;

public class ListDataSets extends UserAwareAction {

	public String execute() {
		return SUCCESS;
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
	
}
