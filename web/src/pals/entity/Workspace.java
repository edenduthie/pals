package pals.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.beans.BeanUtils;

import pals.dto.WorkspaceDTO;

@Entity
public class Workspace 
{
	public static final String ROOT_WORKSPACE_NAME = "ROOT_WORKSPACE";
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	@ManyToOne private PalsUser owner;
	@ManyToMany private Set<PalsUser> guests;
	@OneToMany(mappedBy="currentWorkspace") private Set<PalsUser> currentUsers;
	@ManyToMany(mappedBy="workspaces") Set<DataSet> dataSets;
	@ManyToMany(mappedBy="workspaces") Set<ModelOutput> modelOutputs;
	@ManyToMany(mappedBy="workspaces") Set<Experiment> experiments;
	
	public Workspace() {}
	
	
	public Workspace(WorkspaceDTO currentWorkspace) 
	{
		String[] skip = {"ownser","guests","currentUsers","dataSets","modelOutputs","experiments"};
		BeanUtils.copyProperties(currentWorkspace, this, skip);
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
	public PalsUser getOwner() {
		return owner;
	}
	public void setOwner(PalsUser owner) {
		this.owner = owner;
	}
	public Set<PalsUser> getGuests() {
		return guests;
	}
	public void setGuests(Set<PalsUser> guests) {
		this.guests = guests;
	}
	public Set<PalsUser> getCurrentUsers() {
		return currentUsers;
	}
	public void setCurrentUsers(Set<PalsUser> currentUsers) {
		this.currentUsers = currentUsers;
	}
	public Set<DataSet> getDataSets() {
		return dataSets;
	}
	public void setDataSets(Set<DataSet> dataSets) {
		this.dataSets = dataSets;
	}
	public Set<ModelOutput> getModelOutputs() {
		return modelOutputs;
	}
	public void setModelOutputs(Set<ModelOutput> modelOutputs) {
		this.modelOutputs = modelOutputs;
	}
	public Set<Experiment> getExperiments() {
		return experiments;
	}
	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}
}
