package pals;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import pals.database.FileDAO;
import pals.database.PalsGrantedAuthorityDAO;
import pals.database.PalsUserDAO;
import pals.database.PhotoDataDAO;
import pals.entity.PalsGrantedAuthority;
import pals.exception.InvalidInputException;
import pals.service.AccountService;
import pals.service.FileService;

public class GenerateData extends BaseTest
{
    @Autowired PalsUserDAO palsUserDAO;
    @Autowired FileDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    @Autowired FileService photoService;
    @Autowired AccountService accountService;
    @Autowired PalsGrantedAuthorityDAO palsGrantedAuthorityDAO;

    
    
    
//    @Test
    public void createProductionData() throws IOException, InvalidInputException
    {
    	PalsGrantedAuthority auth = Generator.palsGrantedAuthority();
    	auth.setAuthority(PalsGrantedAuthority.ADMIN_ROLE);
    	palsGrantedAuthorityDAO.put(auth);
    	
//    	createSuburbs();
    }
}
