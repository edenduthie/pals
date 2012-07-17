package pals.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import pals.entity.Country;

@Transactional
public class CountryDAO extends DAO
{
    
    public List<Country> getAll()
    {
    	Query query = em.createQuery("from Country");
    	List<Country> countries = query.getResultList();
    	return countries;
    }
    
    public void create(Country country)
    {
    	em.persist(country);
    }
    
    public void deleteAll()
    {
    	String queryString = "delete from Country";
    	Query query = em.createQuery(queryString);
    	query.executeUpdate();
    }
}
