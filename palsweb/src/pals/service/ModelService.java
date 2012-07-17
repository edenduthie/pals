package pals.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import pals.dao.DAO;
import pals.entity.Experiment;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.PalsFile;
import pals.entity.User;

@Transactional
public class ModelService 
{
	DAO dao;
	ModelOutputService modelOutputService;
    FileService fileService;
	
	private static final Logger log = Logger.getLogger(ModelService.class);
	
	public Model newModel(User user, String modelName, String version, String referencesM,
	        String commentsM, String urlM, List<File> uploads, List<String> uploadFileNames, List<String> uploadContentTypes) throws IOException {
			log.info("Creating new model: " + modelName);
			Model model = new Model();
			model.setCreatedDate(new Date());
			model.setModelName(modelName);
			model.setOwnerUserName(user.getUsername());
			model.setVersion(version);
	    	Experiment e = user.getCurrentExperiment();
	    	if( e != null )
	    	{
	    		e.updateActivity();
	    		dao.update(e);
	    	}
			model.setExperiment(user.getCurrentExperiment());
			model.setReferencesM(referencesM);
			model.setCommentsM(commentsM);
		    model.setUrlM(urlM);
			if( uploads != null && uploads.size() > 0 )
			{
				List<PalsFile> fileList = new ArrayList<PalsFile>();
			    for( int i=0; i < uploads.size(); ++i )
			    {
			    	PalsFile palsFile = fileService.createFile(uploads.get(i));
			    	palsFile.setName(uploadFileNames.get(i));
			    	palsFile.setContentType(uploadContentTypes.get(i));
			    	fileList.add(palsFile);
			    	fileService.save(palsFile);
			    }
			    model.setFiles(fileList);
			}
			dao.getEntityManager().persist(model);
			return model;
		}
	
	public List<Model> getModels(User user) {
		String queryString = "from Model";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " where experiment.id=:experimentId";
		}
		else
		{
			queryString += " where experiment = NULL";
		}
		queryString += " order by id";
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("experimentId", user.getCurrentExperiment().getId());
		}
		return query.getResultList();
	}
	
	public List<Model> getMyModels(User user) {
		String queryString = "from Model";
		if( user.getCurrentExperiment() != null )
		{
			queryString += " where experiment.id=:experimentId";
		}
		else
		{
			queryString += " where experiment = NULL";
		}
		queryString += " and user.username=:username";
		queryString += " order by id";
		Query query = dao.getEntityManager().createQuery(queryString);
		if( user.getCurrentExperiment() != null )
		{
			query.setParameter("experimentId", user.getCurrentExperiment().getId());
		}
		query.setParameter("username", user.getUsername());
		return query.getResultList();
	}
	
	public void delete(Model model)
	{
		// first we delete any associated Model Outputs
		log.debug("Deleting model outputs for model: " + model.getId());
		String queryString = "from ModelOutput where modelId=:modelId";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("modelId",model.getId());
		List<ModelOutput> moList = query.getResultList();
		for(ModelOutput mo : moList)
		{
			modelOutputService.deleteModelOutput(mo);
		}
		
		dao.delete(Model.class.getName(),"id",model.getId());
	}
	
	public Model update(Model model, List<File> uploads, 
			List<String> uploadFileNames, List<String> uploadContentTypes,
			String filesToRemoveString) 
	throws IOException, SecurityException
	{
		log.info("Updating model: " + model.getId());
		log.info("Model name: " + model.getModelName());
		Model modelToUpdate = get(model.getId());
		modelToUpdate.setModelName(model.getModelName());
		modelToUpdate.setVersion(model.getVersion());
		modelToUpdate.setCommentsM(model.getCommentsM());
		modelToUpdate.setReferencesM(model.getReferencesM());
		modelToUpdate.setUrlM(model.getUrlM());
		removeFiles(filesToRemoveString,modelToUpdate);
		
		if( uploads != null && uploads.size() > 0 )
		{
			List<PalsFile> fileList = modelToUpdate.getFiles();
		    for( int i=0; i < uploads.size(); ++i )
		    {
		    	PalsFile palsFile = fileService.createFile(uploads.get(i));
		    	palsFile.setName(uploadFileNames.get(i));
		    	palsFile.setContentType(uploadContentTypes.get(i));
		    	fileList.add(palsFile);
		    	fileService.save(palsFile);
		    }
		    modelToUpdate.setFiles(fileList);
		}
		
		Experiment e = modelToUpdate.getExperiment();
		if( modelToUpdate.getExperiment() != null )
		{
			e.updateActivity();
			dao.update(e);
		}
		
		dao.update(modelToUpdate);
		return modelToUpdate;
	}
	
	public Model get(Integer id)
	{
		return (Model) dao.get(Model.class.getName(),"id", id);
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}

	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	
	public void removeFile(Model model, PalsFile file)
	{
		log.info("Removing file: " + file.getId());
		Model result = get(model.getId());
		result.getFiles().remove(file);
		dao.update(result);
		PalsFile nonDetached = fileService.getNoData(file.getId());
		dao.remove(nonDetached);
		model.getFiles().remove(file);
	}
	
	public void removeFiles(String filesToRemoveString, Model model) throws SecurityException
	{
		log.debug("Executing remove Files");
		log.debug("Files to remove: " + filesToRemoveString);
		if( filesToRemoveString != null && filesToRemoveString.length() > 0)
		{
			String[] splitString = filesToRemoveString.split("\\,");
			List<PalsFile> filesToRemove = new ArrayList<PalsFile>();
			for( String fileIdString : splitString )
			{
				fileIdString = fileIdString.trim();
				if(fileIdString != null && fileIdString.length() > 0)
				{
					Integer fileId = new Integer(fileIdString);
					for( PalsFile file : model.getFiles() )
					{
						if( file.getId().equals(fileId) )
						{
							filesToRemove.add(file);
						}
					}
				}
			}
			for( PalsFile file : filesToRemove )
			{
				removeFile(model,file);
			}
		}
	}
}
