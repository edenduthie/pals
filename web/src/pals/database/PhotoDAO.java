package pals.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.Photo;

@Service
public class PhotoDAO 
{
	@Autowired Database db;
	
    public void put(Photo photo)
    {
    	db.persist(photo);
    }
    
    public Photo get(Integer id)
    {
    	return (Photo) db.getNoNull(Photo.class.getName(), "id", id);
    }
    
    public Photo getWithData(Integer id)
    {
    	String queryString = "from Photo p left join fetch p.data where p.id=:id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<Photo> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public void update(Photo photo)
    {
    	db.update(photo);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Photo.class);
    }

	public List<Photo> getAll() 
	{
		return db.getAll(Photo.class.getName());
	}

	public void delete(Integer id) 
	{
		db.delete(Photo.class.getName(), "id", id);
	}
}
