package pals.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.entity.Analysis;
import pals.utils.ImageUtil;
import pals.utils.PalsFileUtils;


public class AnalysisRunPNG extends UserAwareAction {
	
	public static final Logger log = Logger.getLogger(AnalysisRunPNG.class);

	Integer analysisRunId;
	private InputStream inputStream;
	private boolean thumb;
	private boolean bench;

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
			try 
			{
				inputStream = new FileInputStream(pngFile);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if( isBench() ) {
			try 
			{
				pngFile = PalsFileUtils.getAnalysisRunFileBenchPNG(analRun);
				inputStream = new FileInputStream(pngFile);
			} 
			catch (FileNotFoundException e) {
				log.info("Benchmark plot not found");
				try 
				{
					pngFile = PalsFileUtils.getAnalysisRunFilePNG(analRun);
					InputStream toWriteOn = new FileInputStream(pngFile);
					inputStream = ImageUtil.writeTextOnImage(toWriteOn,
							"Benchmarks are not available for this","analysis type on this model output");
					
				} 
				catch (FileNotFoundException e2) 
				{
					log.error("Analysis file not found");
					e2.printStackTrace();
				} 
				catch (IOException e3) {
					log.error("IOException reading analysis file");
					e3.printStackTrace();
				}
			}
		}
		else {
			pngFile = PalsFileUtils.getAnalysisRunFilePNG(analRun);
			try 
			{
				inputStream = new FileInputStream(pngFile);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return SUCCESS;
	}

	public boolean isBench() {
		return bench;
	}

	public void setBench(boolean bench) {
		this.bench = bench;
	}
	
}
