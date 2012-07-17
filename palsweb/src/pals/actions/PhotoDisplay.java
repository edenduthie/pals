package pals.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import pals.dao.DAO;
import pals.entity.Photo;

public class PhotoDisplay extends UserAwareAction
{
	private InputStream inputStream;
	Integer photoId;
	DAO dao;
	
	public static final Logger log = Logger.getLogger(PhotoDisplay.class);

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public String execute()
	{
		try
		{
			if( photoId != null )
			{
				log.debug("Displaying photo: " + photoId);
				Photo photo = (Photo) dao.get(Photo.class.getName(), "id", photoId);
				inputStream = new FileInputStream(photo.getFilename());
			}
			else
			{
				log.warn("No photo id provided to PhotoDisplay");
			}
		}
		catch( FileNotFoundException e )
		{
			log.error(e.getMessage());
		}
		return SUCCESS;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
}
