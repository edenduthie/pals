package pals.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import pals.Generator;
import pals.entity.Address;
import pals.exception.InvalidInputException;

public class AddressTest
{
	public static final String tooLong = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.";
	
    @Test
    public void testUpdate()
    {
    	Address address = Generator.address();
    	address.setId(123);
    	Address newAddress = new Address();
    	address.update(newAddress);
    	Assert.assertNull(address.getCompany());
    	Assert.assertNull(address.getFirstName());
    	Assert.assertNull(address.getLastName());
    	Assert.assertNull(address.getLat());
    	Assert.assertNull(address.getLon());
    	Assert.assertNull(address.getPhone());
    	Assert.assertNull(address.getPostcode());
    	Assert.assertNull(address.getState());
    	Assert.assertNull(address.getStreet());
    	Assert.assertNull(address.getStreetNumber());
    	Assert.assertNull(address.getSuburb());
    	Assert.assertNull(address.getUnit());
    	Assert.assertEquals(address.getId().intValue(),123);
    }
    
    @Test
    public void testValidateSpecialCharacters() throws InvalidInputException
    {
    	Address address = new Address(
    	    "#$#$#$#$Eden",
    	    "#$#*$*$Duthie",
    	    "*#'''Gator Logic''",
    	    "(61) 0438 045 662",
    	    "21^^",
    	    "115A",
    	    "Marshall Ave&&&",
    	    "Kew&&&&",
    	    "3101'",
    	    "**VIC",
    	    "-34",
    	    "25");
    	address.validate();
    	
    	Assert.assertEquals(address.getFirstName(),"Eden");
    	Assert.assertEquals(address.getLastName(),"Duthie");
    	Assert.assertEquals(address.getCompany(),"Gator Logic");
    	Assert.assertEquals(address.getPhone(),"61 0438 045 662");
    	Assert.assertEquals(address.getUnit(), "21");
    	Assert.assertEquals(address.getStreetNumber(), "115A");
    	Assert.assertEquals(address.getStreet(),"Marshall Ave");
    	Assert.assertEquals(address.getSuburb(),"Kew");
    	Assert.assertEquals(address.getPostcode(),"3101");
    	Assert.assertEquals(address.getLat(),"-34");
    	Assert.assertEquals(address.getLon(),"25");
    }
    
    @Test
    public void testValidateMissingValues() throws InvalidInputException
    {
    	Address address = new Address(
    	    "#$#$#$#$Eden",
    	    "#$#*$*$Duthie",
    	    null,
    	    "(61) 0438 045 662",
    	    null,
    	    "115A",
    	    "Marshall Ave&&&",
    	    "Kew&&&&",
    	    "3101'",
    	    "**VIC",
    	    null,
    	    null);
    	address.validate();
    	
    	Assert.assertEquals(address.getFirstName(),"Eden");
    	Assert.assertEquals(address.getLastName(),"Duthie");
    	Assert.assertNull(address.getCompany());
    	Assert.assertEquals(address.getPhone(),"61 0438 045 662");
    	Assert.assertNull(address.getUnit());
    	Assert.assertEquals(address.getStreetNumber(), "115A");
    	Assert.assertEquals(address.getStreet(),"Marshall Ave");
    	Assert.assertEquals(address.getSuburb(),"Kew");
    	Assert.assertEquals(address.getPostcode(),"3101");
    	Assert.assertNull(address.getLat());
    	Assert.assertNull(address.getLon());
    }
    
    @Test
    public void testValidateInvalidPostcodeWrongLength()
    {
        Address address = Generator.address();
        address.setPostcode("444");
        try
        {
        	address.validate();
        	Assert.fail("Postcode not 4 digits");
        }
        catch(InvalidInputException e) {}
    }
    
    @Test
    public void testValidateInvalidPostcodeCharacter()
    {
        Address address = Generator.address();
        address.setPostcode("444A");
        try
        {
        	address.validate();
        	Assert.fail("Postcode accepted character");
        }
        catch(InvalidInputException e) {}
    }
    
    @Test
    public void testValidateStateTooLong()
    {
        Address address = Generator.address();
        address.setState("Victoria");
        try
        {
        	address.validate();
        	Assert.fail("State too long");
        }
        catch(InvalidInputException e) {}
    }
    
    @Test
    public void testValidateFirstNameTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setFirstName(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidateLastNameTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setLastName(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidateCompanyTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setCompany(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidatePhoneTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setPhone(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidateUnitTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setUnit(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidateStreetNumberTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setStreetNumber(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidateStreetTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setStreet(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
    
    @Test
    public void testValidateSuburbTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setStreet(tooLong);
    	try
    	{
    		address.validate();
    		Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    }
}
