package pals.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pals.analysis.AnalysisException;
import pals.analysis.MissingGlobalAttributesException;
import pals.analysis.NetcdfUtil;
import pals.analysis.ZeroValidAnalysisException;
import pals.dao.DAO;
import pals.entity.Analysable;
import pals.entity.AnalysisType;
import pals.entity.Analysis;
import pals.entity.ModelOutput;
import pals.entity.Variable;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;

@Transactional
public class AnalysableService 
{
	private static final Logger log = Logger.getLogger(AnalysableService.class);
	
	private DAO dao;
    
	public void runAnalysis(List<AnalysisType> analysisList, Analysable entity)
	{
		NetcdfFile ncFile;
		try {
            log.debug("Preparing analysis for entity: " + entity.getClass().getName());
			ncFile = NetcdfUtil.parse(entity.retrieveOutputFilePath());
			//checkRequiredGlobals(ncFile);
			//dao.persist(entity);
			prepareAnalyses(analysisList, ncFile, entity);
			entity.setStatus(ModelOutput.STATUS_ANALYSIS);
			dao.update(entity);
		} catch (IOException e) {
			fail("IOException on Netcdf File", entity);
		} catch (ZeroValidAnalysisException e) {
			entity.setStatus(ModelOutput.STATUS_ERROR);
			//entity.setErrorMessage(e.getMessage());
		}
	}
	
	/** 
	 * 
	 * Check that all required global variables exist in Netcdf file. If not, throw an exception. 
	 * 
	 * At the moment this function does nothing as there are not actually any required globals
	 * 
	 */
	public void checkRequiredGlobals(NetcdfFile ncFile) throws MissingGlobalAttributesException 
	{
		Map<String,Attribute> attMap = NetcdfUtil.extractGlobalAttributeMap(ncFile);
		List<String> requiredGlobals = new LinkedList<String>(); // @fix
		List<String> missing = new LinkedList<String>();
		Iterator<String> globIter = requiredGlobals.iterator();
		String requiredGlob;
		while(globIter.hasNext()) {
			requiredGlob = globIter.next();
			if (!attMap.containsKey(requiredGlob)) {
				missing.add(requiredGlob);
			}
		}
		if (missing.size() > 0) {
			throw new MissingGlobalAttributesException(missing);
		}
	}
	
	/** 
	 * 
	 * For each Analysis in database, check the prerequisites for it to run on this Model Output, if they are okay, set up a run. 
	 * 
	 */
	public void prepareAnalyses(List<AnalysisType> analysisList, NetcdfFile ncFile, Analysable entity) 
	throws ZeroValidAnalysisException 
	{
		log.debug("Preparing Analyses for analysable");
		Map<String,ucar.nc2.Variable> ncVarMapUC = NetcdfUtil.extractVariableMapUpperCase(ncFile);
		int analysisCount = 0;
		for( AnalysisType analysis : analysisList )
		{
			try 
			{
				// only create an analysis run if this analysis type is valid for
				// the given analysable
				if( analysis.getType().equals(entity.retrieveValidAnalysisType()) )
				{
				    //checkAnalysisCanRunOnThisModelOutput(analysis,ncVarMapUC,entity);
					log.debug("Analysis type match: " + analysis.getType());
					++analysisCount;
				    createAnalysisRun(analysis,entity);
				}
				else
				{
					log.debug("Skipping analysis type: " + analysis.getType());
				}
			} 
			catch (AnalysisException e) 
			{
				//ignore and try the next Analysis
				log.info("ANALYSIS CHECK FAILED: " + e.getMessage());
			}
			
		}
		if (analysisCount == 0) throw new ZeroValidAnalysisException("");
	}
	
	/** 
	 * 
	 * Iterate through the required variables of this <Analysis> and check each exists in the
	 * NCdf file of the <ModelOutput>. If one is missing, throw an exception.
	 * 
	 */
	/*
	private void checkAnalysisCanRunOnThisModelOutput(AnalysisType analysis, 
	    Map<String, ucar.nc2.Variable> ncVarMapUC, Analysable entity) throws AnalysisException {
		Iterator<Variable> varIter = analysis.getVariables().iterator();
		Variable var;
		log.info("Checking analysis:" + analysis.getName() + " can run on modelOutput:" + entity.getId());
		while (varIter.hasNext()) {
			var = varIter.next();
			// check this analysis variable exists in netcdf file
			// case insensitive so every var name converted to upper case
			ucar.nc2.Variable ncVar = null;
			Iterator<String> nameIter = var.getAllNames().iterator();
			while(ncVar==null && nameIter.hasNext()) 
				ncVar = ncVarMapUC.get(nameIter.next().toUpperCase());
			if (ncVar == null) {
				throw new AnalysisException(analysis.getName() + " failed to find variable: " + var.getVariableName());
			}
			log.info("Found variable " + ncVar.getName());
			// check this variable is specified in allowed units
			if (var.getAllowedUnits() != null && !var.getAllowedUnits().contains(ncVar.getUnitsString())) {
				throw new AnalysisException(analysis.getName() + " rejected units: " + ncVar.getUnitsString());
			}
			log.info("Allowed units: " + ncVar.getUnitsString());
			// check its dimension @fix
		}
	}
	*/
	
	/**
	 * 
	 * Create an entry in AnalysisRun table of database, unless one already exists.
	 * 
	 */
	public void createAnalysisRun(AnalysisType analysis, Analysable entity) throws AnalysisException {
		log.info("Create Analysis Run for input:"+ toString() + " and analysis:" + analysis.getName());
		Analysis aRun = new Analysis();
		aRun.setAnalysisType(analysis);
		aRun.setAnalysable(entity);
		aRun.setStatus(Analysis.STATUS_NEW);
		aRun.setVisible(true);
		dao.create(aRun);
	}
	
	/** 
	 * 
	 * Set this modelOutput to error state. 
	 * @fix do something with the fail message...
	 * 
	 */
	private void fail(String message, Analysable entity) {
		// set to error state, etc @fix
		entity.setStatus(ModelOutput.STATUS_ERROR);
		dao.create(entity);
		System.out.println("Failing analysable " + entity.getId() + ": " + message);
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
}
