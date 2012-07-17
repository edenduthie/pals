package pals.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import pals.dao.DAO;
import pals.entity.FileData;
import pals.entity.PalsFile;

@Transactional
public class FileService 
{
	public DAO dao;
	private static final int BUFFER = 1024;
	
	private static final Logger log = Logger.getLogger(FileService.class);
	
	public PalsFile createFile(File file) throws IOException
	{
		PalsFile palsFile = new PalsFile();
		List<Byte> byteList = new ArrayList<Byte>();
		byte[] buffer = new byte[BUFFER];
		InputStream is = new FileInputStream(file);
		int length = 0;
		while( (length = is.read(buffer,0,BUFFER)) > 0 )
		{
			for( int i=0; i < length; ++i )
			{
				byteList.add(buffer[i]);
			}
		}
		byte[] data = new byte[byteList.size()];
		for(int i=0; i < byteList.size(); ++i )
		{
			data[i] = byteList.get(i);
		}
		palsFile.setData(new FileData(data));
		is.close();
		file.delete();
		return palsFile;
	}
	
	public PalsFile copy(PalsFile original)
	{
		PalsFile copy = new PalsFile();
		BeanUtils.copyProperties(original,copy);
		byte[] copyData = new byte[original.getData().length()];
		for( int i=0; i < copyData.length; ++i )
		{
			copyData[i] = original.getData().getData()[i];
		}
		copy.getData().setData(copyData);
		return copy;
	}
	
	public List<PalsFile> copy(List<PalsFile> fileList )
	{
		List<PalsFile> newList = new ArrayList<PalsFile>();
		for( PalsFile file : fileList )
		{
			newList.add(copy(file));
		}
		return newList;
	}
	
	public void save(PalsFile file)
	{
		dao.persist(file);
	}
	
	public PalsFile get(Integer id)
	{
		String queryString = "from PalsFile f join fetch f.data d where f.id=:id";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("id",id);
		return (PalsFile) query.getSingleResult();
	}
	
	public PalsFile getNoData(Integer id)
	{
		String queryString = "from PalsFile f where f.id=:id";
		Query query = dao.getEntityManager().createQuery(queryString);
		query.setParameter("id",id);
		return (PalsFile) query.getSingleResult();
	}
	
	public void deleteFile(PalsFile file)
	{
		dao.delete(PalsFile.class.getName(),"id",file.getId());
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
}
