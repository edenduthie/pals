package pals.dao;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import pals.entity.DataSet;
import pals.entity.Experiment;

@Transactional
public class DataSetDAO extends DAO
{
	
    public void create(DataSet dataSet)
    {
    	Experiment e = dataSet.getExperiment();
    	if( e != null )
    	{
    		e.updateActivity();
    		update(e);
    	}
    	em.merge(dataSet);
    }
    
    public void deleteAll()
    {
    	String queryString = "DELETE FROM DataSet";
    	Query query = em.createQuery(queryString);
    	query.executeUpdate();
    }
    
    public DataSet get(Integer id)
    {
    	String queryString = "from DataSet where id=:dataSetId";
    	Query query = em.createQuery(queryString);
    	query.setParameter("dataSetId", id);
    	return (DataSet) query.getSingleResult();
    }
}
