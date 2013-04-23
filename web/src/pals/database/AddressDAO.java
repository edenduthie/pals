package pals.database;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.entity.Address;

@Service
public class AddressDAO implements Serializable
{
	private static final long serialVersionUID = 8677980697545880691L;
	@Autowired Database db;
    
    public void put(Address address)
    {
    	db.persist(address);
    }
    public Address get(Integer id)
    {
    	return (Address) db.getNoNull(Address.class.getName(), "id", id);
    }
    public void update(Address address)
    {
    	db.update(address);
    }
    public void deleteAll()
    {
    	db.deleteAll(Address.class);
    }
}
