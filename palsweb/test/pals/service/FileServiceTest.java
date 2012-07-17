package pals.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import pals.BaseTest;
import pals.entity.PalsFile;

public class FileServiceTest extends BaseTest
{
	String filename = "testdata/cbSetup.9.5.1.212.exe";
	String copyFilename = "copy.txt";
	
	@Autowired
	FileService fileService;
	
    @Test
    public void createFile() throws IOException
    {
    	File file = new File(filename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	
    	PalsFile palsFile = fileService.createFile(copy);
    	
    	Assert.assertEquals(palsFile.getData().length(),10314752);
    	Assert.assertFalse(copy.exists());
    	
    	InputStream is = new FileInputStream(file);
    	byte[] buffer = new byte[palsFile.getData().length()];
    	is.read(buffer,0,buffer.length);
    	for(int i=0; i < buffer.length; ++i)
    	{
    		Assert.assertEquals(buffer[i],palsFile.getData().getData()[i]);
    	}
    	is.close();
    }
    
    @Test
    public void saveFile() throws IOException
    {
    	File file = new File(filename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	
    	PalsFile palsFile = fileService.createFile(copy);
    	fileService.save(palsFile);
    	palsFile = fileService.get(palsFile.getId());
    	
    	Assert.assertEquals(palsFile.getData().length(),10314752);
    	Assert.assertFalse(copy.exists());
    	
    	InputStream is = new FileInputStream(file);
    	byte[] buffer = new byte[palsFile.getData().length()];
    	is.read(buffer,0,buffer.length);
    	for(int i=0; i < buffer.length; ++i)
    	{
    		Assert.assertEquals(buffer[i],palsFile.getData().getData()[i]);
    	}
    	is.close();
    	
    	fileService.deleteFile(palsFile);
    }
    
    @Test
    public void copy() throws IOException
    {
    	File file = new File(filename);
    	Assert.assertTrue(file.exists());
    	File copy = new File(copyFilename);
    	FileUtils.copyFile(file, copy);
    	
    	PalsFile palsFile = fileService.createFile(copy);
    	palsFile.setName("name");
    	palsFile.setContentType("ct");
    	PalsFile palsFileCopy = fileService.copy(palsFile);
    	
    	Assert.assertEquals(palsFileCopy,palsFile);
    }
    
    @Test
    public void copyList() throws IOException
    {
    	List<PalsFile> fileList = new ArrayList<PalsFile>();
    	for( int i=0; i < 4; ++i )
    	{
	    	File file = new File(filename);
	    	Assert.assertTrue(file.exists());
	    	File copy = new File(copyFilename);
	    	FileUtils.copyFile(file, copy);
	    	PalsFile palsFile = fileService.createFile(copy);
	    	palsFile.setName("name");
	    	palsFile.setContentType("ct");
	    	fileList.add(palsFile);
    	}
    	
    	List<PalsFile> copyList = fileService.copy(fileList);
    	
    	for(int i=0; i < copyList.size(); ++i )
    	{
    	    Assert.assertEquals(copyList.get(i),fileList.get(i));
    	}
    }
}
