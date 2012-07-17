package pals.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import pals.entity.Institution;
import pals.entity.Photo;
import pals.entity.User;

@Transactional
public class UserDAO extends DAO
{
    public User create(User user)
    {
    	Institution institution = null;
        institution = (Institution) getNull(Institution.class.getName(),
    	    "name",user.getInstitution().getName());
    	if( institution != null ) user.setInstitution(institution);
    	if( user.getPhoto() != null && user.getPhoto().getId() != null )
    	{
    		Photo photo = (Photo) get(Photo.class.getName(),"id",user.getPhoto().getId());
    		user.setPhoto(photo);
    	}
    	getEntityManager().persist(user);
        return user;
    }
    
	public boolean usernameExists(String username)
	{
		String queryString = "SELECT username from User where username = :username";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("username", username);
		List resultList =  query.getResultList();
		if( resultList.size() <= 0 ) return false;
		else return true;
	}
	
	public void delete(User user)
	{
		String queryString = "DELETE FROM User u WHERE u.username=:username";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("username",user.getUsername());
		query.executeUpdate();
	}
	
	public void update(User user)
	{
    	Institution institution = null;
    	if( user.getInstitution() != null && user.getInstitution().getName() != null )
    	{
            institution = (Institution) getNull(Institution.class.getName(),
    	        "name",user.getInstitution().getName());
    	}
    	if( institution != null ) user.setInstitution(institution);
		getEntityManager().merge(user);
	}
	
	public User getUser(String username)
	{
		String queryString = "from User u where u.username=:username";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("username",username);
		return (User) query.getSingleResult();
	}
}
