package pals.dao;

import java.util.List;

import javax.persistence.Query;

import pals.entity.Model;
import pals.entity.User;

public class ModelDAO extends DAO
{
	/**
	 * Returns all the models in the database, just returns the modelId and modelName
	 * @return
	 */
    public List<Model> getAll(User user)
    {
    	String queryString = "SELECT new pals.entity.Model(m.id,m.modelName,m.version) from Model m";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " where experiment.id=:eid";
		}
		else
		{
			queryString += " where experiment = NULL ";
		}
    	Query query = getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("eid", user.getCurrentExperiment().getId());
		}
    	return query.getResultList();
    }
}
