package pals.actions.dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.analysis.AnalysisException;
import pals.entity.Analysable;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.service.CSV2NCDFConversionException;
import pals.service.DataSetService;
import pals.service.DataSetVersionService;
import pals.service.UploadedFileFormatException;


public class UploadDataSetVersion extends UserAwareAction {
	
	private static final Logger log = Logger.getLogger(UploadDataSetVersion.class);

	private File 		dataSetRaw;
	private Integer		dataSetId;
	private String		versionName;
	private	String		description;
	private DataSet		dataSet;
	private DataSetVersion dataSetVersion;
	private Integer dataSetVersionId;
	private File qcPlotFile;
	private String imagePath;
	private String filename;
	private String message;
	
	public String UPLOAD = "upload";
	public String VERIFY = "verify";
	public String CANCEL = "cancel";
	public String ACCEPT = "accept";
	
	public DataSetVersionService dataSetVersionService;
	public DataSetService dataSetService;
	
    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();
	
	public File getDataSetRaw() {
		return dataSetRaw;
	}
	public void setDataSetRaw(File dataSetRaw) {
		this.dataSetRaw = dataSetRaw;
	}
	public Integer getDataSetId() {
		return dataSetId;
	}
	public void setDataSetId(Integer dataSetId) {
		this.dataSetId = dataSetId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public DataSet getDataSet() {
		if (dataSet == null) {
			dataSet = getUserService().getDataSet(dataSetId);
		}
		return dataSet;
	}
	
	@SkipValidation
	public String upload() {
		return UPLOAD;
	}
	
	public String verify() 
	{
		System.out.println("FILENAME: " + filename);
		if( filename == null || filename.trim().length() <= 0 )
		{
			message = "Please upload a version file before submitting";
			System.out.println("MEssage: " + message);
			return UPLOAD;
		}
		String filePath = Configuration.getInstance().PATH_TO_APP_DATA + "/" + getUser().getUsername() + "/" + getFilename();
		dataSetRaw = new File(filePath);
		
		if (getDataSetRaw() != null && getDataSetId() != null && getVersionName() != null && getDescription() != null) {
			log.debug("Verifying file: " + filename);
			log.debug("About to retrieve data set id:" + getDataSetId());
			DataSet dataSet = getUserService().getDataSet(getDataSetId());
			log.debug("Retrieved data set");
			if( dataSetService.existsVersionName(getVersionName(), dataSet) )
			{
				addFieldError("versionName","The name " + getVersionName() + " already exists, please choose another");
				return UPLOAD;
			}
			try 
			{
				log.debug("About to upload new data set version");
				dataSetVersion = getUserService().newDataSetVersion(getUser(), dataSet, getDataSetRaw(), 
			        getVersionName(), getDescription(), filename, uploads, uploadFileNames, uploadContentTypes);
				log.debug("Succesfully uploaded new data set version");
				// now we generate the verification plot for the version
				qcPlotFile = dataSetVersion.generateQCPlots();
				imagePath = "../User/FileActionPNG.action?username=" +
				    getUser().getUsername() + "&filename=" + qcPlotFile.getName();
				dataSetVersionService.empiricalBenchmarks(dataSetVersion,getUser());
			} 
			catch (CSV2NCDFConversionException e) 
			{
				//System.out.println("OOMA error is " + e.getMessage());
				String msg = "Invalid fluxnet data: error when converting to NCdf: " + e.getMessage();
				log.error(msg);
				//addFieldError("dataSetRaw", msg);
				message = msg;
				return UPLOAD;
			} 
			catch (IOException e) 
			{
				String msg = "Failed to make a backup of the uploaded file";
				log.error(msg + e.getMessage());
				addFieldError("dataSetRaw", msg);
				message = msg;
				return UPLOAD;
			} 
			catch (UploadedFileFormatException e) 
			{
				String msg = "Uploaded file failed validation: " + e.getMessage();
				log.error(msg);
				addFieldError("dataSetRaw", msg);
				message = msg;
				return UPLOAD;
			} 
			catch (AnalysisException e) 
			{
				log.error(e.getMessage());
				addFieldError("dataSetRaw", e.getMessage());
				message = e.getMessage();
				return UPLOAD;
			} 
			catch (InterruptedException e) 
			{
				log.error(e.getMessage());
				addFieldError("dataSetRaw", e.getMessage());
				message = e.getMessage();
				return UPLOAD;
			}
				//File newDataSetNCDF = 
			//getUserService().convertDatasetNCDF2CSV(getDataSetRaw(), newDataSetNCDF);
			return VERIFY;
		}
		addFieldError("dataSetRaw", "Please provide a file to upload");
		return UPLOAD;
	}
	
	@SkipValidation
	public String back() {
		log.debug("Removing data set version: " + getDataSetVersionId());
		dataSetVersionService.delete(getDataSetVersionId());
		return UPLOAD;
	}
	
	@SkipValidation
	public String cancel() {
		log.debug("Removing data set version: " + getDataSetVersionId());
		dataSetVersionService.delete(getDataSetVersionId());
		return CANCEL;
	}
	
	@SkipValidation
    public String accept()
    {
		log.debug("Updating DataSetVersion: " + getDataSetVersionId());
		dataSetVersion = dataSetVersionService.get(getDataSetVersionId());
        dataSetVersion.setStatus(Analysable.STATUS_PREPARED);
        dataSet = dataSetService.get(getDataSetId());
        if( dataSet.getLatestVersion() == null )
        {
        	dataSetVersion.setIsPublic(true);
        }
        dataSetVersionService.update(dataSetVersion);
        if( dataSet.getLatestVersion() == null )
        {
            dataSet.setLatestVersion(dataSetVersion);
        }
        dataSetService.update(dataSet);
        return ACCEPT;
    }
	public DataSetVersion getDataSetVersion() {
		return dataSetVersion;
	}
	public void setDataSetVersion(DataSetVersion dataSetVersion) {
		this.dataSetVersion = dataSetVersion;
	}
	public File getQcPlotFile() {
		return qcPlotFile;
	}
	public void setQcPlotFile(File qcPlotFile) {
		this.qcPlotFile = qcPlotFile;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}
	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}
	public Integer getDataSetVersionId() {
		return dataSetVersionId;
	}
	public void setDataSetVersionId(Integer dataSetVersionId) {
		this.dataSetVersionId = dataSetVersionId;
	}
	public DataSetService getDataSetService() {
		return dataSetService;
	}
	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
    public List<File> getUpload() {
        return this.uploads;
    }
    public void setUpload(List<File> uploads) {
        this.uploads = uploads;
    }
    public List<String> getUploadFileName() {
        return this.uploadFileNames;
    }
    public void setUploadFileName(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }
    public List<String> getUploadContentType() {
        return this.uploadContentTypes;
    }
    public void setUploadContentType(List<String> contentTypes) {
        this.uploadContentTypes = contentTypes;
    }
}
