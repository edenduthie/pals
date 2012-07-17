package pals.actions.analysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.service.ModelOutputService;

public class DynamicPlots extends UserAwareAction
{
	Integer id;  // the data set id to load
	
	ModelOutputService modelOutputService;
	
	List<ModelOutput> modelOutputs;
	
	public static final Logger log = Logger.getLogger(DynamicPlots.class);
	
	String firstDate;
	String lastDate;
	
    public String execute()
    {
    	return SUCCESS;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public List<ModelOutput> getModelOutputs()
	{
		if( modelOutputs == null )
		    modelOutputs =  getModelOutputService().getPublicModelOutputs(getUser());
		return modelOutputs;
	}
	
	public Integer retrieveId()
	{
		if( id == null )
		{
			List<ModelOutput> modelOutputs = getModelOutputs();
			if( modelOutputs != null && modelOutputs.size() > 0 )
			{
				return modelOutputs.get(0).getId();
			}
			else
			{
				return 1;
			}
		}
		else
		{
			return id;
		}
	}
	
	public ModelOutput getModelOutput()
	{
		return modelOutputService.get(retrieveId());
	}
	
	public String getFirstDate()
	{
		if( firstDate != null ) return firstDate;
		ModelOutput mo = modelOutputService.get(retrieveId());
		try {
			firstDate =  modelOutputService.getFirstDate(mo);
		} catch (IOException e) {
			log.error(e);
		}
		return firstDate;
	}
	public String getLastDate()
	{
		if( lastDate != null ) return lastDate;
		ModelOutput mo = modelOutputService.get(retrieveId());
		try {
			lastDate =  modelOutputService.getLastDate(mo);
		} catch (IOException e) {
			log.error(e);
		}
		return lastDate;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}
}
