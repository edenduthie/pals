package pals.entity;

import java.math.BigDecimal;

public class Location 
{
    private Double lat;
    private Double lon;
    private static final int PRECISION_DP = 4;
    
    public void setLat(String degString, String minString, String secString)
    {
    	lat = convertToDecimal(degString,minString,secString);
    }
    
    public void setLon(String degString, String minString, String secString)
    {
    	lon = convertToDecimal(degString, minString, secString);
    }
    
    public Double convertToDecimal(String degString, String minString, String secString)
    {
    	Integer deg = 0;
    	if( degString != null && degString.length() > 0 ) deg = Integer.valueOf(degString);
    	Integer min = 0;
    	if( minString != null && minString.length() > 0 ) min = Integer.valueOf(minString);
    	Double sec = 0.0;
    	if( secString != null && secString.length() > 0 ) sec = Double.valueOf(secString);
    	
    	Double decimal = deg.doubleValue() + (min.doubleValue()/60.0) + (sec/(60.0*60.0));
    	return decimal;
    }
    
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	
	public Double getLatRounded()
	{
		return round(getLat());
	}
	
	public Double getLonRounded()
	{
		return round(getLon());
	}
	
	public Double round(Double d)
	{
        return (int)(d * 10000 + 0.5) / 10000.0;
	}
  
    
}
