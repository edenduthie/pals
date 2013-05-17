package pals.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.database.WorkspaceDAO;
import pals.entity.PalsUser;
import pals.entity.Workspace;

@Service
public class WorkspaceService 
{
	@Autowired WorkspaceDAO workspaceDAO;
	
	public List<Workspace> getMyWorkspaces(PalsUser user)
	{
		return workspaceDAO.getMyWorkspaces(user);
	}
	
	public Set<Workspace> getWorkspacesIAmInvitedTo(PalsUser user)
	{
		return workspaceDAO.getWorkspacesIAmInvitedTo(user);
	}
	
    public Workspace getRootWorkspace()
    {
    	return workspaceDAO.getRootWorkspace();
    }
}
