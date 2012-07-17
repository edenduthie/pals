package pals.service;


import java.util.List;
import java.util.Set;

import pals.analysis.AnalysisException;
import pals.entity.Analysable;
import pals.entity.AnalysisType;
import pals.entity.ModelOutput;

public interface AnalysisServiceInterface {

	public List<Analysable> 	getAnalysableAwaitingPreAnalCheck();
	
	public List<AnalysisType> 		getAnalysisList();

	public void 				pollPreparedAnalysable();
	
	public void					pollRerunModelOutputs();
	
	public void 				pollRunningModelOutputs();
	
	public void 				pollAnalysisRuns();
	
	public void reRunAnalysisRuns();
	
}
