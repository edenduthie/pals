package pals.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Analysis 
{
	@Id @GeneratedValue
	private Integer id;
	@ManyToOne private AnalysisType analysisType;
	@ManyToOne private ModelOutput modelOutput;
	@ManyToOne private Experiment experiment;
	@ManyToMany private Set<PalsFile> results;
	
	public Set<PalsFile> getResults() {
		return results;
	}
	public void setResults(Set<PalsFile> results) {
		this.results = results;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AnalysisType getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(AnalysisType analysisType) {
		this.analysisType = analysisType;
	}
	public ModelOutput getModelOutput() {
		return modelOutput;
	}
	public void setModelOutput(ModelOutput modelOutput) {
		this.modelOutput = modelOutput;
	}
	public Experiment getExperiment() {
		return experiment;
	}
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
}
