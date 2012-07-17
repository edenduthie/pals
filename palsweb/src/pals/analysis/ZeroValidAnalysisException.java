package pals.analysis;

/***
 * Thrown if no analyses can run on a <ModelOutput>.
 */
public class ZeroValidAnalysisException extends AnalysisException {
	
	public ZeroValidAnalysisException(String message) {
		super(message);
	}
}
