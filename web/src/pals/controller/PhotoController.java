package pals.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pals.database.PhotoDAO;
import pals.entity.Photo;

@Controller
public class PhotoController 
{
	public static final Logger log = Logger.getLogger(PhotoController.class);
	
	@Autowired PhotoDAO photoDAO;
	
	@RequestMapping(value="/photo/{photoId}")
	public @ResponseBody byte[] photo(@PathVariable Integer photoId)
	{
		Photo photo = photoDAO.getWithData(photoId);
		if( photo != null )
		{
			return photo.getPicture();
		}
		else
		{
			log.info("Photo not found: " + photoId);
			return new byte[0];
		}
	}

}
