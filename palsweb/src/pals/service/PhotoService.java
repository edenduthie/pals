package pals.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.transaction.annotation.Transactional;

import pals.Configuration;
import pals.dao.DAO;
import pals.entity.Photo;
import pals.utils.ImageUtil;

@Transactional
public class PhotoService 
{
	DAO dao;
	
	@Transactional(rollbackFor = { IOException.class, NullPointerException.class } )
    public Photo createPhoto(File tempFile) throws IOException
    {
    	Photo photo = new Photo();
    	dao.persist(photo);
    	String dirFilename = Configuration.getInstance().PATH_TO_APP_DATA + "/photos";
    	File dir = new File(dirFilename);
    	dir.mkdirs();
    	String targetFilename = Configuration.getInstance().PATH_TO_APP_DATA
    	 + "/photos/" + photo.getId() + ".png";
    	File file = new File(targetFilename);
    	ImageUtil.shrinkImage(tempFile, file,
    	    Configuration.getInstance().PROFILE_PICTURE_WIDTH,
    	    Configuration.getInstance().PROFILE_PICTURE_HEIGHT);
    	photo.setFilename(targetFilename);
    	dao.update(photo);
    	tempFile.deleteOnExit();
    	return photo;
    }

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
}
