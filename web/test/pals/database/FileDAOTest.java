package pals.database;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.PalsFile;
import pals.entity.FileData;

public class FileDAOTest extends BaseTest
{
    @Autowired FileDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    
    @Test
    public void testGetWithData() throws IOException
    {
    	PalsFile photo = new PalsFile();
    	photo.setData(new FileData());
    	photoDAO.put(photo);
    	Assert.assertNotNull(photo.getData().getId());
    	
    	PalsFile result = photoDAO.getWithData(photo.getId());
    	Assert.assertEquals(result.getData().getId(),photo.getData().getId());
    	
        photoDAO.deleteAll();
        photoDataDAO.deleteAll();
    }
}
