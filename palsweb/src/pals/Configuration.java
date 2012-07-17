package pals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/***
 * Singleton access point for configuration information of this instance of PALS.
 * Basically we wrap a pals.properties file and turn properties in the file into
 * named attributes of the class. This means more work to add a property, but diminishes 
 * the probability of property name typing error.
 * 
 * @author stefan
 *
 */
public class Configuration {

	private Properties configFile;
	
	public String PATH_TO_APP_DATA;
	public boolean USE_CONVERT_FOR_PNGS;
	public String IMAGE_THUMB_WIDTH;
	public String IMAGE_THUMB_HEIGHT;
	public String CONVERT_CMD;
	public String CONVERT_NETFLUX_TO_NC_CMD;
	public String GMAPS_KEY;
	public String WEBAPP_URL_BASE;
	public String R_SCRIPTS_PATH;
	public String R_WRAPPER_NAME;
	public int DEFAULT_EXIT_CODE;
	public String TEMPLATE_VERSION;
	public String T1_0_1_1;
	public String T1_0_1_2;
	public Integer TEMPLATE_NUM_ROWS_1_0_1;
	public String TEMPLATE_DATE_FORMAT;
	public Integer TEMPLATE_DATE_COLUMN;
	public String QC_PLOTS_COMMAND;
	public Integer QC_PLOTS_WIDTH;
	public Integer QC_PLOTS_HEIGHT;
	public Integer PLOTS_WIDTH;
	public Integer PLOTS_HEIGHT;
	public List<String> ERROR_CODES;
	public Integer PROFILE_PICTURE_WIDTH;
	public Integer PROFILE_PICTURE_HEIGHT;
	public String SMTP_SERVER;
	public Integer SMTP_PORT;
	public String SMTP_USERNAME;
	public String SMTP_PASSWORD;
	public String FROM_EMAIL;
	public int MAX_DYNAMIC_DATA_POINTS;
	public List<String> MODEL_OUTPUT_VARIABLES;
	public List<Integer> ITEMS_PER_PAGE_OPTIONS;
	
	private Configuration() throws IOException {
		configFile = new Properties();
		configFile.load(this.getClass().getClassLoader().getResourceAsStream("pals.properties"));
		PATH_TO_APP_DATA = configFile.getProperty("PATH_TO_APP_DATA");
		USE_CONVERT_FOR_PNGS = configFile.getProperty("USE_CONVERT_FOR_PNGS").toLowerCase().equals("true");
		IMAGE_THUMB_WIDTH = configFile.getProperty("IMAGE_THUMB_WIDTH");
		IMAGE_THUMB_HEIGHT = configFile.getProperty("IMAGE_THUMB_HEIGHT");
		CONVERT_CMD = configFile.getProperty("CONVERT_CMD");
		CONVERT_NETFLUX_TO_NC_CMD = configFile.getProperty("CONVERT_NETFLUX_TO_NC_CMD");
		GMAPS_KEY = configFile.getProperty("GMAPS_KEY");
		WEBAPP_URL_BASE = configFile.getProperty("WEBAPP_URL_BASE");
		R_SCRIPTS_PATH = configFile.getProperty("R_SCRIPTS_PATH");
		R_WRAPPER_NAME = configFile.getProperty("R_WRAPPER_NAME");
		DEFAULT_EXIT_CODE = new Integer(configFile.getProperty("DEFAULT_EXIT_CODE"));
		TEMPLATE_VERSION = configFile.getProperty("TEMPLATE_VERSION");
		T1_0_1_1 = configFile.getProperty("T1_0_1_1");
		T1_0_1_2 = configFile.getProperty("T1_0_1_2");
		TEMPLATE_NUM_ROWS_1_0_1 = new Integer(configFile.getProperty("TEMPLATE_NUM_ROWS_1_0_1"));
		TEMPLATE_DATE_FORMAT = configFile.getProperty("TEMPLATE_DATE_FORMAT");
		TEMPLATE_DATE_COLUMN = new Integer(configFile.getProperty("TEMPLATE_DATE_COLUMN"));
		QC_PLOTS_COMMAND = new String(configFile.getProperty("QC_PLOTS_COMMAND"));
		QC_PLOTS_WIDTH = new Integer(configFile.getProperty("QC_PLOTS_WIDTH"));
		QC_PLOTS_HEIGHT = new Integer(configFile.getProperty("QC_PLOTS_HEIGHT"));
		PLOTS_WIDTH = new Integer(configFile.getProperty("PLOTS_WIDTH"));
		PLOTS_HEIGHT = new Integer(configFile.getProperty("PLOTS_HEIGHT"));
		PROFILE_PICTURE_WIDTH = new Integer(configFile.getProperty("PROFILE_PICTURE_WIDTH"));
		PROFILE_PICTURE_HEIGHT = new Integer(configFile.getProperty("PROFILE_PICTURE_HEIGHT"));
		SMTP_SERVER = configFile.getProperty("SMTP_SERVER");
		SMTP_PORT = new Integer(configFile.getProperty("SMTP_PORT"));
		SMTP_USERNAME = configFile.getProperty("SMTP_USERNAME");
		SMTP_PASSWORD = configFile.getProperty("SMTP_PASSWORD");
		FROM_EMAIL = configFile.getProperty("FROM_EMAIL");
		MAX_DYNAMIC_DATA_POINTS = new Integer(configFile.getProperty("MAX_DYNAMIC_DATA_POINTS"));
		
		String errorCodesString = new String(configFile.getProperty("ERROR_CODES"));
		String[] splitString = errorCodesString.split(",");
		ERROR_CODES = new ArrayList<String>();
		for( String code : splitString ) ERROR_CODES.add(code);
		
		String modelOutputVariablesString = new String(configFile.getProperty("MODEL_OUTPUT_VARIABLES"));
		splitString = modelOutputVariablesString.split(",");
		MODEL_OUTPUT_VARIABLES = new ArrayList<String>();
		for( String code : splitString ) MODEL_OUTPUT_VARIABLES.add(code);
		
		String itemsPerPageOptionsString = new String(configFile.getProperty("ITEMS_PER_PAGE_OPTIONS"));
		splitString = itemsPerPageOptionsString.split(",");
		ITEMS_PER_PAGE_OPTIONS = new ArrayList<Integer>();
		for( String option : splitString ) ITEMS_PER_PAGE_OPTIONS.add(new Integer(option));
	}
	
	private static Configuration instance;
	static {
		try {
			instance = new Configuration();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getStringProperty(String key)
	{
		return configFile.getProperty(key);
	}
	
	public int getIntProperty(String key)
	{
		return new Integer(configFile.getProperty(key)).intValue();
	}
	
	public static Configuration getInstance() {
		return instance;
	}
	
	public Properties getProperties() {
		return configFile;
	}

	public Float getFloatProperty(String key) 
	{
		return new Float(configFile.getProperty(key));
	}

}
