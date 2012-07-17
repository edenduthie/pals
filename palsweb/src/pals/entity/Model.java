package pals.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/***
 * Represents a climate modelling experiment. 
 * Each <ModelOutput> must be associated with a <Model>
 * 
 * @author Stefan Gregory
 *
 */

@Entity
public class Model extends Experimentable {
	
	private String 		ownerUserName;
	private String 		modelName;
	private String		version;
	private Date		createdDate;
	@Column(columnDefinition="TEXT")
	private String      referencesM;
	@Column(columnDefinition="TEXT")
	private String      commentsM;
	private String      urlM;

	@ManyToOne(optional=false)
	@JoinColumn(name="ownerUserName",referencedColumnName="userName",insertable=false,updatable=false)
	private User		user;
	
	public Model(){}
	
	public Model(Integer id, String modelName, String version)
	{
		this.id = id;
		this.modelName = modelName;
		this.version = version;
	}
	public String getOwnerUserName() {
		return ownerUserName;
	}
	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	
	public String getDisplayName() {
		return modelName + "." + version;
	}
	
	public String getIdentifier()
	{
	    if( modelName != null && version != null )
	    {
	        return modelName + "." + version;
	    }
	    else
	    {
	    	return null;
	    }
	}
	
	@Override
	public boolean equals(Object other)
	{
		if( other instanceof Model )
		{
			Model otherModel = (Model) other;
			if( otherModel.getId().equals(getId()) ) return true;
			else return false;
		}
		else
		{
			return false;
		}
	}

	public String getReferencesM() {
		return referencesM;
	}

	public void setReferencesM(String referencesM) {
		this.referencesM = referencesM;
	}

	public String getCommentsM() {
		return commentsM;
	}

	public void setCommentsM(String commentsM) {
		this.commentsM = commentsM;
	}

	public String getUrlM() {
		return urlM;
	}

	public void setUrlM(String urlM) {
		this.urlM = urlM;
	}	
	
	public String getFullName()
	{
		return modelName + "." + version;
	}
}
