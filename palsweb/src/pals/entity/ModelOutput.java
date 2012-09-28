package pals.entity;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import pals.Globals;

/***
 * The result of running a <Model> on a <DataSetVersion> that
 * a <User> then uploads.
 * 
 * (This model run is undertaken by the user with their
 * own computing resource; PALS does not run Models, it runs
 * analysis on the ModelOutputs. )
 * 
 * We rely on the user to correctly identify which <DataSetVersion> they
 * ran their <Model> on, otherwise the analyses results are potentially bogus.
 * 
 * @author Stefan Gregory
 *
 */
@Entity
public class ModelOutput extends Analysable {
	
	private String		userName;
	private Integer		modelId;
	private Integer		dataSetVersionId;
	private Date		uploadDate;
	
	// blob fields
	private String		parameterSelection;
	private String		stateSelection;
	@Column(columnDefinition="TEXT")
	private String		userComments;
	
	private String accessLevel;
	
	public static final String ACCESS_LEVEL_PUBLIC = "PUBLIC";
	public static final String ACCESS_LEVEL_DATA_SET_OWNER = "DATA_SET_OWNER";
	public static final String ACCESS_LEVEL_PRIVATE = "PRIVATE";
	
	@ManyToOne(optional=false)
    @JoinColumn(name="modelId",referencedColumnName="id",insertable=false, updatable=false)
    private Model		model;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="dataSetVersionId",referencedColumnName="id",insertable=false,updatable=false)
	private DataSetVersion		dataSetVersion;
	
	private Boolean allowDownload;
	
	public ModelOutput()
	{
		setStatus(Analysable.STATUS_NEW);
	}
	
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	public Date getDate() {
		return uploadDate;
	}
	public void setDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public void setDataSetVersionId(Integer dataSetVersionId) {
		this.dataSetVersionId = dataSetVersionId;
	}
	public Integer getDataSetVersionId() {
		return dataSetVersionId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUpoadDate(Date upoadDate) {
		this.uploadDate = upoadDate;
	}
	public Date getUpoadDate() {
		return uploadDate;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public Model getModel() {
		return model;
	}
	public void setDataSet(DataSetVersion dataSetVersion) {
		this.dataSetVersion = dataSetVersion;
	}
	public DataSetVersion getDataSetVersion() {
		return dataSetVersion;
	}
	public String getParameterSelection() {
		return parameterSelection;
	}
	public void setParameterSelection(String parameterSelection) {
		this.parameterSelection = parameterSelection;
	}
	public String getStateSelection() {
		return stateSelection;
	}
	public void setStateSelection(String stateSelection) {
		this.stateSelection = stateSelection;
	}
	public String getUserComments() {
		return userComments;
	}
	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}
	
	public Class getInputClass() {
		return ModelOutput.class;
	}
	
	public String toString() {
		return "<ModelOutput:" + getId() + ">";
	}
	
	public String retrieveOutputFilePath()
	{
	    return getOwner().retrieveFilePath() + File.separator + Globals.MODEL_OUTPUT_FILE_PREFIX + 
	        id + Globals.NETCDF_FILE_SUFFIX;
	}
	
	public String retrieveOutputFileName()
	{
	    return Globals.MODEL_OUTPUT_FILE_PREFIX + 
	        id + Globals.NETCDF_FILE_SUFFIX;
	}
	
	public String retrieveOutputFilePathBench() 
	{
	    return getOwner().retrieveFilePath() + File.separator + Globals.BENCH_FILE_PREFIX + 
        id + Globals.NETCDF_FILE_SUFFIX;
	}
	
	@Override
	public String retrieveSiteName()
	{
		return getDataSetVersion().getDisplayName();
	}

	public void setDataSetVersion(DataSetVersion dataSetVersion) {
		this.dataSetVersion = dataSetVersion;
	}
	
	public String retrieveValidAnalysisType() {
	    return AnalysisType.MODEL_OUTPUT_ANALYSIS_TYPE;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if( other instanceof ModelOutput )
		{
			ModelOutput otherModelOutput = (ModelOutput) other;
			if( otherModelOutput.getId().equals(getId()) ) return true;
			else return false;
		}
		else
		{
			return false;
		}
	}

	public Boolean getAllowDownload() {
		return allowDownload;
	}

	public void setAllowDownload(Boolean allowDownload) {
		this.allowDownload = allowDownload;
	}
}
