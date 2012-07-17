package pals.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PalsFile 
{
	@Id @GeneratedValue
	Integer     id;
	
	@OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	FileData data;
	
	String name;
	String contentType;
	
//	@ManyToMany(mappedBy="files",fetch=FetchType.LAZY)
//	List<Experimentable> experimentables = new ArrayList<Experimentable>();

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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if( other instanceof PalsFile )
		{
			PalsFile otherPalsFile = (PalsFile) other;
			if( !otherPalsFile.getName().equals(getName()) ) return false;
			if( !otherPalsFile.getContentType().equals(getContentType()) ) return false;
	    	if( !otherPalsFile.getData().equals(getData()) ) return false;
	    	return true;
		}
		else
		{
			return false;
		}
	}

	public FileData getData() {
		return data;
	}

	public void setData(FileData data) {
		this.data = data;
	}
}
