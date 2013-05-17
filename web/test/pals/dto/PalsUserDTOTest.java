package pals.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Generator;
import pals.entity.PalsUser;
import pals.entity.Workspace;

public class PalsUserDTOTest 
{
    @Test
    public void testConstructor()
    {
    	PalsUser user = Generator.palsUser();
    	Workspace workspace = Generator.workspace();
    	user.setCurrentWorkspace(workspace);
    	
    	PalsUserDTO dto = new PalsUserDTO(user);
    	
    	Assert.assertEquals(dto.getEmail(),user.getEmail());
    	Assert.assertEquals(dto.getCurrentWorkspace().getName(),workspace.getName());
    }
}
