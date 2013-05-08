package pals.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Experiment 
{
	public static final String PUBLIC_STATUS = "public";
	public static final String PRIVATE_STATUS = "private";
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String type;
	@ManyToOne(fetch=FetchType.EAGER) private Country country;
	@ManyToOne(fetch=FetchType.EAGER) private VegType vegType;
	private Long startTime;
	private Long endTime;
	private String spatialLevel;
	private @ManyToMany Set<DataSet> drivingDataSets;
	private @ManyToMany Set<DataSet> inputDataSets;
	private String timeStepSize;
	@ManyToOne private PalsUser owner;
	private String status;
	@ManyToMany private Set<Workspace> workspaces;
	
	public String getSpatialLevel() {
		return spatialLevel;
	}
	public void setSpatialLevel(String spatialLevel) {
		this.spatialLevel = spatialLevel;
	}
	public String getTimeStepSize() {
		return timeStepSize;
	}
	public void setTimeStepSize(String timeStepSize) {
		this.timeStepSize = timeStepSize;
	}
	@OneToMany(mappedBy="experiment") Set<ModelOutput> modelOutputs;
	
	public Set<ModelOutput> getModelOutputs() {
		return modelOutputs;
	}
	public void setModelOutputs(Set<ModelOutput> modelOutputs) {
		this.modelOutputs = modelOutputs;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public VegType getVegType() {
		return vegType;
	}
	public void setVegType(VegType vegType) {
		this.vegType = vegType;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Set<DataSet> getDrivingDataSets() {
		return drivingDataSets;
	}
	public void setDrivingDataSets(Set<DataSet> drivingDataSets) {
		this.drivingDataSets = drivingDataSets;
	}
	public Set<DataSet> getInputDataSets() {
		return inputDataSets;
	}
	public void setInputDataSets(Set<DataSet> inputDataSets) {
		this.inputDataSets = inputDataSets;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public PalsUser getOwner() {
		return owner;
	}
	public void setOwner(PalsUser owner) {
		this.owner = owner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Set<Workspace> getWorkspaces() {
		return workspaces;
	}
	public void setWorkspaces(Set<Workspace> workspaces) {
		this.workspaces = workspaces;
	}
}
