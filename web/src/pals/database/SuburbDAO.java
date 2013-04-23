package pals.database;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.Suburb;

@Service
public class SuburbDAO implements Serializable
{
	private static final long serialVersionUID = 1295188186363456825L;
	
	@Autowired Database db;
    
    public void put(Suburb suburb)
    {
    	db.persist(suburb);
    }
    public Suburb get(Integer id)
    {
    	return (Suburb) db.getNoNull(Suburb.class.getName(), "id", id);
    }
    public void update(Suburb suburb)
    {
    	db.update(suburb);
    }
    public void deleteAll()
    {
    	db.deleteAll(Suburb.class);
    }
    
    public List<String> searchByName(String name)
    {
    	String queryString = "select name from Suburb";
    	if( name != null ) queryString += " where name LIKE :name";
    	queryString +=  " ORDER BY name ASC";
    	Query query = db.getEntityManager().createQuery(queryString);
    	if( name != null ) query.setParameter("name", name.toUpperCase() + "%");
    	return query.getResultList();
    }
    
    public List<Suburb> getAll()
    {
    	return db.getAll(Suburb.class.getName());
    }
}
