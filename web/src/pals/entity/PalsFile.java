package pals.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PalsFile
{
	public static final String JPG = "jpg";
	public static final String PNG = "png";
	
	@Id @GeneratedValue
	private Integer 		id;
	
    @OneToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
    FileData data;
    
    public PalsFile() {}
    
    public PalsFile(byte[] photoDataBytes, Integer id)
    {
    	this.id = id;
    	FileData photoData = new FileData();
    	photoData.setPicture(photoDataBytes);
    	this.data = photoData;
    }
    
    public PalsFile clone()
    {
    	PalsFile photo = new PalsFile();
    	photo.setName(name);
    	photo.setType(type);
    	photo.setPicture(getPicture().clone());
    	return photo;
    }
	
	/**
	 * jpg or png
	 */
	private String type;
	
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getPicture() {
		return getData().getPicture();
	}

	public void setPicture(byte[] picture) {
		this.data = new FileData();
		this.data.setPicture(picture);
	}
	
	public InputStream retrieveInputStream()
	{
		return new ByteArrayInputStream(getPicture());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FileData getData() {
		return data;
	}

	public void setData(FileData data) {
		this.data = data;
	}
}
