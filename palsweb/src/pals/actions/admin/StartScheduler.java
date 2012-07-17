package pals.actions.admin;



import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.analysis.Scheduler;
import pals.service.AnalysisServiceInterface;


public class StartScheduler extends UserAwareAction {

	private AnalysisServiceInterface analysisService;
	
	String message;
	static Logger log = Logger.getLogger(StartScheduler.class);
	
	public AnalysisServiceInterface getAnalysisService() {
		return analysisService;
	}

	public void setAnalysisService(AnalysisServiceInterface analService) {
		this.analysisService = analService;
	}

	public String execute() {
		if(!getUser().isAdmin())
		{
			message = "Only administrators can start the scheduler";
			return ERROR;
		}
		Scheduler.getInstance().setAnalysisService(analysisService);
		Thread t = new Thread(Scheduler.getInstance());
		t.start();
		return SUCCESS;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
