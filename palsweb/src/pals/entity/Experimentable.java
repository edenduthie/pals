package pals.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Experimentable 
{
	@Id @GeneratedValue
	protected Integer     id;
	
	@ManyToOne(optional=true)
	private Experiment experiment;
	
	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	List<PalsFile> files = new ArrayList<PalsFile>();
	
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	Experimentable copiedFrom;
	
	@ManyToMany(fetch=FetchType.LAZY)
	List<Experiment> copiedTo = new ArrayList<Experiment>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public List<PalsFile> getFiles() {
		return files;
	}

	public void setFiles(List<PalsFile> files) {
		this.files = files;
	}

	public Experimentable getCopiedFrom() {
		return copiedFrom;
	}

	public void setCopiedFrom(Experimentable copiedFrom) {
		this.copiedFrom = copiedFrom;
	}

	public List<Experiment> getCopiedTo() {
		return copiedTo;
	}

	public void setCopiedTo(List<Experiment> copiedTo) {
		this.copiedTo = copiedTo;
	}
}
