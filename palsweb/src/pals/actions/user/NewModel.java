package pals.actions.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import pals.actions.UserAwareAction;
import pals.entity.Model;
import pals.service.ModelService;

public class NewModel extends UserAwareAction {

	private static final Logger log = Logger.getLogger(NewModel.class);
	
	private String modelName;
	private String version;
	
	private String referencesM;
	private String commentsM;
	private String urlM;
	
	public String SHOW = "show";
	public String LIST = "list";
	public String ADD = "add";
	public String MY = "my";
	
	ModelService modelService;
	
    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();
    
    boolean myModels = false;
	
	
	@SkipValidation
	public String show()
	{
		return SHOW;
	}
	
	public String add() {
		if (getModelName() == null || getVersion() == null) {
			return INPUT;
		}
		try {
			getModelService().newModel(getUser(), modelName, version, referencesM, commentsM,
			    urlM, uploads, uploadFileNames, uploadContentTypes);
		} catch (IOException e) {
			log.error(e.getMessage());
			return INPUT;
		}
		return LIST;
	}
	
	@SkipValidation
	public String list() {
		return LIST;
	}
	
	@SkipValidation
	public String my() {
		myModels = true;
		return LIST;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<Model> getModels() {
		if( myModels ) return modelService.getMyModels(getUser());
		else return modelService.getModels(getUser());
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

	public String getReferencesM() {
		return referencesM;
	}

	public void setReferencesM(String referencesM) {
		this.referencesM = referencesM;
	}

	public String getCommentsM() {
		return commentsM;
	}

	public void setCommentsM(String commentsM) {
		this.commentsM = commentsM;
	}

	public String getUrlM() {
		return urlM;
	}

	public void setUrlM(String urlM) {
		this.urlM = urlM;
	}

	public boolean isMyModels() {
		return myModels;
	}

	public void setMyModels(boolean myModels) {
		this.myModels = myModels;
	}

}
