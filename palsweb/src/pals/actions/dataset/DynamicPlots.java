package pals.actions.dataset;

import java.util.List;

import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.service.DataSetService;

public class DynamicPlots extends UserAwareAction
{
	Integer id;  // the data set id to load
	
	DataSetService dataSetService;
	
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
	
	public List<DataSet> getDataSets()
	{
		return getDataSetService().getDataSets(getUser());
	}

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}
	
	public Integer retrieveId()
	{
		if( id == null )
		{
			List<DataSet> dataSets = getDataSets();
			if( dataSets != null && dataSets.size() > 0 )
			{
				return dataSets.get(0).getId();
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
	
	public DataSet getDataSet()
	{
		return dataSetService.get(retrieveId());
	}
}
