package pals.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.BeanUtils;

import pals.exception.InvalidInputException;
import pals.util.Parser;

@Entity
public class Address extends ComparableBean implements Serializable
{
	private static final long serialVersionUID = 7597103605197094167L;

	@Id @GeneratedValue
	private Integer id;
	
	String firstName;
	String lastName;
	String company;
	String phone;
	String unit;
	String streetNumber;
	String street;
	String suburb;
	String postcode;
	String state;
	String lat;
	String lon;
	
	public Address() {}
	
	public Address(String firstName2, String lastName2, String company2,
			String phone2, String unit2, String streetNumber2, String street2,
			String suburb2, String postcode2, String state2, String lat2,
			String lon2) {
		firstName = firstName2;
		lastName = lastName2;
		company = company2;
		phone = phone2;
		unit = unit2;
		streetNumber = streetNumber2;
		street = street2;
		suburb = suburb2;
		postcode = postcode2;
		state = state2;
		lat = lat2;
		lon = lon2;
	}

	public void validate() throws InvalidInputException
	{
		validate(firstName);
		if( firstName.length() > 50 ) throw new InvalidInputException("First name cannot be longer than 50 characters");
		firstName = Parser.removeSpecialCharacters(firstName);
		validate(lastName);
		if( lastName.length() > 50 ) throw new InvalidInputException("Last name cannot be longer than 50 characters");
		lastName = Parser.removeSpecialCharacters(lastName);
		if( company != null ) 
		{
			if( company.length() > 50 ) throw new InvalidInputException("Company name cannot be longer than 50 characters");
			company = Parser.removeSpecialCharacters(company);
		}
		validate(phone);
		if( phone.length() > 20 ) throw new InvalidInputException("Phone cannot be longer than 20 characters");
		phone = Parser.removeSpecialCharacters(phone);
		if( unit != null ) 
		{
			if( unit.length() > 10 ) throw new InvalidInputException("Unit cannot be longer than 10 characters");
			unit = Parser.removeSpecialCharacters(unit);
		}
		validate(streetNumber);
		if( streetNumber.length() > 10 ) throw new InvalidInputException("Street number cannot be longer than 10 characters");
		streetNumber = Parser.removeSpecialCharacters(streetNumber);
		validate(street);
		if( street.length() > 80 ) throw new InvalidInputException("Street cannot be longer than 80 characters");
		street = Parser.removeSpecialCharacters(street);
		validate(suburb);
		if( suburb.length() > 50 ) throw new InvalidInputException("Suburb cannot be longer than 50 characters");
		suburb = Parser.removeSpecialCharacters(suburb);
		validate(postcode);
		postcode = Parser.removeSpecialCharacters(postcode);
		if( !postcode.matches("[0-9]{4}") ) throw new InvalidInputException("Postcode must be 4 digits");
		validate(state);
		state = Parser.removeSpecialCharacters(state);
		if( state.length() > 3 ) throw new InvalidInputException("State can be at most 3 characters");
	}
	
	public void validate(String value) throws InvalidInputException
	{
		if( value == null || value.trim().length() <=0 ) throw new InvalidInputException("Invalid address");
	}
	
	public void update(Address other)
	{
		Integer id = getId();
		BeanUtils.copyProperties(other, this);
		setId(id);
	}
	
	public Address clone()
	{
		Address address = new Address();
		address.update(this);
		return address;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
}
