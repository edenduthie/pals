package pals.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.entity.User;


@Transactional 
public class PublicServiceJPAImpl implements PublicServiceInterface {

	private static Logger logger = Logger.getLogger(PublicServiceJPAImpl.class.getCanonicalName());
	
	EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
	   this.entityManager = entityManager;
    }

	public List<DataSet> getDataSets() {
		Query query = entityManager.createQuery ( "from DataSet order by id desc" );
		List<DataSet> list = query.getResultList();
		if (list == null) list = new LinkedList<DataSet>();
		return list;
	}

	public Model getModel(Integer modelId) {
		Query query = entityManager.createQuery ( "from Model where modelId= :modelId" ).setParameter("modelId", modelId);
		return (Model)query.getSingleResult();
	}
	
	public DataSet getDataSet(Integer dataSetId) {
		Query query = entityManager.createQuery ( "from DataSet where dataSetId= :dataSetId" ).setParameter("dataSetId", dataSetId);
		return (DataSet)query.getSingleResult();
	}
	
	public ModelOutput getModelOutput(Integer modelOutputId) {
		Query query = entityManager.createQuery ( "from ModelOutput where modelOutputId= :modelOutputId" ).setParameter("modelOutputId", modelOutputId);
		return (ModelOutput)query.getSingleResult();
	}
	
	public DataSetVersion getDataSetVersion(Integer dataSetVersionId) {
		Query query = entityManager.createQuery ( "from DataSetVersion where id= :dataSetVersionId" ).setParameter("dataSetVersionId", dataSetVersionId);
		return (DataSetVersion)query.getSingleResult();
	}

	/**
	 * Return most recent DataSetVersion for this DataSet;
	 */
	public DataSetVersion getLatestDataSetVersion(Integer dataSetId) {
		Query query = entityManager.createQuery ( "from DataSetVersion where dataSetId= :dataSetId order by id Desc" ).setParameter("dataSetId", dataSetId);
		return (DataSetVersion)query.getResultList().get(0);
	}
	
	private void persistDataSet(DataSet dataSet) {
		entityManager.persist(dataSet);
	}
	
	private void persistModelOutput(ModelOutput modelOutput) {
		entityManager.persist(modelOutput);
	}
	
	public String getTopOfFile(String filePath, int numberOfLines) throws IOException {
		FileReader in = null;
		StringBuffer out = new StringBuffer();
		try {
			in = new FileReader(new File(filePath));
			BufferedReader buff = new BufferedReader(in);
			String line;
			int count=0;
			while ((line = buff.readLine()) != null && count++ < numberOfLines) {
				out.append(line);
				out.append("\n");
			}
		} finally {
			if (in != null) {
                in.close();
            }
		}
		return out.toString();
	}

	
}
