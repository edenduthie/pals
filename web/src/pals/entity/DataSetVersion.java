package pals.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DataSetVersion 
{
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private @ManyToOne DataSet dataSet;
	@OneToOne(cascade=CascadeType.ALL) private PalsFile file;
	private Long createdTime;
	
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
	public DataSet getDataSet() {
		return dataSet;
	}
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	public PalsFile getFile() {
		return file;
	}
	public void setFile(PalsFile file) {
		this.file = file;
	}
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
}
