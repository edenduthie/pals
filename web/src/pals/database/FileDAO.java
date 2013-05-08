package pals.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.PalsFile;

@Service
public class FileDAO 
{
	@Autowired Database db;
	
    public void put(PalsFile photo)
    {
    	db.persist(photo);
    }
    
    public PalsFile get(Integer id)
    {
    	return (PalsFile) db.getNoNull(PalsFile.class.getName(), "id", id);
    }
    
    public PalsFile getWithData(Integer id)
    {
    	String queryString = "from PalsFile p left join fetch p.data where p.id=:id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<PalsFile> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public void update(PalsFile photo)
    {
    	db.update(photo);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(PalsFile.class);
    }

	public List<PalsFile> getAll() 
	{
		return db.getAll(PalsFile.class.getName());
	}

	public void delete(Integer id) 
	{
		db.delete(PalsFile.class.getName(), "id", id);
	}
}
