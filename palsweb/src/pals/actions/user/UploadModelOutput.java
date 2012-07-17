package pals.actions.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.analysis.NetcdfUtil;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.service.DataSetService;
import pals.service.DataSetVersionService;
import pals.service.InvalidInputException;
import pals.service.ModelOutputService;
import pals.service.ModelService;


public class UploadModelOutput extends UserAwareAction {

	File modelOutputRaw;
	String modelOutputRawFileName;
	String modelOutputName;
	Integer modelId;
	Integer dataSetVersionId;
	
	String stateSelection;
	String parameterSelection;
	String userComments;
	
	List<String> stateSelections;
	List<String> parameterSelections;
	
    ModelOutputService modelOutputService;
    DataSetVersionService dataSetVersionService;
    ModelService modelService;
    DataSetService dataSetService;
    String filename;
    
    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();
    
    ModelOutput modelOutput;
    String message;
	
	public File getModelOutputRaw() {
		return modelOutputRaw;
	}

	public void setModelOutputRaw(File modelOutputRaw) {
		this.modelOutputRaw = modelOutputRaw;
	}

	public String getModelOutputRawFileName() {
		return modelOutputRawFileName;
	}

	public void setModelOutputRawFileName(String modelOutputRawFileName) {
		this.modelOutputRawFileName = modelOutputRawFileName;
	}

	public String getModelOutputName() {
		return modelOutputName;
	}

	public void setModelOutputName(String modelOutputName) {
		this.modelOutputName = modelOutputName;
	}
	
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Integer getDataSetVersionId() {
		return dataSetVersionId;
	}

	public void setDataSetVersionId(Integer dataSetVersionId) {
		this.dataSetVersionId = dataSetVersionId;
	}
	
	public String getStateSelection() {
		return stateSelection;
	}

	public void setStateSelection(String stateSelection) {
		this.stateSelection = stateSelection;
	}

	public String getParameterSelection() {
		return parameterSelection;
	}

	public void setParameterSelection(String parameterSelection) {
		this.parameterSelection = parameterSelection;
	}

	public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public Collection<Model> getModels() {
		return modelService.getModels(getUser());
	}
	
	public Collection<DataSetVersion> getDataSetVersions() {
		return getDataSetService().getDataSetVersions(getUser());
	}
	
	public String execute() {
		if( filename == null || filename.trim().length() <= 0 )
		{
			message = "Please upload a model output file before submitting";
			return INPUT;
		}
		String filePath = Configuration.getInstance().PATH_TO_APP_DATA + "/" + getUser().getUsername() + "/" + getFilename();
		modelOutputRaw = new File(filePath);
		if( getModelOutputRaw() == null )
		{
			message = "Please select a file to upload";
			return INPUT;
		}
		try
		{
			if( getDataSetVersionId() == null )
			{
				message = "A model output must be associated with a Data Set containing at least one public version. Please upload a data set version before uploading the model output";
				return INPUT;
			}
		    DataSetVersion dsv = dataSetVersionService.get(getDataSetVersionId());
		    modelOutput = modelOutputService.newModelOutput(getUser(), getModelOutputRaw(), getModelOutputName(), getModelId(), dsv, getStateSelection(), getParameterSelection(), getUserComments(),
		        getUpload(),getUploadFileName(),getUploadContentType());
		    message = "Model Output Created. Analysis scripts are now running on your data and plots will be available shortly.";
		}
		catch( IOException ioe )
		{
			message = "Failed to upload file: " + ioe.getMessage();
			return INPUT;
		}
		catch( InvalidInputException iie )
		{
			addFieldError("modelOutputName",iie.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}
	
	public boolean performValidation() {
		if (getModelOutputRaw() != null && !NetcdfUtil.check(getModelOutputRaw().getAbsolutePath())) {
			addFieldError("modelOutputRaw", "File must be in NetCDF format.");
			return false;
		}
		return true;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}

	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}

	public List<String> getStateSelections() {
		if( stateSelections == null )
		{
			stateSelections = new ArrayList<String>();
			stateSelections.add("values measured at site");
			stateSelections.add("model spin-up on site data");
			stateSelections.add("default model initialisation");
			stateSelections.add("other");
		}
		return stateSelections;
	}

	public void setStateSelections(List<String> stateSelections) {
        this.stateSelections = stateSelections;
	}

	public List<String> getParameterSelections() {
		if( parameterSelections == null )
		{
			parameterSelections = new ArrayList<String>();
			parameterSelections.add("automated calibration");
			parameterSelections.add("manual calibration");
			parameterSelections.add("no calibration (model default values)");
			parameterSelections.add("other");
		}
		return parameterSelections;
	}

	public void setParameterSelections(List<String> parameterSelections) {
		this.parameterSelections = parameterSelections;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
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

	public ModelOutput getModelOutput() {
		return modelOutput;
	}

	public void setModelOutput(ModelOutput modelOutput) {
		this.modelOutput = modelOutput;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
