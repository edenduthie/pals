package pals.actions.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.dao.AnalysisDAO;
import pals.entity.Analysis;
import pals.entity.AnalysisType;
import pals.entity.DataSetVersion;

public class PlotDataAction extends UserAwareAction
{
    public AnalysisDAO analysisDAO;
	public List<Analysis> analysisList;
	public String analysisType;
	
	public static final Logger log = Logger.getLogger(PlotDataAction.class);

	/**
	 * To prepare we load all the analysis objects with DataSetVersion types
	 * @return 
	 */
	public String execute()
	{
		List<Analysis> tempAnalysisList = analysisDAO.getAnalysisByAnalysisType(getAnalysisType(),
				Analysis.STATUS_COMPLETE, getUser());
		analysisList = new ArrayList<Analysis>();
		for( Analysis analysis : tempAnalysisList )
		{
			if( getAnalysisType() == AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE )
			{
				DataSetVersion dsv = (DataSetVersion) analysis.getAnalysable();
                dsv.setDescription("1");
                dsv.getDataSet().setUrl("1");
				if( dsv.getIsPublic() ) analysisList.add(analysis);
				else
				{
					if( dsv.getOwner().equals(getUser()) ) analysisList.add(analysis);
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

	public String getAnalysisType() {
		if( analysisType == null )
		{
			analysisType = AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
		}
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
}
