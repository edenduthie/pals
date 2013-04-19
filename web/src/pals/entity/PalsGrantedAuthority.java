package pals.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class PalsGrantedAuthority implements GrantedAuthority
{
	private static final long serialVersionUID = -4983123184618219303L;
	
	public static final String ADMIN_ROLE = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_BLOGGER = "ROLE_BLOGGER";
	public static final String ROLE_PRACTITIONER = "ROLE_PRACTITIONER";
	
	@Id @GeneratedValue
	Integer 		id;
	
	String authority;
	
	@ManyToMany(fetch=FetchType.LAZY,mappedBy="authorities")
	List<PalsUser> users = new ArrayList<PalsUser>();
	
	public static PalsGrantedAuthority getLoginRole()
	{
		PalsGrantedAuthority auth = new PalsGrantedAuthority();
		auth.setAuthority(ROLE_USER);
		return auth;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
