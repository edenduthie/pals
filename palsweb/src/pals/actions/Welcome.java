package pals.actions;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class Welcome extends UserAwareAction {

	private static final Logger log = Logger.getLogger(Welcome.class);
	
	public String execute() {	
		log.debug("Executed action: " + getClass().getName());
		return SUCCESS;
	}
	
	public boolean getPublicOnly() {
		return false;
	}

	public Date getLastUpdated() {
		return Calendar.getInstance().getTime();
	}
	
}
