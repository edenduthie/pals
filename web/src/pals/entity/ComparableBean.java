package pals.entity;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

public class ComparableBean 
{
	public static final Logger log = Logger.getLogger(ComparableBean.class);
	
    @Override
    public boolean equals(Object other)
    {
    	return compareProperties(other,this);
    }
    
    public boolean compareProperties(Object o1, Object o2)
    {
    	if( o1 == null || o2 == null )
    	{
    		if( o1 == null && o2 == null ) return true;
    		else return false;
    	}
    	
    	if( o1.getClass().getName() != o2.getClass().getName() ) return false;
    	
    	try 
    	{
			BeanInfo beanInfo = Introspector.getBeanInfo(o1.getClass());
			for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors())
			{
				System.out.println(propertyDesc.getName());
				System.out.println(propertyDesc.getPropertyType().getName());
				
				if( propertyDesc.getPropertyType().getName() != "java.util.List" )
				{
					Object value1 = propertyDesc.getReadMethod().invoke(o1);
					Object value2 = propertyDesc.getReadMethod().invoke(o2);
					if( value1 == null )
					{
						if( value2 != null ) return false;
						else return true;
					}
					if( !value1.equals(value2) ) return false;
				}
			}
			return true;
		} 
    	catch (IntrospectionException e) 
    	{
			return false;
		} 
    	catch (IllegalArgumentException e) {
			return false;
		} 
    	catch (IllegalAccessException e) {
			return false;
		} 
    	catch (InvocationTargetException e) {
			return false;
		}
    }
    

}
