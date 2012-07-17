/**
 * 
 */
package pals.actions.dataset;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import pals.Configuration;
import pals.actions.UserAwareAction;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Experiment;
import pals.service.DataSetService;
import pals.service.ExperimentService;
import pals.utils.HTMLUtil;


/**
 *
 */
public class ViewDataSet extends UserAwareAction {

	private Logger log = Logger.getLogger(ViewDataSet.class);
	
	private Integer maxWordLength = 35;
	
	private Integer id;
	private DataSet dataSet;
	private Integer dataSetVersionId;
	
	public String VIEW = "view";
	private String LATEST = "latest";
	private String COPY_TO_EXPERIMENT = "experiment";
	
	public DataSetService dataSetService;
	public ExperimentService experimentService;
	
	private Integer experimentId;
	
	String message;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer dataSetId) {
		this.id = dataSetId;
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	public String view() {
		setDataSet(getUserService().getDataSet(getId()));
		return VIEW;
	}
	
	public List<DataSetVersion> getDataSetVersions() {
		Boolean publicOnly = true;
		if( getDataSet().getOwner().equals(getUser()) ) publicOnly = false;
		return dataSetService.getDataSetVersions(getDataSet().getId(),publicOnly);
	}
	
	public String getGmapsKey() {
		return Configuration.getInstance().GMAPS_KEY;
	}
	
	public Collection<String> getCommentsParas() {
		return HTMLUtil.getParagraphsFromString(dataSet.getComments());
	}
	
	public Collection<String> getRefsParas() {
		return HTMLUtil.getParagraphsFromString(dataSet.getRefs());
	}
	public DataSetService getDataSetService() {
		return dataSetService;
	}
	public void setDataSetService(DataSetService dataSetService) {
		this.dataSetService = dataSetService;
	}
	
	@SkipValidation
	public String latest()
	{
		setDataSet(getUserService().getDataSet(getId()));
		if( getUser().equals(getDataSet().getOwner()) )
		{
		    dataSetService.setLatestVersion(getDataSetVersionId(),getId());
		}
	    return LATEST;
	}
	public Integer getDataSetVersionId() {
		return dataSetVersionId;
	}
	public void setDataSetVersionId(Integer dataSetVersionId) {
		this.dataSetVersionId = dataSetVersionId;
	}
	
	public String getWrappedUrl()
	{
		String url = getDataSet().getFullUrl();
		if( url.length() > maxWordLength )
		{
			String wrappedUrl = "";
			int index = 0;
			while( index < url.length() )
			{
				int startIndex = index;
				int endIndex = index + maxWordLength;
				if( endIndex >= url.length() ) endIndex = url.length() - 1;
				String line = url.substring(startIndex,endIndex);
				wrappedUrl += line+"<br>";
			    index = endIndex+1;
			}
			return wrappedUrl;
		}
		else
		{
			return url;
		}
	}
	public List<Experiment> getExperiments() 
	{
		List<Experiment> experiments = experimentService.getMyExperiments(getUser());
		List<Experiment> sharedExperiments = experimentService.getSharedExperiments(getUser());
		for(Experiment e : sharedExperiments )
		{
			experiments.add(e);
		}
		return experiments;
	}
	public ExperimentService getExperimentService() {
		return experimentService;
	}
	public void setExperimentService(ExperimentService experimentService) {
		this.experimentService = experimentService;
	}
	
	public String experiment()
	{
		view();
		if( experimentId != null )
		{
			Experiment experiment = experimentService.getExperiment(experimentId);
			DataSet copy;
			try 
			{
				copy = dataSetService.copy(dataSet,experiment);
				dataSetService.setExperiment(copy, experiment);
				dataSetService.prepared(copy);
				setMessage("Data Set copied to experiment: " + experiment.getName());
			} 
			catch (IOException e) 
			{
				log.error(e.getMessage());
				setMessage("Failed to copy Data Set to experiment: " + experiment.getName());
			}
		}
		return COPY_TO_EXPERIMENT;
	}
	
	public Integer getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(Integer experimentId) {
		this.experimentId = experimentId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
