package pals.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class DataSet 
{
	@Id @GeneratedValue
	private Integer id;
	private String name;
	@OneToMany(mappedBy="dataSet") private Set<DataSetVersion> dataSetVersions;
	@OneToMany(mappedBy="dataSet") private Set<Analysis> analyses;
	private Long startTime;
	private Long endTime;
	private String dataType;
	private String soilType;
	private String siteDescriptionUrl;
	private Double lat;
	private Double lon;
	private Double elevationM;
	private Double maxVegitationHeightM;
	private Integer utcOffsetHours;
	private String dateAtTimestampRepresents;
	private String siteContact;
	@Column(columnDefinition="TEXT") private String references;
	@Column(columnDefinition="TEXT") private String comments;
	private Long createdTime;
	@OneToOne private DataSetVersion latestVersion;

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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getSoilType() {
		return soilType;
	}
	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}
	public String getSiteDescriptionUrl() {
		return siteDescriptionUrl;
	}
	public void setSiteDescriptionUrl(String siteDescriptionUrl) {
		this.siteDescriptionUrl = siteDescriptionUrl;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Double getElevationM() {
		return elevationM;
	}
	public void setElevationM(Double elevationM) {
		this.elevationM = elevationM;
	}
	public Double getMaxVegitationHeightM() {
		return maxVegitationHeightM;
	}
	public void setMaxVegitationHeightM(Double maxVegitationHeightM) {
		this.maxVegitationHeightM = maxVegitationHeightM;
	}
	public Integer getUtcOffsetHours() {
		return utcOffsetHours;
	}
	public void setUtcOffsetHours(Integer utcOffsetHours) {
		this.utcOffsetHours = utcOffsetHours;
	}
	public String getDateAtTimestampRepresents() {
		return dateAtTimestampRepresents;
	}
	public void setDateAtTimestampRepresents(String dateAtTimestampRepresents) {
		this.dateAtTimestampRepresents = dateAtTimestampRepresents;
	}
	public String getSiteContact() {
		return siteContact;
	}
	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}
	public String getReferences() {
		return references;
	}
	public void setReferences(String references) {
		this.references = references;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Set<Analysis> getAnalyses() {
		return analyses;
	}
	public void setAnalyses(Set<Analysis> analyses) {
		this.analyses = analyses;
	}
	public Set<DataSetVersion> getDataSetVersions() {
		return dataSetVersions;
	}
	public void setDataSetVersions(Set<DataSetVersion> dataSetVersions) {
		this.dataSetVersions = dataSetVersions;
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
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	public DataSetVersion getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(DataSetVersion latestVersion) {
		this.latestVersion = latestVersion;
	}
}
