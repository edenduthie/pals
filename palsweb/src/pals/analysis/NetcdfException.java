package pals.analysis;

import pals.PalsException;

/***
 * Thrown when something has gone wrong reading or parsing a NetCDF file.
 */
public class NetcdfException extends PalsException {

	public NetcdfException(String message) {
		super(message);
	}
}
