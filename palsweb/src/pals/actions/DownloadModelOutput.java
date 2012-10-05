package pals.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import pals.entity.ModelOutput;
import pals.service.ModelOutputService;
import pals.service.SecurityException;

public class DownloadModelOutput extends UserAwareAction {
	
	private static final Logger log = Logger.getLogger(DownloadModelOutput.class);
	
	private InputStream inputStream;
	private Integer modelOutputId;
	private ModelOutput modelOutput;
	private String downloadContentDisposition;
	
	ModelOutputService modelOutputService;
	
	public String execute()
	{
		try 
		{
			modelOutput = modelOutputService.getModelOutput(getUser(), modelOutputId);
			setDownloadContentDisposition("attachment; filename="+modelOutput.retrieveOutputFileName());
			inputStream = new FileInputStream(modelOutput.retrieveOutputFilePath());
		} 
		catch (SecurityException e) 
		{
			log.error("User does not have permission to view mo file: " + modelOutputId);
			log.error(e);
		} 
		catch (FileNotFoundException e) 
		{
			log.error("Failed to find mo file: " + modelOutputId);
			log.error(e);
		}
		return SUCCESS;
	}
	
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public Integer getModelOutputId() {
		return modelOutputId;
	}
	public void setModelOutputId(Integer modelOutputId) {
		this.modelOutputId = modelOutputId;
	}


	public String getDownloadContentDisposition() {
		return downloadContentDisposition;
	}


	public void setDownloadContentDisposition(String downloadContentDisposition) {
		this.downloadContentDisposition = downloadContentDisposition;
	}


	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}


	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}
	
	public String getContentDisposition() {
		return getDownloadContentDisposition();
	}

}
