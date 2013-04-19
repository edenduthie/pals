package pals.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

/**
 * To be used as the JPADialect in the entityManagerFactory declaration in applicationContext.xml
 * It allows us to specify the isolation level of the transaction per method using the Transaction
 * annotation and isolation=
 * 
 * @author eduthie
 */
public class HibernateExtendedJpaDialect extends HibernateJpaDialect {
	
	private static final long serialVersionUID = -246366978187404608L;
	public static final Logger log = Logger.getLogger(HibernateExtendedJpaDialect.class);

    @Override
    public Object beginTransaction(EntityManager entityManager, final TransactionDefinition definition) throws PersistenceException,
            SQLException, TransactionException {

        Session session = (Session) entityManager.getDelegate();
        
        
        DataSourceUtils.prepareConnectionForTransaction(session.connection(), definition);
        if( definition.getIsolationLevel() >= 0)
        {
        	log.debug("Setting isolation level: " + definition.getIsolationLevel());
            session.connection().setTransactionIsolation(definition.getIsolationLevel());
        }
        
        session.doWork(new Work() 
        {
        	public void execute(Connection connection) throws SQLException 
        	{
        	    DataSourceUtils.prepareConnectionForTransaction(connection, definition);
        	    if( definition.getIsolationLevel() >= 0)
        	    {
        	    	log.debug("Setting isolation level: " + definition.getIsolationLevel());
                    connection.setTransactionIsolation(definition.getIsolationLevel());
        	    }
            }
        });
        
        
        session.connection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // set all isolation to SEZIALIZABLE
        entityManager.getTransaction().begin();
        return prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());
    }

}
