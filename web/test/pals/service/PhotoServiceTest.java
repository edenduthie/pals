package pals.service;

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
import pals.service.PhotoService;

public class PhotoServiceTest extends BaseTest
{
    @Autowired PhotoService photoService;
    @Autowired PhotoDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    
    String filename1 = "testdata/sample-blog-post.jpg";
    int width = 334;
    int height = 334;
    
    @Test
    public void testLoadPhoto() throws IOException
    {
    	System.out.println(System.getProperty("user.dir"));
    	FileInputStream fos = new FileInputStream(new File(filename1));
        Photo photo = photoService.load(fos);
        fos.close();
        Assert.assertEquals(photo.getPicture().length, 14336);
        photoDAO.deleteAll();
        photoDataDAO.deleteAll();
    }
    
    @Test
    public void testResize() throws IOException
    {
    	Photo photo = photoService.create(new FileInputStream(new File(filename1)));
    	photoService.resize(photo, width, height, Photo.JPG);
    	Assert.assertEquals(photo.getPicture().length,7974);
    }
}
