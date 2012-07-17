package pals.actions.account;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import pals.dao.UserDAO;
import pals.entity.Photo;
import pals.entity.User;
import pals.service.PhotoService;
import pals.utils.LSMEvalConstants;

import com.opensymphony.xwork2.ActionSupport;

public class Register extends ActionSupport implements SessionAware {

	User userToCreate;
	UserDAO userDAO;
	Map<String, Object> session;
	String passwordConfirm;
	private File file;
    private String contentType;
    private String filename;
    
    PhotoService photoService;
    
    private static final Logger log = Logger.getLogger(Register.class);
	
	public String execute() {
		if( userToCreate == null ) return INPUT;
		
		if( userToCreate.getUsername().equals(userToCreate.getPassword()) )
		{
			addFieldError("userToCreate.password","Username and password cannot be the same");
			return ERROR;
		}
		
		if( !userToCreate.getPassword().equals(passwordConfirm) )
		{
			addFieldError("userToCreate.password","Password fields do not match");
			return ERROR;
		}

		try
		{
			userToCreate.encryptPassword();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			addFieldError("userToCreate.password","Error encrypting password");
			return ERROR;
		}
		if( file != null && filename != null )
		{
			if( contentType.equals("image/jpeg") || contentType.equals("image/gif") || contentType.equals("image/png") )
			{
				try 
				{
					Photo photo = photoService.createPhoto(file);
					userToCreate.setPhoto(photo);
				} 
				catch (IOException e) 
				{
					log.info(e);
					addFieldError("uploadFile","Oops, the photo upload failed for some reason");
				}
			}
			else
			{
				addFieldError("uploadFile","Photo must be a jpeg, png, or gif");
				log.info("Uploaded invalid photo file: " + filename);
			}
		}
		
		if( userDAO.usernameExists(userToCreate.getUsername()) )
		{
			addFieldError("userToCreate.username","Username taken, please choose another");
			return ERROR;
		}
		else
		{
			userToCreate.setAdmin(false);
			userDAO.create(userToCreate);
			session.put(LSMEvalConstants.USER,userToCreate); // log in automatically
		}
		
		return SUCCESS;
	}

	public User getUserToCreate() {
		return userToCreate;
	}

	public void setUserToCreate(User userToCreate) {
		this.userToCreate = userToCreate;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
    public void setUpload(File file) {
        this.file = file;
     }

     public void setUploadContentType(String contentType) {
        this.contentType = contentType;
     }

     public void setUploadFileName(String filename) {
        this.filename = filename;
     }

	public PhotoService getPhotoService() {
		return photoService;
	}

	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}
}
