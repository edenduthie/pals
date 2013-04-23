package pals.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.database.PhotoDAO;
import pals.database.PhotoDataDAO;
import pals.entity.Photo;
import pals.entity.PhotoData;

public class PhotoDAOTest extends BaseTest
{
    @Autowired PhotoDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    
    @Test
    public void testGetWithData() throws IOException
    {
    	Photo photo = new Photo();
    	photo.setData(new PhotoData());
    	photoDAO.put(photo);
    	Assert.assertNotNull(photo.getData().getId());
    	
    	Photo result = photoDAO.getWithData(photo.getId());
    	Assert.assertEquals(result.getData().getId(),photo.getData().getId());
    	
        photoDAO.deleteAll();
        photoDataDAO.deleteAll();
    }
}
