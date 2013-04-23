package pals;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import pals.database.AddressDAO;
import pals.database.PalsGrantedAuthorityDAO;
import pals.database.PalsUserDAO;
import pals.database.PhotoDAO;
import pals.database.PhotoDataDAO;
import pals.database.SuburbDAO;
import pals.entity.PalsGrantedAuthority;
import pals.entity.Suburb;
import pals.exception.InvalidInputException;
import pals.service.AccountService;
import pals.service.PhotoService;

public class GenerateData extends BaseTest
{
    @Autowired PalsUserDAO palsUserDAO;
    @Autowired PhotoDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    @Autowired PhotoService photoService;
    @Autowired AccountService accountService;
    @Autowired PalsGrantedAuthorityDAO palsGrantedAuthorityDAO;
    @Autowired SuburbDAO suburbDAO;
    @Autowired AddressDAO addressDAO;

    
    public void createSuburbs()
    {
    	try
    	{
    	    BufferedReader fileReader = new BufferedReader(new FileReader(new File("data/suburbs.csv")));
    	    String line = fileReader.readLine();
    	    while( line != null )
    	    {
    	    	Suburb suburb = new Suburb(line.trim());
    	    	suburbDAO.put(suburb);
    	    	line = fileReader.readLine();
    	    }
    	}
    	catch( FileNotFoundException e )
    	{
    		e.printStackTrace();
    	} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    
    
//    @Test
    public void createProductionData() throws IOException, InvalidInputException
    {
    	PalsGrantedAuthority auth = Generator.palsGrantedAuthority();
    	auth.setAuthority(PalsGrantedAuthority.ADMIN_ROLE);
    	palsGrantedAuthorityDAO.put(auth);
    	
//    	createSuburbs();
    }
}
