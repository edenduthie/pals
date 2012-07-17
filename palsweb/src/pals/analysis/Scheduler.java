package pals.analysis;


import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import pals.entity.AnalysisType;
import pals.entity.ModelOutput;
import pals.entity.Variable;
import pals.service.AnalysisServiceInterface;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;


/**
 * Singleton that controls all running of analyses on Model Outputs and Data Sets. 
 * Really a wrapper class for an implementation of <AnalysisService>.
 * 
 * Potentially this could run as a separate process, outside the webapp.
 * 
 * All calls to analysis scripts (R) should originate here.
 * 
 * @author Stefan Gregory
 *
 */
public class Scheduler implements Runnable {
	
	private static int WAIT_TIME = 10000; // milliseconds between polling iterations
	private AnalysisServiceInterface analysisService;  // where all the logic happens
	private static Scheduler singleton = new Scheduler();
	private boolean awake; // is the scheduler active
	
	private Scheduler() {
	}
	
	public static Scheduler getInstance() {
		return singleton;
	}
	
	public void setAnalysisService(AnalysisServiceInterface analysisService) {
		this.analysisService = analysisService;
	}
	
	public void stop() {
		awake = false;
	}


	public void run() {
		System.out.println("Scheduler started....... * * * * * *");
		try {
			awake = true;
			while (awake) {
				//analysisService.pollRerunModelOutputs();
				analysisService.pollPreparedAnalysable();
				analysisService.pollRunningModelOutputs();
				analysisService.pollAnalysisRuns();
				Thread.sleep(WAIT_TIME);
			}
		} catch (InterruptedException e) {
			// finish
		}
		System.out.println("Scheduler stopped....... * * * * * *");
		
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Scheduler sched = getInstance();
		// "WebContent/WEB-INF/applicationContext.xml"
		String appContextPath = args[0];
		ApplicationContext appCon = new FileSystemXmlApplicationContext(appContextPath);
		sched.setAnalysisService((AnalysisServiceInterface)appCon.getBean("analysisService"));
		sched.run();
	}
	
}
