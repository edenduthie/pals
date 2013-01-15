package pals;

/***
 * Base class for all exceptions in PALS.
 *  
 * @author Stefan
 *
 */
public class PalsException extends Exception {

	public PalsException(String message) {
		super(message);
	}
	public PalsException(String message, Exception e) {
		super(message, e);
	}
}
