package pals.dto;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import pals.entity.PalsUser;

public class PalsUserDTO extends ResponseDTO implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String name;
	private Long createdTime;
	private Boolean expired;
	private Boolean locked;
	private Boolean credentialsExpired;
	private Boolean enabled;
	private String ip;
	private Long signupTime;
	private WorkspaceDTO currentWorkspace;
    
    public PalsUserDTO() {}
    
    public PalsUserDTO(PalsUser palsUser)
    {
    	String[] skip = {"password","currentWorkspace"};
    	BeanUtils.copyProperties(palsUser, this, skip);
    	if( palsUser.getCurrentWorkspace() != null ) setCurrentWorkspace(new WorkspaceDTO(palsUser.getCurrentWorkspace()));
    }
    
    public PalsUserDTO(String message, boolean success)
    {
    	setMessage(message);
    	setSuccess(success);
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getSignupTime() {
		return signupTime;
	}

	public void setSignupTime(Long signupTime) {
		this.signupTime = signupTime;
	}

	public WorkspaceDTO getCurrentWorkspace() {
		return currentWorkspace;
	}

	public void setCurrentWorkspace(WorkspaceDTO currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}
}
