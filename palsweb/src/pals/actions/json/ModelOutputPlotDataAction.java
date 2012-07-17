package pals.actions.json;

import java.util.ArrayList;
import java.util.List;

import pals.actions.UserAwareAction;
import pals.dao.AnalysisDAO;
import pals.entity.Analysis;
import pals.entity.DataSetVersion;
import pals.entity.ModelOutput;
import pals.entity.User;

public class ModelOutputPlotDataAction extends UserAwareAction
{
	private static final long serialVersionUID = -4514638854692081783L;
	public AnalysisDAO analysisDAO;
	public List<Analysis> analysisList;
	public Integer modelId;

	/**
	 * To prepare we load all the analysis objects with DataSetVersion types
	 * @return 
	 */
	public String execute()
	{
		List<Analysis> tempList = analysisDAO.getAnalysisByModelId(getModelId(),Analysis.STATUS_COMPLETE,getUser());
		analysisList = new ArrayList<Analysis>();
		// now remove any model outputs that we do not have permission to see
		for( Analysis analysis : tempList )
		{
			if( analysis.getAnalysable() instanceof ModelOutput )
			{
				ModelOutput modelOutput = (ModelOutput) analysis.getAnalysable();
				if( modelOutput.getAccessLevel().equals(ModelOutput.ACCESS_LEVEL_PUBLIC) )
				{
					analysisList.add(analysis);
				}
				else if( modelOutput.getAccessLevel().equals(ModelOutput.ACCESS_LEVEL_DATA_SET_OWNER) )
				{
					if( modelOutput.getDataSetVersion().getOwner().equals(getUser()) )
					{
						analysisList.add(analysis);
					}
				}
				if( modelOutput.getOwner().equals(getUser()) )
				{
					analysisList.add(analysis);
				}
			}
		}
		return SUCCESS;
	}

	public List<Analysis> getAnalysisList() {
		return analysisList;
	}

	public void setAnalysisList(List<Analysis> analysisList) {
		this.analysisList = analysisList;
	}

	public AnalysisDAO getAnalysisDAO() {
		return analysisDAO;
	}

	public void setAnalysisDAO(AnalysisDAO analysisDAO) {
		this.analysisDAO = analysisDAO;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
}
