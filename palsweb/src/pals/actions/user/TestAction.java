package pals.actions.user;

import java.util.Map;

import pals.actions.UserAwareAction;


/***
 * Useful for testing various Struts 2 action behaviors
 *
 */
public class TestAction extends UserAwareAction {
	
	private String frogName;

	public String getFrogName() {
		return frogName;
	}

	public void setFrogName(String frogName) {
		this.frogName = frogName;
	}
	
	

}
