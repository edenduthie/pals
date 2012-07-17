package pals.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Country {

	@Id @GeneratedValue
	Integer id;
	String code;
	String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer countryId) {
		this.id = countryId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String countryCode) {
		this.code = countryCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if( other instanceof Country )
		{
			Country otherCountry = (Country) other;
			if( otherCountry.getId().equals(getId()) )
			{
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	
}
