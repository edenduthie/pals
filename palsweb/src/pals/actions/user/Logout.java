package pals.actions.user;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import pals.utils.LSMEvalConstants;


import com.opensymphony.xwork2.ActionSupport;

public class Logout extends ActionSupport implements SessionAware {

	private Map session;
	
	public String execute() {
		session.clear();
		return SUCCESS;
	}

	public void setSession(Map session) {
		this.session = session;
	}
}
