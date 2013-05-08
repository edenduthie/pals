package pals.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.database.FileDAO;
import pals.database.PhotoDataDAO;
import pals.entity.PalsFile;

public class FileServiceTest extends BaseTest
{
    @Autowired FileService photoService;
    @Autowired FileDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    
    String PalsFilename1 = "testdata/sample-blog-post.jpg";
    int width = 334;
    int height = 334;
    
    @Test
    public void testLoadPhoto() throws IOException
    {
    	System.out.println(System.getProperty("user.dir"));
    	FileInputStream fos = new FileInputStream(new File(PalsFilename1));
        PalsFile photo = photoService.load(fos);
        fos.close();
        Assert.assertEquals(photo.getPicture().length, 14336);
        photoDAO.deleteAll();
        photoDataDAO.deleteAll();
    }
    
    @Test
    public void testResize() throws IOException
    {
    	PalsFile photo = photoService.create(new FileInputStream(new File(PalsFilename1)));
    	photoService.resize(photo, width, height, PalsFile.JPG);
    	Assert.assertEquals(photo.getPicture().length,7974);
    }
}
