package pals.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Generator;
import pals.dto.PalsUserDTO;
import pals.entity.PalsUser;

public class PalsUserDTOTest 
{
    @Test
    public void testConstructor()
    {
    	PalsUser user = Generator.palsUser();
    	PalsUserDTO dto = new PalsUserDTO(user);
    	Assert.assertEquals(dto.getEmail(),user.getEmail());
    }
}
