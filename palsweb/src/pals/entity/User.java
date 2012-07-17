package pals.entity;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import pals.Configuration;

/**
 * Someone who can log into the PALS application.
 * 
 * @author Stefan Gregory
 *
 */

@Entity @Table(name="PalsUser")
public class User {

	@Id
	private String 		username;
	private String		password;
	private String 		fullName;
	private String 		shortName;
	private String 		email;
	private Date 		created;
	private boolean 	isAdmin;
	
	private Integer itemsPerPageImportDataSet;
	private Integer itemsPerPageListPublicDataSets;
	private Integer itemsPerPageMyDataSets;
	private Integer itemsPerPageListPublicModelOutputs;
	private Integer itemsPerPageListMyModelOutputs;
	private Integer itemsPerPageListDataSetOwnerModelOutputs;
	
	@ManyToOne(optional=true, cascade={CascadeType.REFRESH,CascadeType.MERGE})
	private Experiment  currentExperiment;
	
	@ManyToMany(mappedBy="sharedList", cascade={CascadeType.REFRESH,CascadeType.MERGE})
	Set<Experiment> experiments;
	
	@ManyToOne(optional=true, cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	Institution institution;
	@Column(columnDefinition="TEXT")
	String researchInterest;
	@ManyToOne(optional=true,cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	Photo photo;
	Boolean showEmail;
	
	@Transient boolean shared;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public String fileDirectory()
	{
		return username;
	}
	
	public String retrieveFilePath()
	{
		return Configuration.getInstance().PATH_TO_APP_DATA + File.separator + getUsername();
	}
	
	@Override
	public boolean equals(Object other)
	{
		if( other instanceof User )
		{
			User otherUser = (User) other;
			if( otherUser.getUsername().equals(getUsername()) ) return true;
			else return false;
		}
		else
		{
			return false;
		}
	}
	public Set<Experiment> getExperiments() {
		return experiments;
	}
	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}
	public Experiment getCurrentExperiment() {
		return currentExperiment;
	}
	public void setCurrentExperiment(Experiment currentExperiment) {
		this.currentExperiment = currentExperiment;
	}
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	public boolean correctPassword(String password)
	{
		MessageDigest mdEnc = null;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
		mdEnc.update(password.getBytes(), 0, password.length());
		String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
		if( md5.equals(getPassword()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void encryptPassword() throws NoSuchAlgorithmException
	{
		MessageDigest mdEnc = null;
        mdEnc = MessageDigest.getInstance("MD5");
		mdEnc.update(getPassword().getBytes(), 0, getPassword().length());
		String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
		setPassword(md5);
	}
	public String getResearchInterest() {
		return researchInterest;
	}
	public void setResearchInterest(String researchInterest) {
		this.researchInterest = researchInterest;
	}
	public Institution getInstitution() {
		return institution;
	}
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public Boolean getShowEmail() {
		return showEmail;
	}
	public void setShowEmail(Boolean showEmail) {
		this.showEmail = showEmail;
	}
	
	public void remove(Experiment experiment)
	{
		experiments.remove(experiment);
	}
	public Integer getItemsPerPageImportDataSet() {
		return itemsPerPageImportDataSet;
	}
	public void setItemsPerPageImportDataSet(Integer itemsPerPageImportDataSet) {
		this.itemsPerPageImportDataSet = itemsPerPageImportDataSet;
	}
	
	public Integer getItemsPerPageImportDataSetWithDefault()
	{
		if( itemsPerPageImportDataSet == null ) 
		{
			return Configuration.getInstance().getIntProperty("DEFAULT_ITEMS_PER_PAGE_IMPORT_DATA_SET");
		}
		else
		{
			return getItemsPerPageImportDataSet();
		}
	}
	
	
	public Integer getItemsPerPageListPublicDataSetsWithDefault() 
	{
		if( itemsPerPageListPublicDataSets == null ) 
		{
			return Configuration.getInstance().getIntProperty("DEFAULT_ITEMS_PER_PAGE_LIST_PUBLIC_DATA_SETS");
		}
		else
		{
			return getItemsPerPageListPublicDataSets();
		}
	}
	
	public Integer getItemsPerPageMyDataSetsWithDefault() 
	{
		if( itemsPerPageMyDataSets == null ) 
		{
			return Configuration.getInstance().getIntProperty("DEFAULT_ITEMS_PER_PAGE_MY_DATA_SETS");
		}
		else
		{
			return getItemsPerPageMyDataSets();
		}
	}
	
	public Integer getItemsPerPageListPublicModelOutputsWithDefault() 
	{
		if( itemsPerPageListPublicModelOutputs == null ) 
		{
			return Configuration.getInstance().getIntProperty("DEFAULT_ITEMS_PER_PAGE_LIST_PUBLIC_MODEL_OUTPUTS");
		}
		else
		{
			return getItemsPerPageListPublicModelOutputs();
		}
	}
	
	public Integer getItemsPerPageListMyModelOutputsWithDefault() 
	{
		if( itemsPerPageListMyModelOutputs == null ) 
		{
			return Configuration.getInstance().getIntProperty("DEFAULT_ITEMS_PER_PAGE_LIST_MY_MODEL_OUTPUTS");
		}
		else
		{
			return getItemsPerPageListMyModelOutputs();
		}
	}
	
	public int getItemsPerPageListDataSetOwnerModelOutputsWithDefault() 
	{
		if( itemsPerPageListDataSetOwnerModelOutputs == null ) 
		{
			return Configuration.getInstance().getIntProperty("DEFAULT_ITEMS_PER_PAGE_LIST_DATA_SET_OWNER_MODEL_OUTPUTS");
		}
		else
		{
			return getItemsPerPageListDataSetOwnerModelOutputs();
		}
	}
	
	public Integer getItemsPerPageListPublicDataSets() {
		return itemsPerPageListPublicDataSets;
	}
	public void setItemsPerPageListPublicDataSets(
			Integer itemsPerPageListPublicDataSets) {
		this.itemsPerPageListPublicDataSets = itemsPerPageListPublicDataSets;
	}
	public Integer getItemsPerPageMyDataSets() {
		return itemsPerPageMyDataSets;
	}
	public void setItemsPerPageMyDataSets(Integer itemsPerPageMyDataSets) {
		this.itemsPerPageMyDataSets = itemsPerPageMyDataSets;
	}
	public Integer getItemsPerPageListPublicModelOutputs() {
		return itemsPerPageListPublicModelOutputs;
	}
	public void setItemsPerPageListPublicModelOutputs(
			Integer itemsPerPageListPublicModelOutputs) {
		this.itemsPerPageListPublicModelOutputs = itemsPerPageListPublicModelOutputs;
	}
	public Integer getItemsPerPageListMyModelOutputs() {
		return itemsPerPageListMyModelOutputs;
	}
	public void setItemsPerPageListMyModelOutputs(
			Integer itemsPerPageListMyModelOutputs) {
		this.itemsPerPageListMyModelOutputs = itemsPerPageListMyModelOutputs;
	}
	public Integer getItemsPerPageListDataSetOwnerModelOutputs() {
		return itemsPerPageListDataSetOwnerModelOutputs;
	}
	public void setItemsPerPageListDataSetOwnerModelOutputs(
			Integer itemsPerPageListDataSetOwnerModelOutputs) {
		this.itemsPerPageListDataSetOwnerModelOutputs = itemsPerPageListDataSetOwnerModelOutputs;
	}
	
}
