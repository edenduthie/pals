package pals.dto;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import pals.entity.PalsUser;

public class PalsUserDTO extends ResponseDTO implements Serializable
{
    public String email;
    
    public PalsUserDTO() {}
    
    public PalsUserDTO(PalsUser palsUser)
    {
    	BeanUtils.copyProperties(palsUser, this);
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
}
