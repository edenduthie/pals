package pals.dto;

import org.springframework.beans.BeanUtils;

import pals.entity.Workspace;


public class WorkspaceDTO 
{
	private Integer id;
	private String name;
	
	public WorkspaceDTO() {}
	
	public WorkspaceDTO(Workspace workspace)
	{
		BeanUtils.copyProperties(workspace,this);
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
