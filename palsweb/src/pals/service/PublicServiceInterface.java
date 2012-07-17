package pals.service;

import java.io.IOException;
import java.util.List;

import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;


public interface PublicServiceInterface {

	public List<DataSet> getDataSets();

	public DataSetVersion getDataSetVersion(Integer dataSetVersionId);
	
	public DataSetVersion getLatestDataSetVersion(Integer dataSetId);
	
	public Model getModel(Integer modelId);
	
	public DataSet getDataSet(Integer dataSetId);
	
	public ModelOutput getModelOutput(Integer modelOutputId);
	
	public String getTopOfFile(String filePath, int numberOfLines)  throws IOException;
	
	//public void persistDataSet(DataSet dataSet);

	//public void persistModelOutput(ModelOutput mo);
	
}
