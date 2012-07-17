package pals.analysis;

import pals.PalsException;

/***
 * A base class for all exceptions thrown by analysis code: 
 * that is, anything to do with parsing NetCDF or running R scripts.
 *
 * @author Stefan Gregory
 */
public class AnalysisException extends PalsException {

	public AnalysisException(String message) {
		super(message);
	}
}
