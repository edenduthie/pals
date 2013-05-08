package pals.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.FileData;

@Service
public class PhotoDataDAO 
{
	@Autowired Database db;
	
    public void put(FileData photoData)
    {
    	db.persist(photoData);
    }
    
    public FileData get(Integer id)
    {
    	return (FileData) db.getNoNull(FileData.class.getName(), "id", id);
    }
    
    public void update(FileData photoData)
    {
    	db.update(photoData);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(FileData.class);
    }

	public List<FileData> getAll() 
	{
		return db.getAll(FileData.class.getName());
	}
}
