package pals.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import pals.Globals;
import pals.entity.DataSetVersion;
import pals.service.PublicServiceInterface;
import pals.utils.PalsFileUtils;


/***
 * 
 * This action returns the actual DataSet raw file (NetCDF) as a data stream.
 *
 * @author Stefan Gregory
 *
 */
public class DownloadDataSet extends UserAwareAction {
	
	private static final Logger log = Logger.getLogger(DownloadDataSet.class);

	private InputStream inputStream;
	private Integer dataSetVersionId;
	private Integer dataSetId;
	private DataSetVersion dataSetVersion;
	
	private String downloadContentDisposition;
	
	private PublicServiceInterface publicService;
	
	public PublicServiceInterface getPublicService() {
		return publicService;
	}

	public void setPublicService(PublicServiceInterface publicService) {
		this.publicService = publicService;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public Integer getDataSetVersionId() {
		if (dataSetVersionId == null && dataSetId != null) {
			dataSetVersionId = getLatestDataSetVersion(dataSetId).getId();
		}
		return dataSetVersionId;
	}
	
	public DataSetVersion getDataSetVersion() {
		if (dataSetVersion == null) {
			if (dataSetVersionId != null) {
				dataSetVersion = getPublicService().getDataSetVersion(dataSetVersionId);
			} else if (dataSetId != null) {
				dataSetVersion = getLatestDataSetVersion(dataSetId);
			} else {
				// return null
			}
		}
		return dataSetVersion;
	}

	public void setDataSetVersionId(Integer dataSetVersionId) {
		this.dataSetVersionId = dataSetVersionId;
	}
	
	public Integer getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(Integer dataSetId) {
		this.dataSetId = dataSetId;
	}

	public String getContentDisposition() {
		return getDownloadContentDisposition();
	}
	
	public DataSetVersion getLatestDataSetVersion(Integer dataSetId) {
		return getPublicService().getLatestDataSetVersion(dataSetId);
	}

	public String met() {
		DataSetVersion dataSetVersion = getDataSetVersion();
		File dsFile = PalsFileUtils.getDataSetVersionMetFile(dataSetVersion);
		String fileName =  "filename=\"" + dataSetVersion.getDisplayName() + "_met" + Globals.NETCDF_FILE_SUFFIX + "\""; 
		setDownloadContentDisposition(fileName);
		try {
			inputStream = new FileInputStream(dsFile);
			getUserService().incrementDataSetDownloadCount(dataSetVersion.getDataSet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String flux() {
		DataSetVersion dataSetVersion = getDataSetVersion();
		File dsFile = PalsFileUtils.getDataSetVersionFluxFile(dataSetVersion);
		String fileName =  "filename=\"" + dataSetVersion.getDisplayName() + "_flux" + Globals.NETCDF_FILE_SUFFIX + "\""; 
		setDownloadContentDisposition(fileName);
		try {
			inputStream = new FileInputStream(dsFile);
			getUserService().incrementDataSetDownloadCount(dataSetVersion.getDataSet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String original() {
		DataSetVersion dataSetVersion = getDataSetVersion();
		String originalFileName = dataSetVersion.getOriginalFileName();
		String fileName = dataSetVersion.uploadedFilePath();
System.out.println("FILENAME: " + fileName);
		File dsFile = new File(fileName);
		setDownloadContentDisposition("attachment; filename=" + originalFileName);
		try {
			inputStream = new FileInputStream(dsFile);
			getUserService().incrementDataSetDownloadCount(dataSetVersion.getDataSet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getDownloadContentDisposition() {
		return downloadContentDisposition;
	}

	public void setDownloadContentDisposition(String downloadContentDisposition) {
		this.downloadContentDisposition = downloadContentDisposition;
	}
}
