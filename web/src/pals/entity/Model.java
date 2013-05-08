package pals.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Model 
{
	@Id @GeneratedValue
	private Integer id;
	private String name;
	@OneToMany(mappedBy="model") private Set<ModelOutput> modelOutputs;
	
	public Set<ModelOutput> getModelOutputs() {
		return modelOutputs;
	}
	public void setModelOutputs(Set<ModelOutput> modelOutputs) {
		this.modelOutputs = modelOutputs;
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
