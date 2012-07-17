package pals.actions.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pals.actions.UserAwareAction;
import pals.entity.AnalysisType;
import pals.entity.Analysis;
import pals.service.AnalysisServiceInterface;


public class PlotsSplash extends UserAwareAction {

	private AnalysisServiceInterface analysisService;
		
	public AnalysisServiceInterface getAnalysisService() {
		return analysisService;
	}

	public void setAnalysisService(AnalysisServiceInterface analysisService) {
		this.analysisService = analysisService;
	}

	
	public Collection<AnalysisType> getAnalyses() {
		return getAnalysisService().getAnalysisList();
	}
	
	public Collection<String> getAnalysisVariables() {
		Iterator<AnalysisType> iter = getAnalyses().iterator();
		Set<String> analysisVariables = new HashSet<String>();
		while (iter.hasNext()) {
			analysisVariables.add(iter.next().getVariableName());
		}
		return analysisVariables;
	}
	
	public Collection<String> getAnalysisNames() {
		Iterator<AnalysisType> iter = getAnalyses().iterator();
		Set<String> analysisNames = new HashSet<String>();
		while (iter.hasNext()) {
			analysisNames.add(iter.next().getName());
		}
		return analysisNames;
	}
	
	
}
