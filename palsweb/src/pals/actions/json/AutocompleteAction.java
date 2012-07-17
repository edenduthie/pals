package pals.actions.json;

import java.util.List;

import pals.dao.DAO;
import pals.entity.Institution;

import com.opensymphony.xwork2.ActionSupport;

public class AutocompleteAction extends ActionSupport
{
	String INSTITUTION = "institution";
	
	DAO dao;
	
	InstitutionList institutionList;
	List<Object> institutions;
	
	String prop = "PROP";
	
    public String institution()
    {
    	institutions = dao.getAll(Institution.class.getName());
    	institutionList = new InstitutionList(institutions);
    	return INSTITUTION;
    }

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public InstitutionList getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(InstitutionList institutionList) {
		this.institutionList = institutionList;
	}

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public List<Object> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(List<Object> institutions) {
		this.institutions = institutions;
	}
}
