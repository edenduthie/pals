package pals.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.Photo;

public class PhotoServiceTest extends BaseTest
{
	String srcFilename = "testdata/largerImage.png";
	String srcFilenameGif = "testdata/gif_test.gif";
	String srcFilenameJpeg = "testdata/jpeg_test.jpg";
	String destFilename = "testdata/largerimage_temp.png";
	
    @Autowired
    PhotoService photoService;
    
    @Test
    public void createPhoto() throws IOException
    {
    	File src = new File(srcFilename);
    	File dest = new File(destFilename);
    	FileUtils.copyFile(src, dest);
    	Photo photo = photoService.createPhoto(dest);
    	Assert.assertNotNull(photo.getFilename());
    	File createdFile = new File(photo.getFilename());
    	Assert.assertTrue(createdFile.exists());
    	createdFile.deleteOnExit();
    }
    
    @Test
    public void createPhotoGif() throws IOException
    {
    	File src = new File(srcFilenameGif);
    	File dest = new File(destFilename);
    	FileUtils.copyFile(src, dest);
    	Photo photo = photoService.createPhoto(dest);
    	Assert.assertNotNull(photo.getFilename());
    	File createdFile = new File(photo.getFilename());
    	Assert.assertTrue(createdFile.exists());
    	createdFile.deleteOnExit();
    }
    
    @Test
    public void createPhotoJpeg() throws IOException
    {
    	File src = new File(srcFilenameJpeg);
    	File dest = new File(destFilename);
    	FileUtils.copyFile(src, dest);
    	Photo photo = photoService.createPhoto(dest);
    	Assert.assertNotNull(photo.getFilename());
    	File createdFile = new File(photo.getFilename());
    	Assert.assertTrue(createdFile.exists());
    	createdFile.deleteOnExit();
    }
}
