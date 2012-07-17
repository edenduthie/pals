package pals.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.dialect.lock.UpdateLockingStrategy;
import org.springframework.transaction.annotation.Transactional;

import pals.dao.DAO;
import pals.dao.UserDAO;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.entity.Experimentable;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.User;

@Transactional
public class ExperimentService 
{
    private DAO dao;
    private UserDAO userDAO;
    private DataSetService dataSetService;
    private ModelOutputService modelOutputService;
    private ModelService modelService;
    
	private static Logger log = Logger.getLogger(ExperimentService.class);
    
    public Experiment getExperiment(Integer id)
    {
    	return (Experiment) dao.get(Experiment.class.getName(), "id", id);
    }

    @SuppressWarnings("unchecked")
	public List<Experiment> getMyExperiments(User user)
    {
    	String queryString = "from Experiment e where e.owner.username=:username";
    	Query query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("username",user.getUsername());
    	List<Experiment> experiments = query.getResultList();
    	for(Experiment experiment : experiments)
    	{
    		experiment.setNumDataSets(getNumDataSets(experiment));
    	}
    	return experiments;
    }
    
    public void remove(Experiment experiment)
    {
    	removeCurrentExperimentFromUsers(experiment);
//    	Experiment result = (Experiment) dao.get(Experiment.class.getName(), "id", experiment.getId());
//    	dao.remove(result);
    	String queryString = "delete from Experiment where id=:experimentId";
    	Query query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("experimentId",experiment.getId());
    	query.executeUpdate();
    }
    
    public List<Experiment> getSharedExperiments(User user)
    {
    	String queryString = "from User u inner join fetch u.experiments where u.username=:username";
    	Query query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("username", user.getUsername());
    	@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
    	List<Experiment> experiments = new ArrayList<Experiment>();
    	for( User userSingleE : users )
    	{
    		Object[] userExperiments = userSingleE.getExperiments().toArray();
    		for( Object o : userExperiments )
    		{
    			Experiment e = (Experiment) o;
    			boolean have = false;
    			for( Experiment currentE : experiments )
    			{
    				if( currentE.getId() == e.getId()) have = true;
    			}
    			if( !have ) experiments.add(e);
    		}
    	}
    	
    	// get experiments shared with all
    	queryString = "from Experiment where shareWithAll=:shareWithAll";
    	query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("shareWithAll", true);
    	@SuppressWarnings("unchecked")
		List<Object> experimentsSharedWithAll = query.getResultList();
    	for( Object o : experimentsSharedWithAll )
    	{
    		experiments.add((Experiment) o);
    	}

    	for(Experiment experiment : experiments)
    	{
    		experiment.setNumDataSets(getNumDataSets(experiment));
    	}
    	
    	return experiments;
    }
    
    public void permitted(User user, Integer experimentId) throws SecurityException
    {
    	Experiment experiment = (Experiment) dao.get(Experiment.class.getName(), "id", experimentId);

    	if( experiment.getOwner().equals(user) || experiment.userAllowed(user) )
    	{
    		user.setCurrentExperiment(experiment);
        	User loadedUser = (User) dao.get(User.class.getName(), "username", user.getUsername());
        	loadedUser.setCurrentExperiment(experiment);
        	dao.update(loadedUser);
    	}
    	else
    	{
    		throw new SecurityException("You do not have permission to view the experiment: " +
    		    experiment.getName());
    	}

    }
    
    public void setTheseUsersAsShared(List<String> usernames, Experiment experiment)
    {
    	Set<User> newSharedList = new HashSet<User>();
    	
    	for( String username : usernames )
    	{
    		if( !username.equals("false") )
    		{
    		    User user = userDAO.getUser(username);
    		    newSharedList.add(user);
    		}
    	}
    	
    	experiment.setSharedList(newSharedList);
    	update(experiment);
    }
    
    public void update(Experiment experiment)
    {
    	updateActivity(experiment);
    	dao.update(experiment);
    }

    public void delete(Integer experimentId)
    {
    	Experiment experiment = getExperiment(experimentId);
    	clearSharedList(experiment);
    	removeExperimentFromCopiedTo(experiment);
    	for( Experimentable e : experiment.getExperimentables() )
    	{
            log.info("Deleting experimentable: " + e.getId());
    		if( e instanceof DataSet )
    		{
    			DataSet dataSet = (DataSet) e;
    			dataSetService.delete(dataSet);
    		}
    		if( e instanceof ModelOutput )
    		{
    			ModelOutput mo = (ModelOutput) e;
    			modelOutputService.deleteModelOutput(mo);
    		}
    		if( e instanceof Model )
    		{
    			Model model = (Model) e;
    			modelService.delete(model);
    		}
    	}
    	
    	experiment = getExperiment(experimentId);
    	for( Experimentable e : experiment.getExperimentables() )
    	{
    		log.info("NOT DELETED: " + e.getId());
    	}
    	
    	remove(experiment);
    }
    
    public void clearSharedList(Experiment experiment)
    {
    	List<User> userList = new ArrayList<User>();
    	for( User user : experiment.getSharedList() )
    	{
    		userList.add(user);
    	}
    	for( User user : userList )
    	{
    		if( user.getExperiments() != null )
    		{
    		    user.getExperiments().remove(experiment);
    		    dao.update(user);
    		    dao.getEntityManager().flush();
    		}
    	}
    	experiment.getSharedList().clear();
    	dao.update(experiment);
    	updateActivity(experiment);
    	dao.getEntityManager().flush();
    }
    
    public void removeCurrentExperimentFromUsers(Experiment experiment)
    {
    	String queryString = "update User u set currentExperiment=null where currentExperiment.id=:experimentId";
    	Query query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("experimentId",experiment.getId());
    	query.executeUpdate();
    }
    
    public void removeCurrentExperimentFromExperimentables(Experiment experiment)
    {
    	String queryString = "from Experimentable where experiment.id=:experimentId";
    	Query query = dao.getEntityManager().createQuery(queryString);
    	query.setParameter("experimentId",experiment.getId());
    	List<Experimentable> experimentables = query.getResultList();
    	for( Experimentable e : experimentables )
    	{
    		e.setExperiment(null);
    		dao.getEntityManager().merge(e);
    	}
    }
    
    public void removeExperimentFromCopiedTo(Experiment experiment)
    {
    	for( Experimentable experimentable : experiment.getExperimentable() )
    	{
    		List<Experiment> newCopiedToList = new ArrayList<Experiment>();
    		for( Experiment experimentCopiedTo : experimentable.getCopiedTo() )
    		{
    			if( experiment.getId() != experimentCopiedTo.getId() )
    			{
    				newCopiedToList.add(experiment);
    			}
    		}
    		experimentable.setCopiedTo(newCopiedToList);
    		dao.update(experimentable);
    		dao.getEntityManager().flush();
    	}
    	experiment.setExperimentable(new ArrayList<Experimentable>());
    	dao.update(experiment);
    	dao.getEntityManager().flush();
    }
    
	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
	
	public int getNumUsers(Experiment experiment)
	{
		return experiment.getSharedList().size();
	}
	
	public int getNumDataSets(Experiment experiment)
	{
		String queryString = "SELECT COUNT(ds.id) from DataSet ds where experiment.id=:id";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("id",experiment.getId());
		Number result = (Number) query.getSingleResult();
		return result.intValue();
	}
	
	public void updateActivity(Integer experimentId)
	{
		Experiment experiment = get(experimentId);
		updateActivity(experiment);
	}
	
	public void updateActivity(Experiment experiment)
	{
		experiment.updateActivity();
		dao.update(experiment);
	}
	
	public Experiment get(Integer id)
	{
		return (Experiment) dao.get(Experiment.class.getName(),"id",id);
	}
}
