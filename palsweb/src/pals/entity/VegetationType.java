package pals.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VegetationType {

	@Id
	String vegetationType;
	
	boolean userAdded;

	public String getVegetationType() {
		return vegetationType;
	}

	public void setVegetationType(String vegetationType) {
		this.vegetationType = vegetationType;
	}

	public boolean isUserAdded() {
		return userAdded;
	}

	public void setUserAdded(boolean userAdded) {
		this.userAdded = userAdded;
	}
}
