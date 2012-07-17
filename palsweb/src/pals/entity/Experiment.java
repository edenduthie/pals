package pals.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Experiment 
{
	@Id @GeneratedValue
	Integer     id;
	
	@ManyToOne(optional=false, cascade={CascadeType.REFRESH,CascadeType.MERGE})
	User owner;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.REFRESH,CascadeType.MERGE})
	private Set<User> sharedList;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "copiedTo")
	private List<Experimentable> experimentable = new ArrayList<Experimentable>();
	
	boolean shared;
	String name;
	boolean shareWithAll;
	Date creationDate;
	Date lastActivity;
	
	@Transient
	int numDataSets;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "experiment")
	private List<Experimentable> experimentables;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User user) {
		this.owner = user;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getSharedList() {
		return sharedList;
	}

	public void setSharedList(Set<User> sharedList) {
		this.sharedList = sharedList;
	}
	
	public void addSharedUser(User user)
	{
		if (sharedList == null ) sharedList = new HashSet<User>();
		sharedList.add(user);
	}
	
	public void removeSharedUser(User user)
	{
		if( sharedList != null )
		{
			sharedList.remove(user);
		}
	}
	
	public boolean userAllowed(User user)
	{
		if( isShareWithAll() ) return true;
		if( sharedList == null ) return false;
		for( User member : sharedList )
		{
			if( member.equals(user) ) return true;
		}
		return false;
	}

	public List<Experimentable> getExperimentables() {
		return experimentables;
	}

	public void setExperimentables(List<Experimentable> experimentables) {
		this.experimentables = experimentables;
	}

	public boolean isShareWithAll() {
		return shareWithAll;
	}

	public void setShareWithAll(boolean shareWithAll) {
		this.shareWithAll = shareWithAll;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}

	public int getNumDataSets() {
		return numDataSets;
	}

	public void setNumDataSets(int numDataSets) {
		this.numDataSets = numDataSets;
	}
	
	public void updateActivity()
	{
		setLastActivity(Calendar.getInstance().getTime());
	}

	public List<Experimentable> getExperimentable() {
		return experimentable;
	}

	public void setExperimentable(List<Experimentable> experimentable) {
		this.experimentable = experimentable;
	}
}
