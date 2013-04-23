package pals.database;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.PalsGrantedAuthority;

@Service
public class PalsGrantedAuthorityDAO 
{
    @Autowired Database db;
    
    public static final Logger log = Logger.getLogger(PalsGrantedAuthorityDAO.class);
    
	public PalsGrantedAuthority getRoleUser()
	{
		String queryString = "from PalsGrantedAuthority where authority=:roleUser";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("roleUser",PalsGrantedAuthority.ROLE_USER);
		List<PalsGrantedAuthority> results = query.getResultList();
		if( results.size() > 0 ) return results.get(0);
		else 
		{
			log.error("ROLE_USER not found in database, creating a new one");
			PalsGrantedAuthority auth = new PalsGrantedAuthority();
			auth.setAuthority(PalsGrantedAuthority.ROLE_USER);
			db.persist(auth);
			return auth;
		}
	}
	
	public PalsGrantedAuthority getRolePractitioner()
	{
		String queryString = "from PalsGrantedAuthority where authority=:rolePractitioner";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("rolePractitioner",PalsGrantedAuthority.ROLE_PRACTITIONER);
		List<PalsGrantedAuthority> results = query.getResultList();
		if( results.size() > 0 ) return results.get(0);
		else 
		{
			log.error("ROLE_PRACTITIONER not found in database, creating a new one");
			PalsGrantedAuthority auth = new PalsGrantedAuthority();
			auth.setAuthority(PalsGrantedAuthority.ROLE_PRACTITIONER);
			db.persist(auth);
			return auth;
		}
	}
	
	public void put(PalsGrantedAuthority auth)
	{
		db.persist(auth);
	}
	
	public void deleteAll()
	{
		db.deleteAll(PalsGrantedAuthority.class);
	}
}
