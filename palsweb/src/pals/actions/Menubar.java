package pals.actions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import pals.entity.User;
import pals.utils.LSMEvalConstants;


import com.opensymphony.xwork2.ActionSupport;

public class Menubar  extends ActionSupport implements SessionAware {

	String activeTab;
	private Map session;
	
	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public void setSession(Map session) {
		this.session = session;
	}
	
	public User getUser() {
		return (User)session.get(LSMEvalConstants.USER);
	}

	public String getMessage() {
		if (getUser() == null) {
			return "Not logged in";
		} 
		return "Logged in";
	}
	
	public boolean isLoggedIn() {
		if (getUser() == null) {
			return false;
		} 
		return true;
	}
	
	public List<String> getTabs() {
		List<String> tabs = new LinkedList<String>();
		tabs.add("homeTab");
		tabs.add("dataSetsTab");
		tabs.add("publicModelOutputsTab");
		if (isLoggedIn()) {
			tabs.add("logoutTab");
		} else {
			tabs.add("loginTab");
		}
		return tabs;
	}
	
	/*
	public Map<String,String> getMenuLinks() {
		HashMap menuLinkMap = new HashMap();
		menuLinkMap.put("Home", "Welcome.action");
		menuLinkMap.put("Data Sets", "ListDataSets.action");
		menuLinkMap.put("Publicly Available Model Outputs", "ListModelOutputs.action");
		if (isLoggedIn()) {
			menuLinkMap.put("My Model Outputs", "User/ListModelOutputs.action");
			menuLinkMap.put("My Models", "User/ListModels.action");
			menuLinkMap.put("My Data Sets", "User/ListDataSets.action");
			menuLinkMap.put("Upload", "User/Upload.action");
			menuLinkMap.put("Account Details", "User/AccountDetails.action");
			menuLinkMap.put("Logout", "User/Logout.action");
		} else {
			menuLinkMap.put("Login", "User/Login.action");
		}
		menuLinkMap
		return menuLinkMap;
	}*/
}

