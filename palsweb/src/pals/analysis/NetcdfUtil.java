package pals.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/***
 * All logic related to interpreting NetCDF lives here.
 * 
 * @author Stefan Gregory
 *
 */
public class NetcdfUtil {

	private static Logger logger = Logger.getLogger(NetcdfUtil.class.getCanonicalName());
	
	/**
	 * Extract variables and global attributes from a <NetcdfFile> to
	 * ensure it is a valid NetCDF file.
	 * 
	 * This method is incomplete.
	 * @TODO check all essential global variables are present
	 * @TODO check all necessary variables are present
	 * 
	 * @return whether or not this is a valid NetCDF file.
	 */
	private static boolean isValidNetCDF(NetcdfFile ncfile) {
		//System.out.println(ncfile.getDetailInfo());
		List<Attribute> atList = ncfile.getGlobalAttributes();
		Iterator<Attribute> iter = atList.iterator();
		Attribute attr;
		while (iter.hasNext()) {
			attr = iter.next();
			//System.out.println(attr.getName() + "=" + attr.getValue(0));
			// @TODO check against required global variables
		}
		
		Iterator<Variable> varIter = ncfile.getVariables().iterator();
		Variable var;
		while (varIter.hasNext()) {
			var = varIter.next();
			//System.out.println(var.getName()+", " + var.getDescription()  + ": ");
			 atList = var.getAttributes();
			 iter = atList.iterator();
			while (iter.hasNext()) {
				attr = iter.next();
				//System.out.print(attr.getName() + "=" + attr.getValue(0) + ", ");
				// @TODO check against required variables
			}
			//System.out.println();
			//System.out.println();
		}
		
		return true;
	}
	
	/**
	 * @param filePath
	 * @return whether the filePath given points to a valid NetCDF file.
	 */
	public static boolean check(String filePath) {
		NetcdfFile ncfile = null;
		try {
			ncfile = NetcdfFile.open(filePath);
			return isValidNetCDF(ncfile);
		} catch (IOException ioe) {
			System.out.println("Error trying to open " + filePath + ": " + ioe.getMessage());
		} finally { 
			if (null != ncfile) try {
				ncfile.close();
			} catch (IOException ioe) {
			}
		}
		return false;
	}
	
	/**
	 * Extract global attributes from a NetCDF file and 
	 * @return a <Map>: attributeName --> <Attribute>.
	 * @param ncFile
	 */
	public static Map<String,Attribute> extractGlobalAttributeMap(NetcdfFile ncFile) {
		Map<String,Attribute> attMap = new HashMap<String,Attribute>();
		Iterator<Attribute> aIter = ncFile.getGlobalAttributes().iterator();
		Attribute att;
		while (aIter.hasNext()) {
			att = aIter.next();
			attMap.put(att.getName(), att);
		}
		return attMap;
	}
	
	/**
	 * Extract global attributes from a NetCDF file and return
	 * map where the attribute name is guaranteed upper case,
	 * to avoid case sensitivity issues. 
	 * @return a <Map>: ATTRIBUTENAME --> <Attribute>.
	 * @param ncFile
	 */
	public static Map<String,Variable> extractVariableMapUpperCase(NetcdfFile ncFile) {
		Map<String,Variable> varMap = new HashMap<String,Variable>();
		Iterator<Variable> vIter = ncFile.getVariables().iterator();
		Variable var;
		while (vIter.hasNext()) {
			var = vIter.next();
			varMap.put(var.getName().toUpperCase(), var);
		}
		return varMap;
	}
	
	/***
	 * @return a <NetcdfFile> object 
	 * @param filePath
	 */
	public static NetcdfFile parse(String filePath) throws IOException {
		NetcdfFile ncFile = null;
		try {
			ncFile = NetcdfFile.open(filePath);
		} finally { 
			if (null != ncFile) try {
				ncFile.close();
			} catch (IOException ioe) {
			}
		}
		return ncFile;
	}
	
	/**
	 * @param filePath to NetCDF file
	 * @return description of the header information
	 */
	public static String getHeaderDesc(String filePath) {
		NetcdfFile ncfile = null;
		StringBuffer desc = new StringBuffer();
		try {
			ncfile = NetcdfFile.open(filePath);
			List<Attribute> atList = ncfile.getGlobalAttributes();
			Iterator<Attribute> iter = atList.iterator();
			Attribute attr;
			while (iter.hasNext()) {
				attr = iter.next();
				desc.append(attr.getName() + "=" + attr.getValue(0) + "\n");
			}
		} catch (IOException ioe) {
			desc.append(ioe.getMessage());
			desc.append("Error opening Netcdf file");
			logger.info("Error trying to open " + filePath + ": " + ioe.getMessage());
		} finally { 
			if (null != ncfile) try {
				ncfile.close();
			} catch (IOException ioe) {
			}
		}
		return desc.toString();
	}
	
	/**
	 * Testing method.
	 */
	public static void main(String[] args) {
		NetcdfUtil netcdfUtil = new NetcdfUtil();
		//boolean test = netcdfUtil.check("/Users/stefan/Dev/CCRC/testdata/README.rtf");
		boolean test = netcdfUtil.check("/Users/stefan/Dev/CCRC/testdata/out_cable.nc");
		//boolean test = netcdfUtil.check("/Users/stefan/Dev/CCRC/testdata/metTB.nc");
		logger.info("Result is " + test);
	}
}
