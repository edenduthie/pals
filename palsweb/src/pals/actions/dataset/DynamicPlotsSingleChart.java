package pals.actions.dataset;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.service.DataSetService;
import pals.service.DataSetVersionService;

public class DynamicPlotsSingleChart extends UserAwareAction
{
	Integer id;  // the data set id to load
	
	DataSetService dataSetService;
	DataSetVersionService dataSetVersionService;
	
	public static final Logger log = Logger.getLogger(DynamicPlotsSingleChart.class);
	
	String firstDate;
	String lastDate;
	
	boolean[] availableColumns;
	
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
	
	public String getFirstDate()
	{
		if( firstDate != null ) return firstDate;
		DataSet ds = dataSetService.get(retrieveId());
		DataSetVersion dsv = ds.getLatestVersion();
		try {
			firstDate =  dataSetVersionService.getFirstDate(dsv.uploadedFilePath());
		} catch (IOException e) {
			log.error(e);
		} catch (ParseException e) {
			log.error(e);
		}
		return firstDate;
	}
	public String getLastDate()
	{
		if( lastDate != null ) return lastDate;
		DataSet ds = dataSetService.get(retrieveId());
		DataSetVersion dsv = ds.getLatestVersion();
		try {
			lastDate =  dataSetVersionService.getLastDate(dsv.uploadedFilePath());
		} catch (IOException e) {
			log.error(e);
		} catch (ParseException e) {
			log.error(e);
		}
		return lastDate;
	}

	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(
			DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}
	
	public boolean dataAvailableForColumn(int columnIndex)
	{
		DataSet ds = dataSetService.get(retrieveId());
		DataSetVersion dsv = ds.getLatestVersion();
		try 
		{
			if( availableColumns == null )
			{
				availableColumns = dataSetVersionService.dataAvailableForColumns(dsv.uploadedFilePath());
			}
			return availableColumns[columnIndex];
		} 
		catch (IOException e) 
		{
			log.error(e);
			return true;
		}
		
	}
}
