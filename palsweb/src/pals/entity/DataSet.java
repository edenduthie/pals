package pals.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import pals.Globals;

/***
 * A collection of climate data at a particular location.
 * This data can be updated, hence we have <DataSetVersion>s
 * which represent the actual data files.
 * 
 * @author Stefan Gregory
 *
 */
@Entity
public class DataSet extends Analysable{

	private String		dataSetType;
	@ManyToOne(optional=false)
	private Country		country;
	@ManyToOne(optional=false)
	private VegetationType		vegType;
	private String		userName;
	private String		url;
	@Column(columnDefinition="TEXT")
	private String		refs;
	private Double		latitude;
	private Double		longitude;
	private Double		elevation;
	private Double		maxVegHeight;
	private Double		towerHeight;
	@Column(columnDefinition="TEXT")
	private String		comments; // blob
	private Integer		downloadCount = 0;
	private Double		timeZoneOffsetHours;
	private String		measurementAggregation;
	private String soilType;
	private String siteContact;
	
	@ManyToOne(optional=true)
	private DataSetVersion latestVersion;
	
	public DataSet()
	{
		setStatus(Analysable.STATUS_NEW);
	}
	public String getDataSetType() {
		return dataSetType;
	}
	public void setDataSetType(String dataSetType) {
		this.dataSetType = dataSetType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUrl() {
		return url;
	}
	/**
	 * Return URL including protocol, which defaults to http.
	 * @return
	 */
	public String getFullUrl() {
		if( url == null ) return null;
		if (url.split(":").length < 2)
			return "http://" + url;
		else 
			return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRefs() {
		return refs;
	}
	public void setRefs(String refs) {
		this.refs = refs;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getElevation() {
		return elevation;
	}
	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}
	public Double getMaxVegHeight() {
		return maxVegHeight;
	}
	public void setMaxVegHeight(Double maxVegHeight) {
		this.maxVegHeight = maxVegHeight;
	}
	public Double getTowerHeight() {
		return towerHeight;
	}
	public void setTowerHeight(Double towerHeight) {
		this.towerHeight = towerHeight;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Integer getDownloadCount() {
		return downloadCount;
	}
	
	public void initialiseDownloadCount() {
		this.downloadCount = 0;
	}
	
	public void incrementDownloadCount() {
		if (this.downloadCount == null)
			this.downloadCount = 0;
		this.downloadCount++;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public VegetationType getVegType() {
		return vegType;
	}
	public void setVegType(VegetationType vegType) {
		this.vegType = vegType;
	}
	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}
	
	//public List<DataSetVersion> getDataSetVersions() {
	//	return dataSetVersions;
	//}
	//public void setDataSetVersions(List<DataSetVersion> dataSetVersions) {
	//	this.dataSetVersions = dataSetVersions;
	//}
	
	public boolean equals(Object other)
	{
		if( other instanceof DataSet )
		{
			DataSet dataSetOther = (DataSet) other;
			if( 
			    dataSetOther.getName().equals(getName()) &&
			    dataSetOther.getId().equals(getId())
			)
			{
			    return true;
			}
			else
			{
				return false;
			}
			    	
		}
		else
		{
			return false;
		}
	}
	public Double getTimeZoneOffsetHours() {
		return timeZoneOffsetHours;
	}
	public void setTimeZoneOffsetHours(Double timeZoneOffsetHours) {
		this.timeZoneOffsetHours = timeZoneOffsetHours;
	}
	public String getMeasurementAggregation() {
		return measurementAggregation;
	}
	public void setMeasurementAggregation(String measurementAggregation) {
		this.measurementAggregation = measurementAggregation;
	}
	
	
	public String retrieveLatString()
	{
		if( latitude != null ) return latitude.toString();
		else return null;
	}
	
	public String retrieveLonString()
	{
		if( longitude != null ) return longitude.toString();
		else return null;
	}
	
	public String retrieveElevationString()
	{
		if( elevation != null ) return elevation.toString();
		else return null;
	}
	
	public String retrieveTimezoneOffsetHoursString()
	{
		if( timeZoneOffsetHours != null ) return timeZoneOffsetHours.toString();
		else return null;
	}
	
	public void updateLatString(String lat)
	{
		if( lat != null && lat.length() > 0 )
		{
			this.latitude = Double.valueOf(lat);
		}
	}
	
	public void updateLonString(String lon)
	{
		if( lon != null && lon.length() > 0 )
		{
			this.longitude = Double.valueOf(lon);
		}
	}
	
	public void updateElevationString(String elevation)
	{
		if( elevation != null && elevation.length() > 0  )
		{
			this.elevation = Double.valueOf(elevation);
		}
	}
	
	public void updateTimezoneOffsetString(String timezoneOffset)
	{
		if( timezoneOffset != null && timezoneOffset.length() > 0 )
		{
			this.timeZoneOffsetHours = Double.valueOf(timezoneOffset);
		}
	}
	
	/**
	 * Returns the command line arguments to pass to the netcdf conversion script
	 * @return
	 */
	public String getMetadata()
	{
		String metadata = "";
		metadata += latitude;
		metadata += " " + longitude;
		//metadata += " " + timeStepSizeSeconds;
		metadata += " " + elevation;
		metadata += " " + towerHeight;
		metadata += " " + name.replace(' ', Globals.REPLACEMENT_CHAR);
		return metadata;
	}
	@Override
	public String retrieveOutputFilePath() {
	    return null;
	}
	public DataSetVersion getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(DataSetVersion latestVersion) {
		this.latestVersion = latestVersion;
	}
	public String getSoilType() {
		return soilType;
	}
	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}
	public String getSiteContact() {
		return siteContact;
	}
	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}
}
