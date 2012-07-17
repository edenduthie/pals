package pals.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pals.actions.UserAwareAction;
import pals.entity.Analysis;
import pals.entity.DataSet;


public class ViewAnalysisRun extends UserAwareAction {

	Integer analysisRunId;
	//private InputStream inputStream;

	public Integer getAnalysisRunId() {
		return analysisRunId;
	}

	public void setAnalysisRunId(Integer analysisRunId) {
		this.analysisRunId = analysisRunId;
	}
	
	public Analysis getAnalysisRun() {
		return getUserService().getAnalysisRun(getUser(), analysisRunId);
	}
	
	/*
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() {
		AnalysisRun analRun = getAnalysisRun();
		File pdfFile = getUserService().getAnalysisRunFile(analysisRunId, analRun.getModelOutputId(), analRun.getModelOutput().getUserName());
		try {
			setInputStream(new FileInputStream(pdfFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}*/
	
}
