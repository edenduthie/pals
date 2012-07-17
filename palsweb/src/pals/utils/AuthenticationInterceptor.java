package pals.utils;

import java.util.Map;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.entity.User;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationInterceptor implements Interceptor {

	static final Logger log = Logger.getLogger(AuthenticationInterceptor.class);
	
	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		log.debug("Authentication intercepted");
		Map session = actionInvocation.getInvocationContext().getSession();
		User user = (User) session.get(LSMEvalConstants.USER);
		if (user == null) {
			log.debug("User object is null");
			Action action = (Action) actionInvocation.getAction();
			if (action instanceof UserAwareAction) {
				log.debug("Action is instance of UserAwareAction");
			    return Action.LOGIN;
			}
			else
			{
				log.debug("Action is not instance of UserAwareAction");
				return actionInvocation.invoke();
			}
		} else {
			log.debug("User found");
			Action action = (Action) actionInvocation.getAction();
			if (action instanceof UserAware) {
				log.debug("Action is instance of UserAwareAction, setting User");
				((UserAware)action).setUser(user);
			}
		}
		return actionInvocation.invoke();
	}

}
