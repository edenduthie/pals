package pals.actions;

import java.io.FileNotFoundException;
import java.io.IOException;

import pals.entity.ModelOutput;
import pals.service.PublicServiceInterface;
import pals.utils.PalsFileUtils;


public class ViewModelOutput extends UserAwareAction {

	Integer modelOutputId;
	private PublicServiceInterface publicService;

	public Integer getModelOutputId() {
		return modelOutputId;
	}
	public void setModelOutputId(Integer modelOutputId) {
		this.modelOutputId = modelOutputId;
	}
	public PublicServiceInterface getPublicService() {
		return publicService;
	}

	public void setPublicService(PublicServiceInterface publicService) {
		this.publicService = publicService;
	}

	public ModelOutput getModelOutput() {
		return getPublicService().getModelOutput(getModelOutputId());
	}
	
	public String getRaw() {
		String result = null;
		try {
			result =  getPublicService().getTopOfFile(PalsFileUtils.getModelOutputFilePath(getModelOutput()), 50);
		} catch (FileNotFoundException e) {
			result = "File not found.";
		} catch (IOException e) {
			result = "IOException.";
		}
		return result;
	}
}
