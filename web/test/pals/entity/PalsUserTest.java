package pals.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Generator;
import pals.dto.PalsUserDTO;

public class PalsUserTest
{
    @Test
    public void constructor()
    {
        PalsUser user = Generator.palsUser();
        Workspace workspace = Generator.workspace();
        user.setCurrentWorkspace(workspace);
        PalsUserDTO dto = new PalsUserDTO(user);
        
        PalsUser constructed = new PalsUser(dto);
        Assert.assertEquals(constructed.getCurrentWorkspace().getName(),workspace.getName());
    }
}
