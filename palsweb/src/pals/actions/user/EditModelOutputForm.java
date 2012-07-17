package pals.actions.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pals.actions.UserAwareAction;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.service.DataSetService;
import pals.service.InvalidInputException;
import pals.service.ModelOutputService;
import pals.service.ModelService;
import pals.service.SecurityException;


/***
 * This action allows a user to edit a <ModelOutput> in various ways.
 * The action performed is determined by the editTask attribute.
 * 
 * @author Stefan Gregory
 *
 */
public class EditModelOutputForm extends UserAwareAction {
	
	public static String MODEL_OUTPUT_EDIT_TASK_DELETE = "delete";
	public static String MODEL_OUTPUT_EDIT_TASK_EDIT = "edit";
	public static String MODEL_OUTPUT_EDIT_TASK_MODIFY = "modify";
	public static String MODEL_OUTPUT_EDIT_TASK_PUBLIC = "public";
	public static String MODEL_OUTPUT_EDIT_TASK_PRIVATE = "private";
	public static String MODEL_OUTPUT_EDIT_TASK_SHARE_DATA_PROVIDER = "sharedataprovider";
	public static String MODEL_OUTPUT_EDIT_TASK_TEST = "test";
	
	String editTask;  // one of the above attributes...
	
	List<Integer> modelOutputId;
	String userErrorMessage;
	
	String modelOutputName;
	Integer modelId;
	Integer dataSetVersionId;
	
	String stateSelection;
	String parameterSelection;
	String userComments;
	
	ModelOutputService modelOutputService;
	ModelService modelService;
	DataSetService dataSetService;
	
	List<String> stateSelections;
	List<String> parameterSelections;
	
	String filesToRemove;    
	
	private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();
	
	public List<Integer> getModelOutputId() {
		return modelOutputId;
	}

	public void setModelOutputId(List<Integer> modelOutputId) {
		this.modelOutputId = modelOutputId;
	}

	public List<ModelOutput> getModelOutputs() throws SecurityException {
		List<ModelOutput> modelOutputs = new LinkedList<ModelOutput>();
		Iterator<Integer> iter = getModelOutputId().iterator();
		while (iter.hasNext()) {
			modelOutputs.add(getModelOutputService().getModelOutput(getUser(), iter.next()));
		}
		return modelOutputs;
	}
	
	public ModelOutput getModelOutput() throws SecurityException {
		return getModelOutputs().get(0);
	}
	
	public String getEditTask() {
		return editTask;
	}

	public void setEditTask(String editTask) {
		this.editTask = editTask;
	}

	public String getUserErrorMessage() {
		return userErrorMessage;
	}

	public void setUserErrorMessage(String userErrorMessage) {
		this.userErrorMessage = userErrorMessage;
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
		try {
			if (getEditTask().equals(MODEL_OUTPUT_EDIT_TASK_DELETE)) {
				return executeDelete();
			} else if (getEditTask().equals(MODEL_OUTPUT_EDIT_TASK_PUBLIC)) {
				return executePublic();
			} else if (getEditTask().equals(MODEL_OUTPUT_EDIT_TASK_SHARE_DATA_PROVIDER)) {
				return executeShareDataProvider();
			} else if (getEditTask().equals(MODEL_OUTPUT_EDIT_TASK_PRIVATE)) {
				return executePrivate();
			} else if (getEditTask().equals(MODEL_OUTPUT_EDIT_TASK_MODIFY)) {
				return executeModify();
			} else {
				setUserErrorMessage("Unknown edit task:" + getEditTask());
				return ERROR;
			}
		} catch (SecurityException e) {
			setUserErrorMessage("SecurityException: " + e.getMessage());
			return ERROR;
		} catch (IOException e) {
			setUserErrorMessage("IOException: " + e.getMessage());
			return ERROR;
		}
		
	}
	
	
	
	

	private String executeShareDataProvider() throws SecurityException 
	{
		List<ModelOutput> modelOutputs = getModelOutputs();
		if (modelOutputs.size() == 0) {
			setUserErrorMessage("No ModelOutputs Selected.");
			return ERROR;
		}
		Iterator<ModelOutput> iter = modelOutputs.iterator();
		while (iter.hasNext()) {
			getUserService().setModelOutputPublic(getUser(), iter.next(), ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER);
		}
		return SUCCESS;
	}

	/**
	 * Delete the selected <ModelOutput>s.
	 * @throws SecurityException 
	 */
	private String executeDelete() throws SecurityException {
		List<ModelOutput> modelOutputs = getModelOutputs();
		if (modelOutputs.size() == 0) {
			setUserErrorMessage("No ModelOutputs Selected.");
			return ERROR;
		}
		for( ModelOutput mo : modelOutputs )
		{
			List<ModelOutput> moSameModelDataSet = modelOutputService.getModelOutputsForModelDataSet(
		        mo.getModel(),mo.getDataSetVersion().getDataSet(),getUser());
			System.out.println("SIZE: " + moSameModelDataSet.size());
			if( moSameModelDataSet.size() <= 1 )
			{
				setUserErrorMessage("You must have at least one non-private Model Output for each Model and Data Set combination");
				return ERROR;
			}
			getModelOutputService().deleteModelOutput(getUser(),mo);
		}
		return SUCCESS;
	}
	
	/**
	 * Make selected <ModelOutput>s publicly accessible.
	 * @throws SecurityException
	 */
	private String executePublic() throws SecurityException {
		List<ModelOutput> modelOutputs = getModelOutputs();
		if (modelOutputs.size() == 0) {
			setUserErrorMessage("No ModelOutputs Selected.");
			return ERROR;
		}
		Iterator<ModelOutput> iter = modelOutputs.iterator();
		while (iter.hasNext()) {
			getUserService().setModelOutputPublic(getUser(), iter.next(), ModelOutput.ACCESS_LEVEL_PUBLIC);
		}
		return SUCCESS;
	}
	
	/**
	 * Make selected <ModelOutput>s only accessible to this user.
	 * Check PALS accessibility rules in the process:
	 * - each user must have at last one public <ModelOutput> for each <Model> they have
	 *   and for each <DataSet> they have run it on
	 *   
	 * @throws SecurityException
	 */
	private String executePrivate() throws SecurityException 
	{
		List<ModelOutput> modelOutputs = getModelOutputs();
		if (modelOutputs.size() == 0) {
			setUserErrorMessage("No ModelOutputs Selected.");
			return ERROR;
		}		

		for( ModelOutput mo : modelOutputs )
		{
			List<ModelOutput> moSameModelDataSet = modelOutputService.getModelOutputsForModelDataSet(
		        mo.getModel(),mo.getDataSetVersion().getDataSet(),getUser());
			if( moSameModelDataSet.size() <= 1 )
			{
				setUserErrorMessage("You must have at least one non-private Model Output for each Model and Data Set combination");
				return ERROR;
			}
	        getUserService().setModelOutputPublic(getUser(), mo, ModelOutput.ACCESS_LEVEL_PRIVATE);
		}
	    return SUCCESS;
	}
	
	/***
	 * Change the attributes of this Model Output to whatever the user has edited them to be. 
	 * The new attribute values will come as request variables. 
	 * 
	 * @throws SecurityException
	 * @throws IOException 
	 */
	private String executeModify() throws SecurityException, IOException {
		try {
			removeFiles();
			getModelOutputService().updateModelOutput(getUser(), getModelOutput(), getModelOutputName(), getModelId(), getDataSetVersionId(), getStateSelection(), getParameterSelection(), getUserComments(),getUpload(),getUploadFileName(),getUploadContentType());
		} catch (InvalidInputException e) {
			addFieldError("modelOutputName","That name already exists, please select a different name");
			return INPUT;
		}
		return SUCCESS;
	}
	
	public void removeFiles() throws SecurityException
	{
		System.out.println("Executing remove Files");
		String[] splitString = filesToRemove.split("\\,");
		List<PalsFile> filesToRemove = new ArrayList<PalsFile>();
		for( String fileIdString : splitString )
		{
			fileIdString = fileIdString.trim();
			if(fileIdString != null && fileIdString.length() > 0)
			{
				Integer fileId = new Integer(fileIdString);
				for( PalsFile file : getModelOutput().getFiles() )
				{
					if( file.getId().equals(fileId) )
					{
						filesToRemove.add(file);
					}
				}
			}
		}
		for( PalsFile file : filesToRemove )
		{
			modelOutputService.removeFile(getModelOutput(),file);
		}
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
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

	public String getFilesToRemove() {
		return filesToRemove;
	}

	public void setFilesToRemove(String filesToRemove) {
		this.filesToRemove = filesToRemove;
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
