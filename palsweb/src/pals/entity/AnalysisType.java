package pals.entity;

import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import pals.Configuration;

/***
 * 
 * Represents an R script that runs on a NetCDF file and produces a PDF plot.
 * 
 * The NetCDF file is associated with either a <ModelOutput> or <DataSetVersion>,
 * which are realised by <ModelOutputAnalysis> and <DataSetAnalysis> respectively.
 * The resulting plot is represented by an <AnalysisRun> object.
 *
 * @author Stefan Gregory
 *
 */
@Entity
public class AnalysisType {
	
	public static final String MODEL_OUTPUT_ANALYSIS_TYPE = "MODEL_OUTPUT";
	public static final String DATA_SET_ANALYSIS_TYPE = "DATA_SET";
	public static final String DATA_SET_VERSION_ANALYSIS_TYPE = "DATA_SET_VERSION";
	
	@Id @GeneratedValue
	Integer 		id;
	String			name;
	//String			variableName;
	String			executablePath;
	String			type;
	String variableName;
	String analysisTypeName;
	
	/*
	@OneToMany(mappedBy="id", fetch=FetchType.EAGER) @Cascade(value=CascadeType.ALL)
	List<Variable>	variables;
	*/
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer analysisId) {
		this.id = analysisId;
	}

	public String getName() {
		return name;
	}

	public void setName(String analysisName) {
		this.name = analysisName;
	}

	/*
	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String analysisVariableName) {
		this.variableName = analysisVariableName;
	}
	*/

	public String getExecutablePath() {
		return executablePath;
	}

	public void setExecutablePath(String executablePath) {
		this.executablePath = executablePath;
	}
	
	/*
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	*/
	
    public Class getInputClass()
    {
    	return getClass();
    }

	public String getType() {
		return type;
	}

	public void setType(String analysisType) {
		this.type = analysisType;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	public String retrieveValidAnalysisType() {
		return AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
	}

	public String getAnalysisTypeName() {
		return analysisTypeName;
	}

	public void setAnalysisTypeName(String analysisTypeName) {
		this.analysisTypeName = analysisTypeName;
	}

}
