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

public class DataSetAsFile extends UserAwareAction
{
	static Logger log = Logger.getLogger(DataSetAsFile.class);
	
	Integer id;
	DataSetService dataSetService;
	DataSetVersionService dataSetVersionService;
	List<String> lines;
	String startDate;
	
	public String execute()
	{
		DataSet ds = dataSetService.get(id);
		DataSetVersion dsv = ds.getLatestVersion();
		if( !dsv.getIsPublic() ) throw new SecurityException("This data set version is not public");
		try {
			lines = dataSetVersionService.fixTimeCSV(dsv.uploadedFilePath(),startDate);
		} catch (IOException e) {
			log.error(e);
		} catch (ParseException e) {
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

	public DataSetService getDataSetService() {
		return dataSetService;
	}

	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public DataSetVersionService getDataSetVersionService() {
		return dataSetVersionService;
	}

	public void setDataSetVersionService(DataSetVersionService dataSetVersionService) {
		this.dataSetVersionService = dataSetVersionService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}
