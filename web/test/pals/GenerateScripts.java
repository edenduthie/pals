package pals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pals.database.SuburbDAO;
import pals.entity.Suburb;

public class GenerateScripts extends BaseTest
{
	String suburbsFile = "scripts/suburbs.sql";
	
	@Autowired SuburbDAO suburbDAO;
	
    //@Test
    public void generateSuburbs() throws IOException
    {
    	List<Suburb> suburbs = suburbDAO.getAll();
    	
    	FileWriter writer = new FileWriter(new File(suburbsFile));
    	
    	writer.write("insert into suburb(id,name) values");
    	
    	
    	int i=0;
    	
    	for( Suburb suburb : suburbs )
    	{
    		String values = "";
    		if( i++ > 0 ) values += ",";
    		values += "(" + suburb.getId() + ",'" + suburb.getName() + "')";
    		writer.write(values);
    	}
    	
    	writer.write(";");
    	
    	writer.flush();
    	writer.close();
    }
}
