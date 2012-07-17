package pals.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Configuration;

public class ImageUtilTest 
{
	String largerImageFilename = "testdata\\largerImage.png";
	String thumbnailFilename = "testdata\\thumbnail_generated.png";
	
	String textWrittenFilename = "testdata\\text_written.png";
	
	@Test
    public void testCreateThumbnail() throws IOException
    {
    	File largerImageFile = new File(largerImageFilename);
    	File thumbnailFile = new File(thumbnailFilename);
    	ImageUtil.createThumbnail(largerImageFile, thumbnailFile);
    	Assert.assertTrue(thumbnailFile.exists());
    	BufferedImage thumbnailImage = ImageIO.read(thumbnailFile);
    	Assert.assertEquals(thumbnailImage.getWidth(),Integer.parseInt(Configuration.getInstance().IMAGE_THUMB_WIDTH));
    	Assert.assertEquals(thumbnailImage.getHeight(),Integer.parseInt(Configuration.getInstance().IMAGE_THUMB_HEIGHT));
    	thumbnailFile.deleteOnExit();
    }
	
	@Test
	public void writeTextOnImage() throws IOException
	{
		File largerImageFile = new File(largerImageFilename);
		InputStream inputStream = new FileInputStream(largerImageFile);
		
		InputStream writtenFile = ImageUtil.writeTextOnImage(inputStream,
				"Benchmarks are not available for this","analysis type on this model output");
		
		File textWrittenFile = new File(textWrittenFilename);
		FileOutputStream out = new FileOutputStream(textWrittenFile);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = writtenFile.read(buffer)) != -1) {
		    out.write(buffer, 0, len);
		}
		
	    textWrittenFile.deleteOnExit();
	}
}
