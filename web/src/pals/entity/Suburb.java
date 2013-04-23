package pals.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Suburb implements Serializable
{
	private static final long serialVersionUID = 727745081763426116L;
	
	@Id @GeneratedValue
	private Integer id;
	
	private String name;

	
	public Suburb() {}
	
	public Suburb(String name) 
	{
		this.name = name.replace('\'', ' ');
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
}
