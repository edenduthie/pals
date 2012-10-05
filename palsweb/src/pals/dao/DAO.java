package pals.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DAO {
	EntityManager em;
	
	private static Logger log = Logger.getLogger(DAO.class);

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
	   this.em = entityManager;
    }
    
    public EntityManager getEntityManager()
    {
    	return em;
    }
    
    public void persist(Object o)
    {
    	em.persist(o);
    }
    
    public void create(Object o)
    {
    	update(o);
    }
    
    public void update(Object o)
    {
    	em.merge(o);
    }
    
    public Object get(String className, String idField, Object id)
    {
    	String queryString = "FROM " + className + " where " + idField +
    	   "=:id";
    	Query query = em.createQuery(queryString);
    	query.setParameter("id",id);
    	return query.getSingleResult();
    }
    
    public Object getNull(String className, String idField, Object id)
    {
    	try
    	{
	    	String queryString = "FROM " + className + " where " + idField +
	    	   "=:id";
	    	Query query = em.createQuery(queryString);
	    	query.setParameter("id",id);
	    	return query.getSingleResult();
    	}
    	catch( NoResultException e )
    	{
    		log.debug(e.getMessage());
    		return null;
    	}
    	catch( NonUniqueResultException nure )
    	{
    		log.debug(nure);
    		return null;
    	}
    }
    
    public void remove(Object o)
    {
    	em.remove(o);
    }
    
    public void deleteAll(String className)
    {
    	String queryString = "DELETE FROM " + className;
    	Query query = em.createQuery(queryString);
    	query.executeUpdate();
    }
    
    public List getAll(String className)
    {
    	String queryString = "from " + className;
    	Query query = em.createQuery(queryString);
    	return query.getResultList();
    }
    
    public void delete(String className, String idField, Object id)
    {
    	String queryString = "FROM " + className + " where " + idField +
 	       "=:id";
 	    Query query = em.createQuery(queryString);
 	    query.setParameter("id",id);
 	    try
 	    {
 	        Object result = query.getSingleResult();
 	        em.remove(result);
 	    }
 	    catch( NoResultException nre )
 	    {
 	    	log.debug(nre.getMessage());
 	    }
    }
}
