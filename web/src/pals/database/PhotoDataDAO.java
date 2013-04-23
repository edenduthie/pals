package pals.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.PhotoData;

@Service
public class PhotoDataDAO 
{
	@Autowired Database db;
	
    public void put(PhotoData photoData)
    {
    	db.persist(photoData);
    }
    
    public PhotoData get(Integer id)
    {
    	return (PhotoData) db.getNoNull(PhotoData.class.getName(), "id", id);
    }
    
    public void update(PhotoData photoData)
    {
    	db.update(photoData);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(PhotoData.class);
    }

	public List<PhotoData> getAll() 
	{
		return db.getAll(PhotoData.class.getName());
	}
}
