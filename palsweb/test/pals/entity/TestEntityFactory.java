package pals.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestEntityFactory 
{
	static String scriptPath = "R -f D:\\code\\pals\\palsweb\\WebContent\\r\\";
	static String script = "ModelAnnualCycleNEE.R";
	
    public static Country country()
    {
    	Country country = new Country();
    	country.setCode("AU");
    	country.setName("Australia");
    	return country;
    }
    
    public static DataSet dataSet()
    {
    	DataSet dataSet = new DataSet();
    	dataSet.setComments("Comments");
    	dataSet.setCountry(country());
    	dataSet.setName("Data Set Name");
    	dataSet.setDataSetType("Fluxnet");
    	dataSet.setDownloadCount(0);
    	dataSet.setElevation(100.0);
    	dataSet.setLatitude(101.0);
    	dataSet.setLongitude(102.0);
    	dataSet.setMaxVegHeight(24.0);
    	dataSet.setRefs("Refs");
    	dataSet.setTowerHeight(34.0);
    	dataSet.setUrl("url");
    	dataSet.setOwner(user());
    	dataSet.setUserName(dataSet.getOwner().getUsername());
    	dataSet.setVegType(vegType());
    	dataSet.setTimeZoneOffsetHours(1.5);
    	dataSet.setMeasurementAggregation("dummy");
    	dataSet.setStatus(Analysable.STATUS_NEW);
    	return dataSet;
    }
    
    public static User user()
    {
    	User user = new User();
    	user.setAdmin(false);
    	user.setCreated(Calendar.getInstance().getTime());
    	user.setEmail("Email");
    	user.setFullName("Full Name");
    	user.setPassword("Password");
    	user.setShortName("Short Name");
    	user.setUsername("username");
    	user.setInstitution(institution());
    	user.setPhoto(photo());
    	return user;
    }
    
    public static VegetationType vegType()
    {
    	VegetationType vegType = new VegetationType();
    	vegType.setVegetationType("Vegetation Type");
    	return vegType;
    }
    
    public static DataSetVersion dataSetVersion()
    {
    	DataSetVersion version = new DataSetVersion();
    	version.setDataSet(dataSet());
    	version.setDataSetId(version.getDataSet().getId());
    	//version.setDataSetVersionId(1);
    	version.setDescription("Description");
    	version.setStatus(Analysable.STATUS_PREPARED);
    	version.setUploadDate(Calendar.getInstance().getTime());
    	version.setName("First Version");
    	version.setOriginalFileName("test.csv");
    	version.setOwner(user());
    	Calendar now = Calendar.getInstance();
    	version.setEndDate(now.getTime());
    	version.setStartDate(now.getTime());
    	version.setUploadDate(now.getTime());
    	return version;
    }
    
    public static ModelOutput modelOutput()
    {
        ModelOutput modelOutput = new ModelOutput();
        modelOutput.setDataSet(dataSetVersion());
        modelOutput.setDataSetVersionId(modelOutput.getDataSetVersion().getId());
        modelOutput.setDate(Calendar.getInstance().getTime());
        modelOutput.setModel(model());
        modelOutput.setModelId(modelOutput.getModel().getId());
        //modelOutput.setModelOutputId(1);
        modelOutput.setName("ModelOutputName");
        modelOutput.setParameterSelection("Parameter Selection");
        modelOutput.setAccessLevel(ModelOutput.ACCESS_LEVEL_PUBLIC);
        modelOutput.setStateSelection("VIC");
        modelOutput.setUploadDate(Calendar.getInstance().getTime());
        modelOutput.setOwner(user());
        modelOutput.setUserComments("User comments");
        modelOutput.setUserName(modelOutput.getOwner().getUsername());
        //modelOutput.setVisible(true);
        modelOutput.getDataSetVersion().getDataSet().setOwner(modelOutput.getOwner());
        modelOutput.getDataSetVersion().getDataSet().setUserName(modelOutput.getOwner().getUsername());
        modelOutput.setStatus(Analysable.STATUS_PREPARED);
        return modelOutput;
    }
    
    public static Model model()
    {
    	Model model = new Model();
    	model.setCreatedDate(Calendar.getInstance().getTime());
    	model.setModelName("Model Name");
    	model.setUser(user());
    	model.setOwnerUserName(model.getUser().getUsername());
    	model.setVersion("Version 1");
    	//model.setModelId(1);
    	return model;
    }
    
    public static AnalysisType analysisType()
    {
    	AnalysisType analysis = new AnalysisType();
    	analysis.setType(AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE);
    	analysis.setName("Test Analysis");
    	analysis.setAnalysisTypeName("AnalysisTypeName");
    	analysis.setVariableName("Variable Name");
    	analysis.setExecutablePath(scriptPath+script);
    	return analysis;
    }
    
    public static List<Variable> variableList()
    {
    	List<Variable> variables = new ArrayList<Variable>();
    	return variables;
    }
    
    public static List<AnalysisType> analysisList()
    {
        List<AnalysisType> list = new ArrayList<AnalysisType>();
        list.add(analysisType());
        return list;
    }
    
    public static Analysis analysis()
    {
        Analysis analysis = new Analysis();
        analysis.setAnalysable(dataSet());
        analysis.setAnalysisType(analysisType());
        analysis.setEndTime(Calendar.getInstance().getTime());
        analysis.setStartTime(Calendar.getInstance().getTime());
        analysis.setStatus(Analysis.STATUS_RUNNING);
        analysis.setVisible(true);
        return analysis;
    }
    
    public static Experiment experiment()
    {
    	Experiment experiment = new Experiment();
    	experiment.setShared(false);
    	experiment.setOwner(user());
    	experiment.setName("experiment");
    	return experiment;
    }
    
    public static Institution institution()
    {
    	Institution inst = new Institution();
    	inst.setName("UNSW");
    	return inst;
    }
    
    public static Photo photo()
    {
    	Photo photo = new Photo();
    	photo.setFilename("photo.jpg");
    	return photo;
    }
}
