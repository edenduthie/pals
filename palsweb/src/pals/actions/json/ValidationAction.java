package pals.actions.json;

import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.service.DataSetService;
import pals.service.InvalidInputException;
import pals.service.ModelOutputService;

public class ValidationAction extends UserAwareAction
{
    public String text;
    public String message;
    
    public Integer dataSetId;
    
    ModelOutputService modelOutputService;
    DataSetService dataSetService;
    
    String MODEL_OUTPUT_NAME = "modelOutputName";
    String VERSION_NAME = "versionName";
    
    public String modelOutputName()
    {
    	try
    	{ 
    		modelOutputService.checkNameNotTaken(text);
    	}
    	catch( InvalidInputException e )
    	{
    		message = e.getMessage();
    	}
    	return MODEL_OUTPUT_NAME;
    }
    
    public String versionName()
    {
		DataSet dataSet = getUserService().getDataSet(getDataSetId());
		if( dataSetService.existsVersionName(text, dataSet) )
		    message = "Name already exists";
		return VERSION_NAME;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}

	public Integer getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(Integer dataSetId) {
		this.dataSetId = dataSetId;
	}
}
