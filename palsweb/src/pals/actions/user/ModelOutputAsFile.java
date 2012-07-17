package pals.actions.user;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.service.DataSetService;
import pals.service.DataSetVersionService;
import pals.service.ModelOutputService;
import pals.service.SecurityException;

public class ModelOutputAsFile extends UserAwareAction
{
	static Logger log = Logger.getLogger(ModelOutputAsFile.class);
	
	Integer id;
	ModelOutputService modelOutputService;
	List<String> lines;
	String startDate;
	
	public String execute()
	{
		try 
		{
			lines = modelOutputService.getCSV(id, startDate, Configuration.getInstance().MAX_DYNAMIC_DATA_POINTS);
		} 
		catch (IOException e) {
			log.error(e);
		} catch (SecurityException e) {
			log.error(e);
		}
		return SUCCESS;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}
}
