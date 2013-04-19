package pals.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.PalsUser;

@Service
public class PalsUserDAO 
{
    @Autowired Database db;
    
    public PalsUser get(Integer id)
    {
    	return (PalsUser) db.getNoNull(PalsUser.class.getName(), "id", id);
    }
    
    public PalsUser get(String username)
    {
    	return (PalsUser) db.getNoNull(PalsUser.class.getName(), "email", username);
    }
    
    public void put(PalsUser user)
    {
    	db.persist(user);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(PalsUser.class);
    }
    
    public void update(PalsUser user)
    {
    	db.update(user);
    }
    
    public List<PalsUser> getAll()
    {
    	return db.getAll(PalsUser.class.getName());
    }
}
