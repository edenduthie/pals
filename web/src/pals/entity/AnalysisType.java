package pals.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class AnalysisType 
{
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String scriptName;
	@OneToMany(mappedBy="analysisType") private Set<Analysis> analyses;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public Set<Analysis> getAnalyses() {
		return analyses;
	}
	public void setAnalyses(Set<Analysis> analyses) {
		this.analyses = analyses;
	}
}
