package pals.analysis;

import java.util.List;

/***
 * Thrown when an uploaded ModelOutput is missing
 * and essential global attribute.
 *
 */
public class MissingGlobalAttributesException extends AnalysisException {

	List<String> missingAttributes;
	
	public MissingGlobalAttributesException(List<String> missing) {
		super(missing.toString());
		this.missingAttributes = missing;
	}

	public List<String> getMissingAttributes() {
		return missingAttributes;
	}

	public void setMissingAttributes(List<String> missingAttributes) {
		this.missingAttributes = missingAttributes;
	}
	
}
