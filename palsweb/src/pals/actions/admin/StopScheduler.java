package pals.actions.admin;

import pals.actions.UserAwareAction;
import pals.analysis.Scheduler;

import com.opensymphony.xwork2.ActionSupport;


public class StopScheduler extends UserAwareAction {
	
	String message;

	public String execute() {
		if(!getUser().isAdmin())
		{
			message = "Only administrators can stop the scheduler";
			return ERROR;
		}
		Scheduler.getInstance().stop();
		return SUCCESS;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
