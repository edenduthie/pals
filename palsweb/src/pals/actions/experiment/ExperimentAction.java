package pals.actions.experiment;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import pals.actions.UserAwareAction;
import pals.dao.DAO;
import pals.entity.Experiment;
import pals.entity.User;
import pals.service.ExperimentService;
import pals.service.SecurityException;
import pals.utils.LSMEvalConstants;

public class ExperimentAction extends UserAwareAction
{
    public String LIST = "list";
    public String LIST_SHARED = "list_shared";
    public String FORM = "form";
    public String ADD = "add";
    public String LOAD = "load";
    public String CLOSE = "close";
    public String SHARE = "share";
    public String UPDATE = "update";
    public String DELETE = "delete";
    
    Experiment experiment;
    List<Experiment> myExperiments;
    List<Experiment> sharedExperiments;
    Integer experimentId;
    String experimentName;
    
    DAO dao;
    ExperimentService experimentService;
    
    public static final Logger log = Logger.getLogger(ExperimentAction.class);
    
    @SkipValidation
    public String list()
    {
    	return LIST;
    }
    
    @SkipValidation
    public String list_shared()
    {
    	return LIST_SHARED;
    }
    
    @SkipValidation
    public String form()
    {
    	return FORM;
    }
    
    public String add()
    {
    	log.debug("Creating new experiment: " + experiment.getName());
    	experiment.setOwner(getUser());
    	experiment.setShared(false);
    	experiment.setCreationDate(Calendar.getInstance().getTime());
    	experiment.setLastActivity(Calendar.getInstance().getTime());
    	dao.persist(experiment);
    	return ADD;
    }
    
    @SkipValidation
    public String load()
    {
    	try {
			experimentService.permitted(getUser(), getExperimentId());
		} catch (SecurityException e) {
			// TODO Send a message to the user
			e.printStackTrace();
		}
    	return LOAD;
    }
    
    @SkipValidation
    public String close()
    {
    	User user = getUser();
    	user.setCurrentExperiment(null);
    	User loadedUser = (User) dao.get(User.class.getName(), "username", user.getUsername());
    	loadedUser.setCurrentExperiment(null);
    	dao.update(loadedUser);
    	return CLOSE;
    }
    
    @SkipValidation
    public String update()
    {
    	experimentName = experiment.getName();
    	if( experimentName.length() > 20 ||
    			!experimentName.matches("^[a-zA-Z0-9_. ]*$") )
    	{
    		return UPDATE;
    	}
    	experiment = experimentService.getExperiment(experimentId);
    	experiment.setName(experimentName);
    	experimentService.update(experiment);
    	return UPDATE;
    }
    
    @SkipValidation
    public String delete()
    {
    	experimentService.delete(experimentId);
    	getUser().setCurrentExperiment(null);
    	return DELETE;
    }
    
    public List<Experiment> getMyExperiments()
    {
    	if( myExperiments == null )
    	{
    	    log.debug("Searching for experemets with user: " + getUser().getUsername());
    	    myExperiments = experimentService.getMyExperiments(getUser());
    	    log.debug(myExperiments.size() + " experiments found");
    	}
    	return myExperiments;
    }
    
    public List<Experiment> getSharedExperiments()
    {
    	if( sharedExperiments == null )
    	{
    		sharedExperiments = experimentService.getSharedExperiments(getUser());
    	}
    	return sharedExperiments;
    }
    
    public void setSharedExperiments(List<Experiment> sharedExperiments)
    {
    	this.sharedExperiments = sharedExperiments;
    }
    

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public ExperimentService getExperimentService() {
		return experimentService;
	}

	public void setExperimentService(ExperimentService experimentService) {
		this.experimentService = experimentService;
	}

	public Integer getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(Integer experimentId) {
		this.experimentId = experimentId;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
}
