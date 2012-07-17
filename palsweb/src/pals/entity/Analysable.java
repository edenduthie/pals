package pals.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.log4j.Logger;

/***
 * 
 * Something that an <Analysis> can run on.
 * Currently, this includes <ModelOutput>s and <DataSetVerion>s.
 * 
 * The 'status' attribute is a summary of all the analyses
 * running on this instance. Details about status flags are bellow. 
 *   
 * @author Stefan Gregory
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Analysable extends Experimentable{
	
	public static final Logger log = Logger.getLogger(Analysable.class);
	
	private char 		status;
	//@Id @GeneratedValue
	//protected Integer     id;
	
	@ManyToOne(optional=false)
	//@JoinColumn(name="owner_username",referencedColumnName="username",insertable=false,updatable=false)
	private User		owner;
	
	protected String name;

	
	public void setStatus(char status) {
		this.status = status;
	}
	public char getStatus() {
		return status;
	}
	// Status must be one of the following...
	
	public static final char STATUS_UNVERIFIED = 'u';
	
	// This instance has just been created
	public static final char STATUS_NEW = 'n';
	
	// Pre-analysis checks have been performed and passed. 
	// This instance is ready to have analysis run on it.
	public static final char STATUS_PREPARED = 'p';
	
	// Analyses are currently running on this instance.
	public static final char STATUS_ANALYSIS = 'a';
	
	// All analyses have run and are complete on this instance.
	public static final char STATUS_COMPLETE = 'c';
	
	// All anayses have run, but one or more failed.
	public static final char STATUS_ERROR = 'e';
	
	// Analyses need to be re-run on this instance, eg because a ModelOutputs dataSet attribute was edited.
	public static final char STATUS_RERUN = 'x';
	
	public String retrieveOutputFilePath()
	{
		return null;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String retrieveSiteName() {
		return null;
	}
	
	public String retrieveValidAnalysisType() {
	    return AnalysisType.DATA_SET_VERSION_ANALYSIS_TYPE;
	}
	public String retrieveOutputFilePathBench() 
	{
		return null;
	}
}
