package pals.actions.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.web.util.HtmlUtils;

import pals.actions.UserAwareAction;
import pals.entity.Model;
import pals.entity.User;
import pals.service.ModelService;
import pals.service.SecurityException;

public class ModelAction extends UserAwareAction 
{
	static final String VIEW = "view";
	static final String EDIT = "edit";
	static final String UPDATE = "update";
	static final String DELETE = "delete";
	
    Integer modelId;
    Model model;
    ModelService modelService;
    
	private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();
    
    String filesToRemove;
    
    private static final Logger log = Logger.getLogger(ModelAction.class);
    
    String message;

    @SkipValidation
    public String view()
    {
    	model = modelService.get(modelId);
    	return VIEW;
    }
    
    @SkipValidation
    public String edit()
    {
    	model = modelService.get(modelId);
    	return EDIT;
    }
    
    public String update()
    {
    	try {
    		//model = modelService.get(model.getId());
			modelService.update(model,getUpload(),getUploadFileName(),
			    getUploadContentType(), filesToRemove);
		} catch (IOException e) {
			log.error(e.getMessage());
			return UPDATE;
		} catch (SecurityException e) {
			log.error(e.getMessage());
			return UPDATE;
		}
    	return UPDATE;
    }
    
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
	
	public List<File> getUpload() {
        return this.uploads;
    }
    public void setUpload(List<File> uploads) {
        this.uploads = uploads;
    }
    public List<String> getUploadFileName() {
        return this.uploadFileNames;
    }
    public void setUploadFileName(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }
    public List<String> getUploadContentType() {
        return this.uploadContentTypes;
    }
    public void setUploadContentType(List<String> contentTypes) {
        this.uploadContentTypes = contentTypes;
    }

	public String getFilesToRemove() {
		return filesToRemove;
	}

	public void setFilesToRemove(String filesToRemove) {
		this.filesToRemove = filesToRemove;
	}
	
	public boolean getModelOwner()
	{
		if( model != null )
		{
			if( model.getOwnerUserName().equals(getUser().getUsername())) return true;
		}
		return false;
	}
	
	public String getReferences()
	{
		if( model == null ) return null;
		String description = model.getReferencesM();
		description = HtmlUtils.htmlEscape(description);
		description = description.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
		return description;
	}
	
	public String getComments()
	{
		if( model == null ) return null;
		String description = model.getCommentsM();
		description = HtmlUtils.htmlEscape(description);
		description = description.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
		return description;
	}
	
	@SkipValidation
	public String delete()
	{
		if( !getUser().isAdmin() )
		{
			message = "Only administrators can delete models";
			return DELETE;
		}
		model = modelService.get(modelId);
		modelService.delete(model);
		message = "Model deleted: " + model.getFullName();
		return DELETE;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<Model> getModels() {
		return modelService.getModels(getUser());
	}
}
