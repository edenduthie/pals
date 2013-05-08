package pals.database;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.PalsUser;
import pals.entity.Workspace;

@Service
public class WorkspaceDAO 
{
    @Autowired Database db;
    
    public Workspace get(Integer id)
    {
    	return (Workspace) db.getNoNull(Workspace.class.getName(), "id", id);
    }
    
    public void put(Workspace workspace)
    {
    	db.persist(workspace);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Workspace.class);
    }
    
    public void update(Workspace workspace)
    {
    	db.update(workspace);
    }
    
    public List<Workspace> getAll()
    {
    	return db.getAll(Workspace.class.getName());
    }
    
    public List<Workspace> getMyWorkspaces(PalsUser user)
    {
    	String queryString = "from Workspace where owner.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",user.getId());
    	return query.getResultList();
    }
    
    public Workspace getRootWorkspace()
    {
    	String queryString = "from Workspace where name = :name";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("name",Workspace.ROOT_WORKSPACE_NAME);
    	List<Workspace> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public Set<Workspace> getWorkspacesIAmInvitedTo(PalsUser user)
    {
    	String queryString = "from PalsUser u left join fetch u.guestWorkspaces w where u.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",user.getId());
    	List<PalsUser> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0).getGuestWorkspaces();
    	else return null;
    }
}
