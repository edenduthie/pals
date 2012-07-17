package pals.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pals.actions.UserAwareAction;
import pals.entity.Analysis;
import pals.utils.PalsFileUtils;


public class BenchmarkPNG extends UserAwareAction {

	Integer analysisRunId;
	private InputStream inputStream;
	private boolean thumb;

	public boolean isThumb() {
		return thumb;
	}

	public void setThumb(boolean thumb) {
		this.thumb = thumb;
	}

	public Integer getAnalysisRunId() {
		return analysisRunId;
	}

	public void setAnalysisRunId(Integer analysisRunId) {
		this.analysisRunId = analysisRunId;
	}
	
	public Analysis getAnalysisRun() {
		return getUserService().getAnalysisRun(getUser(), analysisRunId);
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() {
		Analysis analRun = getAnalysisRun();
		File pngFile;
		if (isThumb()) {
			pngFile = PalsFileUtils.getAnalysisRunFileThumb(analRun);
		} else {
			pngFile = PalsFileUtils.getAnalysisRunFilePNG(analRun);
		}
		//System.out.println("pngFile is......:" + pngFile.getAbsolutePath());
		try {
			inputStream = new FileInputStream(pngFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
}
