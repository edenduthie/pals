package pals.actions;

import java.util.List;

import pals.entity.ModelOutput;
import pals.service.ModelOutputService;

public class ListModelOutputs  extends UserAwareAction {

	public String execute() {
		return SUCCESS;
	}
	
	private ModelOutputService modelOutputService;

	
	public List<ModelOutput> getModelOutputs() {
		return modelOutputService.getPublicModelOutputs(getUser());
	}


	public ModelOutputService getModelOutputService() {
		return modelOutputService;
	}


	public void setModelOutputService(ModelOutputService modelOutputService) {
		this.modelOutputService = modelOutputService;
	}
}
