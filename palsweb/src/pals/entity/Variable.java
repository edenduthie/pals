package pals.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CollectionOfElements;

/***
 * 
 * A variable that is found in a NetCDF file that an <Analysis> is interested in.
 * 
 * An <Analysis> might require the <Variable> to be present or in particular units.
 * 
 * Variables are often spelt inconsistently, hence we have multilpe allowed names.
 * 
 * @author Stefan Gregory
 * 
 */
@Entity
public class Variable {
	
	@Id @GeneratedValue
	private Integer		variableId;
	
	private String	 	variableName;
	private Integer		analysisId;
	private boolean		required;

	@CollectionOfElements
	private List<String>	otherNames;

	@CollectionOfElements 
	private List<String> allowedUnits;
	
	
	public Integer getVariableId() {
		return variableId;
	}

	public void setVariableId(Integer variableId) {
		this.variableId = variableId;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public Integer getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(Integer analysisId) {
		this.analysisId = analysisId;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<String> getAllowedUnits() {
		return allowedUnits;
	}

	public void setAllowedUnits(List<String> allowedUnits) {
		this.allowedUnits = allowedUnits;
	}

	public List<String> getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(List<String> otherNames) {
		this.otherNames = otherNames;
	}
	
	public List<String> getAllNames() {
		LinkedList<String> all = new LinkedList<String>();
		all.add(getVariableName());
		all.addAll(getOtherNames());
		return all;
	}
	
	
}
