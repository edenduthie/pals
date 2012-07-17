package pals;

/***
 * 
 * Some global properties defined here that are independent of
 * deployment hence they are not in pals.properites, hence not wrapped
 * by <Configuration>.
 * 
 * @author stefan
 *
 */
public class Globals {

	public static String MODEL_OUTPUT_DIR_PREFIX  = "mo";
	public static String DATA_SET_DIR_PREFIX      = "ds";
	public static String MODEL_OUTPUT_FILE_PREFIX = "mo";
	public static String BENCH_FILE_PREFIX = "bc";
	public static String DATA_SET_FILE_PREFIX     = "ds";
	public static String ANALYSIS_RUN_FILE_PREFIX = "a";
	public static String ANALYSIS_RUN_FILE_PREFIX_BENCH = "abc";
	public static String NETCDF_FILE_SUFFIX       = ".nc";
	public static String PDF_FILE_SUFFIX          = ".pdf";
	public static String PNG_FILE_SUFFIX          = ".png";
	public static String THUMB_FILE_SUFFIX        = "_thumb.png";
	public static String ALLOWED_EXTENSION        = "csv";
    public static String SEPARATOR = ",";	
    public static Character REPLACEMENT_CHAR = '_';
    
    public static String SORT_ASC = "ASC";
    public static String SORT_DESC = "DESC";
    public static String SORT_NONE = "NONE";
}
