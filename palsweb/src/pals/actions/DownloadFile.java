package pals.actions;

import java.io.InputStream;

import pals.entity.PalsFile;
import pals.service.FileService;

public class DownloadFile extends UserAwareAction
{
	InputStream inputStream;
	String downloadContentDisposition;
	Integer id;
	FileService fileService;
	String contentType;
	
	public String execute()
	{
		PalsFile file = fileService.get(id);
		inputStream = file.getData().getInputStream();
		setDownloadContentDisposition("attachment;filename=" + file.getName());
		setContentType(file.getContentType());
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDownloadContentDisposition() {
		return downloadContentDisposition;
	}

	public void setDownloadContentDisposition(String downloadContentDisposition) {
		this.downloadContentDisposition = downloadContentDisposition;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
