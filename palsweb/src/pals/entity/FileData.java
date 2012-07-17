package pals.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class FileData 
{
	@Id @GeneratedValue
	Integer     id;
	
	@Lob
	byte[] data;
	
	public FileData() {}
	
	public FileData(byte[] data)
	{
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public int length()
	{
		return data.length;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if( other instanceof FileData )
		{
			FileData otherPalsFile = (FileData) other;
	    	for(int i=0; i < getData().length; ++i)
	    	{
	    		if( getData()[i] != otherPalsFile.getData()[i] ) return false;
	    	}
	    	return true;
		}
		else
		{
			return false;
		}
	}
	
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(data);
	}
}
