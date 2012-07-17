package pals.actions.user;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pals.actions.UserAwareAction;
import pals.entity.Analysis;


public class ViewPlots extends UserAwareAction {

	private List<Integer> analysisRunIds;
	private List<Analysis> analysisRuns;
	
	public List<Integer> getAnalysisRunId() {
		return analysisRunIds;
	}

	public void setAnalysisRunId(List<Integer> analysisRunIds) {
		this.analysisRunIds = analysisRunIds;
	}

	public List<Analysis> getAnalysisRuns() {
		if (analysisRuns == null) {
			analysisRuns = new LinkedList<Analysis>();
			Iterator<Integer> iter = getAnalysisRunId().iterator();
			while (iter.hasNext()) {
				analysisRuns.add(getUserService().getAnalysisRun(getUser(), iter.next()));
			}
		}
		return analysisRuns;
	}
	
	
}
