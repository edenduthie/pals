package pals.actions.account;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.hibernate.classic.Validatable;

import pals.actions.UserAwareAction;
import pals.dao.UserDAO;
import pals.entity.Photo;
import pals.entity.User;
import pals.service.PhotoService;

public class Account extends UserAwareAction implements Validatable {
	
	public static final Logger log = Logger.getLogger(Account.class);
	
	User userToEdit;
	UserDAO userDAO;
	String passwordConfirm;
	private File file;
    private String contentType;
    private String filename;
    
    PhotoService photoService;
	
	public String execute()
	{
		getUser().setFullName(userToEdit.getFullName());
		getUser().setEmail(userToEdit.getEmail());
		getUser().setInstitution(userToEdit.getInstitution());
		getUser().setResearchInterest(userToEdit.getResearchInterest());
		getUser().setShowEmail(userToEdit.getShowEmail());
		if( file != null && filename != null )
		{
			if( contentType.equals("image/jpeg") || contentType.equals("image/gif") || contentType.equals("image/png") )
			{
				try 
				{
					Photo photo = photoService.createPhoto(file);
					getUser().setPhoto(photo);
					System.out.println("Set photo");
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
		if( userToEdit.getPassword() != null && userToEdit.getPassword().trim().length() > 0 )
		{
			if( !userToEdit.getPassword().equals(passwordConfirm) )
			{
				addFieldError("userToEdit.password","Passwords do not match");
				log.debug("Mismatched passwords");
				return ERROR;
			}
			getUser().setPassword(getUserToEdit().getPassword().trim());
			try 
			{
				getUser().encryptPassword();
			} 
			catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage());
				addFieldError("userToEdit.password","Failed to encrypt password");
				return ERROR;
			}
		}
		
		userDAO.update(getUser());
	    return SUCCESS;
	}
	
	/**
	 * The validation code for the password is in here because we don't require the user
	 * to change their password if they do not wish to, and if we used the validation framework
	 * it would check password length, etc..
	 */
	public void validate()
	{
		String password = getUserToEdit().getPassword();
		password = password.trim();
		if( password != null && password.length() > 0 )
		{
			if( password.length() <= 5 )
			{
				addFieldError("userToEdit.password","Password must be greater than 5 characters");
			}
			if( !password.matches("^[^\\s]+$") )
			{
				addFieldError("userToEdit.password","No whitespace is allowed in password");
			}
		}
	}

	public User getUserToEdit() {
		return userToEdit;
	}

	public void setUserToEdit(User userToEdit) {
		this.userToEdit = userToEdit;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public PhotoService getPhotoService() {
		return photoService;
	}

	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
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
}
