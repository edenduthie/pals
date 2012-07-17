package pals.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class HTMLUtil {

	
	public static Collection<String> getParagraphsFromString(String raw) {
		String[] bits = raw.split("\n");
		LinkedList<String> paragraphs = new LinkedList<String>();
		for (int i=0; i < bits.length; i++)
			paragraphs.add(bits[i]);
		return paragraphs;
	}
	
	
	/***
	 * Add basic HTML tags to a string so it displays as you would expect it to.
	 * Convert carriage returns to <br />
	 * Convert '&' to '&amp;' etc
	 * @param raw
	 * @return input string with added HTML tags
	 */
	public static String markUp(String raw) {
		raw = raw.replaceAll("&", "&amp;");
		raw = raw.replaceAll("'", "&#146;");
		raw = raw.replaceAll("\"", "&#148;");
		Collection<String> paras = getParagraphsFromString(raw);
		StringBuffer result = new StringBuffer();
		Iterator<String> iter = paras.iterator();
		while (iter.hasNext()) {
			result.append("<p>");
			result.append(iter.next());
			result.append("</p>\n\n");
		}
		return result.toString();
	}
}
