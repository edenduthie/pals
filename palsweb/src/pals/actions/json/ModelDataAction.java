package pals.actions.json;

import java.util.ArrayList;
import java.util.List;

import pals.actions.UserAwareAction;
import pals.dao.AnalysisDAO;
import pals.dao.ModelDAO;
import pals.entity.Analysis;
import pals.entity.Model;

public class ModelDataAction extends UserAwareAction
{
    public List<Model> modelList;
    public AnalysisDAO analysisDAO;
    public ModelDAO modelDAO;
    
    public String execute()
    {
    	modelList = new ArrayList<Model>();
    	List<Model> retrievedModelList = modelDAO.getAll(getUser());
    	// only include models with at least one analysis that is complete
    	for( Model model : retrievedModelList )
    	{
    		List<Analysis> analysisList = analysisDAO.getAnalysisByModelId(model.getId(),Analysis.STATUS_COMPLETE,getUser());
    		if( analysisList.size() > 0 ) modelList.add(model);
    	}
    	return SUCCESS;
    }
    
	public List<Model> getModelList() {
		return modelList;
	}
	public void setModelList(List<Model> modelList) {
		this.modelList = modelList;
	}

	public AnalysisDAO getAnalysisDAO() {
		return analysisDAO;
	}

	public void setAnalysisDAO(AnalysisDAO analysisDAO) {
		this.analysisDAO = analysisDAO;
	}

	public ModelDAO getModelDAO() {
		return modelDAO;
	}

	public void setModelDAO(ModelDAO modelDAO) {
		this.modelDAO = modelDAO;
	}
}
