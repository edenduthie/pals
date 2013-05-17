package pals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.Generator;
import pals.database.PalsUserDAO;
import pals.database.WorkspaceDAO;
import pals.entity.PalsUser;
import pals.entity.Workspace;

public class WorkspaceServiceTest extends BaseTest
{
    @Autowired WorkspaceDAO workspaceDAO;
    @Autowired PalsUserDAO palsUserDAO;
    @Autowired WorkspaceService workspaceService;
    
    @Test
    public void getMyWorkspaces()
    {
    	Workspace workspace = Generator.workspace();
    	PalsUser user = Generator.palsUser();
    	palsUserDAO.put(user);
    	workspace.setOwner(user);
    	workspaceDAO.put(workspace);
    	
    	List<Workspace> results = workspaceService.getMyWorkspaces(user);
    	Assert.assertEquals(results.size(),1);
    	Assert.assertEquals(results.get(0).getId(),workspace.getId());
    	
    	workspaceDAO.deleteAll();
    	palsUserDAO.deleteAll();
    }
}
