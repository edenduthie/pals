package pals.actions.upload;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import pals.Configuration;
import pals.entity.User;
import pals.utils.LSMEvalConstants;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Upload extends ActionSupport implements ServletRequestAware
{
	HttpServletRequest request;
	static Logger log = Logger.getLogger(Upload.class);
	
	File filedata;
	String filename;
	String contentType;
	String username;
	
	public String execute()
	{
//		User user = (User) ActionContext.getContext().get(LSMEvalConstants.USER);
//		if( user == null )
//		{
//			log.error("Illegal access to file upload");
//			return ERROR;
//		}
//		username = user.getUsername();
		String filePath = Configuration.getInstance().PATH_TO_APP_DATA + "/" + username + "/" + filename;
		System.out.println("saving file: " + filePath);
		try 
		{
			FileUtils.copyFile(filedata,new File(filePath));
		} 
		catch (IOException e) 
		{
			log.error(e.getMessage());
		}
		System.out.println("finished saving");
		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
	public void setFiledata(File file)
	{
        this.filedata = file;		
	}
	
	public void setFiledataFileName(String name)
	{
		this.filename = name;
	}
	
	public void setFiledataContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
