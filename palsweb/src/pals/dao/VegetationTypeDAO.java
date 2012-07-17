package pals.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import pals.entity.VegetationType;

/**
 * Class to retrieve and save reference data
 */
@Transactional
public class VegetationTypeDAO extends DAO
{
    public List<VegetationType> getAll()
    {
    	String queryString = "from VegetationType where userAdded = false";
    	Query query = em.createQuery(queryString);
    	return query.getResultList();
    }
    
    public void create(VegetationType object)
    {
    	object.setUserAdded(true);
    	em.persist(object);
    }
    
    public void deleteAll()
    {
    	String queryString = "delete from VegetationType";
    	Query query = em.createQuery(queryString);
    	query.executeUpdate();
    }
}
