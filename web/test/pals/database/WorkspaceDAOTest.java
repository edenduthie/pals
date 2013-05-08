package pals.database;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.entity.PalsUser;
import pals.entity.Workspace;

public class WorkspaceDAOTest extends BaseTest
{
    @Autowired WorkspaceDAO workspaceDAO;
    @Autowired PalsUserDAO palsUserDAO;
    
    @Test
    public void getMyWorkspaces()
    {
    	Workspace workspace = Generator.workspace();
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	workspace.setOwner(user);
    	workspaceDAO.put(workspace);
    	
    	List<Workspace> results = workspaceDAO.getMyWorkspaces(user);
    	Assert.assertEquals(results.size(),1);
    	Assert.assertEquals(results.get(0).getId(),workspace.getId());
    	
    	workspaceDAO.deleteAll();
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void getMyWorkspacesNotFound()
    {
    	Workspace workspace = Generator.workspace();
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	workspaceDAO.put(workspace);
    	
    	List<Workspace> results = workspaceDAO.getMyWorkspaces(user);
    	Assert.assertEquals(results.size(),0);
    	
    	workspaceDAO.deleteAll();
    	palsUserDAO.deleteAll();
    }
    
    @Test
    public void getRootWorkspace()
    {
    	Workspace workspace = Generator.workspace();
    	workspace.setName(Workspace.ROOT_WORKSPACE_NAME);
    	workspaceDAO.put(workspace);
    	
    	Workspace result = workspaceDAO.getRootWorkspace();
    	Assert.assertEquals(result.getId(),workspace.getId());
    	
    	workspaceDAO.deleteAll();
    }
    
    @Test
    public void getWorkspacesIAmInvitedTo()
    {
    	Workspace workspace = Generator.workspace();
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	Set<PalsUser> guests = new HashSet<PalsUser>();
    	guests.add(user);
    	workspace.setGuests(guests);
    	workspaceDAO.put(workspace);
    	
    	Set<Workspace> results = workspaceDAO.getWorkspacesIAmInvitedTo(user);
    	Assert.assertEquals(results.size(),1);
    	
    	workspace.setGuests(new HashSet<PalsUser>());
    	workspaceDAO.update(workspace);
    	workspaceDAO.deleteAll();
    	palsUserDAO.deleteAll();
    }
}
