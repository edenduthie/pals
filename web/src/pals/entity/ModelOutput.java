package pals.entity;

import java.io.File;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ModelOutput 
{
	@Id @GeneratedValue
	private Integer id;
	private String name;
	@ManyToOne private Experiment experiment;
	@ManyToOne private DataSet specificDataSet; // chosen from the data sets of an experiment
	@OneToMany(mappedBy="modelOutput") private Set<Analysis> analyses;
	@OneToOne(cascade=CascadeType.ALL) private PalsFile file;
	private String stateSelection;
	private String parameterSelection;
	@Column(columnDefinition="TEXT") private String comments;
	private Boolean allowPublicDownloads;
	@OneToMany(cascade=CascadeType.ALL) private Set<PalsFile> ancillaryFiles;
	@ManyToOne private Model model;
	
	public Model getModel() {
		return model;
	}
	public Set<PalsFile> getAncillaryFiles() {
		return ancillaryFiles;
	}
	public void setAncillaryFiles(Set<PalsFile> ancillaryFiles) {
		this.ancillaryFiles = ancillaryFiles;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public String getStateSelection() {
		return stateSelection;
	}
	public void setStateSelection(String stateSelection) {
		this.stateSelection = stateSelection;
	}
	public String getParameterSelection() {
		return parameterSelection;
	}
	public void setParameterSelection(String parameterSelection) {
		this.parameterSelection = parameterSelection;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Boolean getAllowPublicDownloads() {
		return allowPublicDownloads;
	}
	public void setAllowPublicDownloads(Boolean allowPublicDownloads) {
		this.allowPublicDownloads = allowPublicDownloads;
	}
	public PalsFile getFile() {
		return file;
	}
	public void setFile(PalsFile file) {
		this.file = file;
	}
	public Set<Analysis> getAnalyses() {
		return analyses;
	}
	public void setAnalyses(Set<Analysis> analyses) {
		this.analyses = analyses;
	}
	public DataSet getSpecificDataSet() {
		return specificDataSet;
	}
	public void setSpecificDataSet(DataSet specificDataSet) {
		this.specificDataSet = specificDataSet;
	}
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
	public Experiment getExperiment() {
		return experiment;
	}
	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}
}
