package pals.actions.json;

import java.util.ArrayList;
import java.util.List;

import pals.entity.Institution;

public class InstitutionList 
{
    public List<Institution> institutions;
    
    public InstitutionList(List<Object> objects)
    {
    	institutions = new ArrayList<Institution>();
    	for( Object o : objects )
    	{
    		institutions.add((Institution) o);
    	}
    }

	public List<Institution> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(List<Institution> institutions) {
		this.institutions = institutions;
	}
}
