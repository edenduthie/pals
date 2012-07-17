package pals.actions.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pals.Configuration;
import pals.actions.UserAwareAction;

public class FileAction extends UserAwareAction
{
	private InputStream inputStream;
	private String filename;
	private String username;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String execute() {
		
		String outputFilename = Configuration.getInstance().PATH_TO_APP_DATA +
		     "/" + username +
		     "/" + filename;
		File outputFile = new File(outputFilename);
		
		try {
			inputStream = new FileInputStream(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
