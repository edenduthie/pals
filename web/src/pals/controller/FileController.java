package pals.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pals.database.FileDAO;
import pals.entity.PalsFile;

@Controller
public class FileController 
{
	public static final Logger log = Logger.getLogger(FileController.class);
	
	@Autowired FileDAO fileDAO;
	
	@RequestMapping(value="/file/{fileId}")
	public @ResponseBody byte[] file(@PathVariable Integer fileId)
	{
		PalsFile photo = fileDAO.getWithData(fileId);
		if( photo != null )
		{
			return photo.getPicture();
		}
		else
		{
			log.info("File not found: " + fileId);
			return new byte[0];
		}
	}

}
