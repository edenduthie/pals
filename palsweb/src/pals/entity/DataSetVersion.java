package pals.entity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import pals.Configuration;
import pals.Globals;
import pals.analysis.AnalysisException;
import pals.utils.PalsFileUtils;

/***
 * Represents the actual data file (NetCDF) of the <DataSet>.
 * Data Sets can be updated, hence we have <DataSetVersion>s.
 * 
 * @author Stefan Gregory
 *
 */
@Entity
public class DataSetVersion extends Analysable {
	
	//private char 		status;
	
	private Integer 	dataSetId;
	private Date		uploadDate;
	@Column(columnDefinition="TEXT")
	private String		description;
	private String 		originalFileName;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="dataSetId",referencedColumnName="id",insertable=false,updatable=false)
	private DataSet		dataSet;
	
	private Boolean isPublic = false;
	
	@Transient
	public File uploadedFile;
	
	private Date startDate;
	private Date endDate;

	public Integer getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(Integer dataSetId) {
		this.dataSetId = dataSetId;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getDescription() {
		return description;
	}
	
	/**
	 * Limit to 35 characters...
	 * @return
	 */
	public String getShortDescription() {
		if( description == null ) return null;
		if (description.length() > 35) 
			return description.substring(0, 32) + "...";
		else
			return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSetId = dataSet.getId();
		this.dataSet = dataSet;
	}
	
	public String getDisplayName() {
		return getDataSet().getName() + "." + getName();
	}
	public String toString() {
		return "<DataSetVersion:" + getId() + ">";
	}
	
	public String uploadedFilePath()
	{
		return
		    Configuration.getInstance().PATH_TO_APP_DATA + File.separator +
		    getDataSet().getOwner().fileDirectory() + File.separator +
		    Globals.DATA_SET_FILE_PREFIX +
		    getDataSet().getId() + "." +
		    getId() + "_orig." + extension();
		    
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	
	public String extension()
	{	
		if( getOriginalFileName() != null )
		{
			String fileName = getOriginalFileName();
			String[] splitString = fileName.split("\\.");
			if( splitString.length > 1 )
			{
				return splitString[splitString.length-1];
			}
		}
		
		return "unknown";
	}
	
	/**
	 * REturns the command line arguments to pass to the R script
	 * @return
	 */
	public String getMetadata()
	{
		if( dataSet != null && dataSet.getMetadata() != null && name != null )
		{
		    return dataSet.getMetadata() + " " + name.replace(' ', Globals.REPLACEMENT_CHAR);
		}
		else
		{
			return null;
		}
	}
	
	public void validateUploadedFile(String filename)
	{
		
	}

	@Override
	public String retrieveOutputFilePath() {
	    return PalsFileUtils.getDataSetVersionFluxFilePath(this);
	}
	
	public String retrieveMetFilePath() {
		return PalsFileUtils.getDataSetVersionMetFilePath(this);
	}
	
	public String retrieveQCPlotsFilePath() {
		return  retrieveQCPlotsFilePathNoExtension() + Globals.PNG_FILE_SUFFIX;
	}
	
	public String retrieveQCPlotsFilePathNoExtension() {
		return PalsFileUtils.getDataSetVersionFilePathWidthSuffix(this, "_qc");
	}
	
	@Override
	public String retrieveSiteName()
	{
		return getDisplayName();
	}
	
	public File generateQCPlots() throws AnalysisException, IOException, InterruptedException
	{
		String command = Configuration.getInstance().QC_PLOTS_COMMAND +
	        " " + retrieveMetFilePath() +
	        " " + retrieveOutputFilePath() +
	        " " + retrieveQCPlotsFilePathNoExtension() +
	        " png" +
	        " " + Configuration.getInstance().QC_PLOTS_WIDTH +
	        " " + Configuration.getInstance().QC_PLOTS_HEIGHT;
		PalsFileUtils.executeCommand(command);	
		File file = new File(retrieveQCPlotsFilePath());
		if( !file.exists() )
		{
			throw new AnalysisException("Failed to generate the qc plots");
		}
	        
		return file;
	}

	public String retrieveFluxFilePath() {
		return PalsFileUtils.getDataSetVersionFluxFilePath(this);
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBenchmarkFile() 
	{
		return Configuration.getInstance().getStringProperty("R_DIR") +
		    getId() + "_dsv.r";
	}

	public String retrieveBenchFilePath() 
	{
		return PalsFileUtils.getDataSetVersionBenchmarkFilePath(this);
	}
}
