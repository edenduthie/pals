package pals.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class PalsUser extends ComparableBean implements UserDetails
{
	private static final long serialVersionUID = -7709595936431626340L;
	
	@Id @GeneratedValue
	private Integer id;
	private String email;
	private String password;
	private String name;
	private Long createdTime;
	private Boolean expired = false;
	private Boolean locked = false;
	private Boolean credentialsExpired = false;
	private Boolean enabled = true;
	private String ip;
	private Long signupTime;
	@OneToMany(mappedBy="owner") private Set<Workspace> workspaces;
	@ManyToMany(mappedBy="guests") private Set<Workspace> guestWorkspaces;
	@ManyToOne private Workspace currentWorkspace;
	
	@ManyToMany(fetch=FetchType.EAGER)
	List<PalsGrantedAuthority> authorities = new ArrayList<PalsGrantedAuthority>();
	
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	@Transient
	public String getUsername() {
		return getEmail();
	}
	@Override
	public boolean isAccountNonExpired() {
		return !getExpired();
	}
	@Override
	public boolean isAccountNonLocked() {
		return !getLocked();
	}
	@Override
	public boolean isCredentialsNonExpired() {
        return !getCredentialsExpired();
	}
	@Override
	public boolean isEnabled() {
		return getEnabled();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}
	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	@Override
	public List<PalsGrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<PalsGrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public void add(PalsGrantedAuthority roleUser) 
	{
		if( authorities == null ) authorities = new ArrayList<PalsGrantedAuthority>();
		authorities.add(roleUser);
	}
	
	public boolean hasRole(String role)
	{
		if( getAuthorities() != null )
		{
			for( PalsGrantedAuthority auth : getAuthorities() )
			{
				if(auth.getAuthority().equals(role)) return true;
			}
		}
		return false;
	}
	
	@Transient
	public Boolean getPractitioner()
	{
	    return hasRole(PalsGrantedAuthority.ROLE_PRACTITIONER);
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
	public Set<Workspace> getWorkspaces() {
		return workspaces;
	}
	public void setWorkspaces(Set<Workspace> workspaces) {
		this.workspaces = workspaces;
	}
	public Set<Workspace> getGuestWorkspaces() {
		return guestWorkspaces;
	}
	public void setGuestWorkspaces(Set<Workspace> guestWorkspaces) {
		this.guestWorkspaces = guestWorkspaces;
	}
	public Workspace getCurrentWorkspace() {
		return currentWorkspace;
	}
	public void setCurrentWorkspace(Workspace currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}
}
