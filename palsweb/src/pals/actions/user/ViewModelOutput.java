package pals.actions.user;

import java.util.Iterator;
import java.util.List;

import pals.actions.UserAwareAction;
import pals.analysis.NetcdfUtil;
import pals.entity.Analysable;
import pals.entity.Analysis;
import pals.entity.ModelOutput;
import pals.service.ModelOutputService;
import pals.service.SecurityException;
import pals.utils.PalsFileUtils;



public class ViewModelOutput extends UserAwareAction {

	private Integer modelOutputId;
	private ModelOutput modelOutput;
	private ModelOutputService modelOutputService;
	private String message;

	public Integer getModelOutputId() {
		return modelOutputId;
	}

	public void setModelOutputId(Integer modelOutputId) {
		this.modelOutputId = modelOutputId;
	}
	
	public ModelOutput getModelOutput() throws SecurityException {
		if (modelOutput == null) {
			modelOutput = getModelOutputService().getModelOutput(getUser(), getModelOutputId());
		}
		return modelOutput;
	}
	
	public String getTopOfFile() throws SecurityException {
		return NetcdfUtil.getHeaderDesc(PalsFileUtils.getModelOutputFilePath(getModelOutput()));
	}
	
	public List<Analysis> getAnalysisRuns() {
		return getUserService().getAnalysisRuns(getUser(), getModelOutputId());
	}
	
	public List<Analysis> getAnalysisRunsInError() {
		List<Analysis> errorRuns = getAnalysisRuns();
		Analysis ar;
		Iterator<Analysis> iter = errorRuns.iterator();
		while(iter.hasNext()) {
			ar = iter.next();
			if (ar.getStatus() != Analysis.STATUS_ERROR) {
				errorRuns.remove(ar);
			}
		}
		return errorRuns;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean refresh() throws SecurityException
	{
		modelOutput = getModelOutput();
		if( modelOutput == null ) return false;
		if( modelOutput.getStatus() == Analysable.STATUS_NEW ||
		    modelOutput.getStatus() == Analysable.STATUS_PREPARED || 
		    modelOutput.getStatus() == Analysable.STATUS_ANALYSIS )
		{
			return true;
		}
		return false;
	}
	
	public boolean complete()
	{
		if( message != null && message.length() > 0 &&
			modelOutput != null && modelOutput.getStatus() == Analysable.STATUS_COMPLETE )
			return true;
		else return false;
	}
}
