package pals.actions.admin;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.log4j.Logger;

import pals.actions.UserAwareAction;
import pals.service.AnalysisServiceInterface;


public class ReRun extends UserAwareAction {

	static final Logger log = Logger.getLogger(ReRun.class);
	String message;
	
	AnalysisServiceInterface analysisService;

	public String execute() {
		if(!getUser().isAdmin())
		{
			message = "Only administrators can re-run analysis";
			return ERROR;
		}
		log.info("Starting ReRun of COMPLETE and ERROR Analysis");
		Thread t = new AnalysisReRun(analysisService);
		t.start();
		return SUCCESS;
	}

	public AnalysisServiceInterface getAnalysisService() {
		return analysisService;
	}

	public void setAnalysisService(AnalysisServiceInterface analysisService) {
		this.analysisService = analysisService;
	}
	
	public class AnalysisReRun extends Thread
	{
		AnalysisServiceInterface analysisService;
		
		public AnalysisReRun(AnalysisServiceInterface analysisService)
		{
			super();
			this.analysisService = analysisService;
		}
		
		public void run()
		{
			analysisService.reRunAnalysisRuns();
		}
	}

	public int length() {
		return message.length();
	}

	public boolean isEmpty() {
		return message.isEmpty();
	}

	public char charAt(int index) {
		return message.charAt(index);
	}

	public int codePointAt(int index) {
		return message.codePointAt(index);
	}

	public int codePointBefore(int index) {
		return message.codePointBefore(index);
	}

	public int codePointCount(int beginIndex, int endIndex) {
		return message.codePointCount(beginIndex, endIndex);
	}

	public int offsetByCodePoints(int index, int codePointOffset) {
		return message.offsetByCodePoints(index, codePointOffset);
	}

	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		message.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
		message.getBytes(srcBegin, srcEnd, dst, dstBegin);
	}

	public byte[] getBytes(String charsetName)
			throws UnsupportedEncodingException {
		return message.getBytes(charsetName);
	}

	public byte[] getBytes(Charset charset) {
		return message.getBytes(charset);
	}

	public byte[] getBytes() {
		return message.getBytes();
	}

	public boolean equals(Object anObject) {
		return message.equals(anObject);
	}

	public boolean contentEquals(StringBuffer sb) {
		return message.contentEquals(sb);
	}

	public boolean contentEquals(CharSequence cs) {
		return message.contentEquals(cs);
	}

	public boolean equalsIgnoreCase(String anotherString) {
		return message.equalsIgnoreCase(anotherString);
	}

	public int compareTo(String anotherString) {
		return message.compareTo(anotherString);
	}

	public int compareToIgnoreCase(String str) {
		return message.compareToIgnoreCase(str);
	}

	public boolean regionMatches(int toffset, String other, int ooffset, int len) {
		return message.regionMatches(toffset, other, ooffset, len);
	}

	public boolean regionMatches(boolean ignoreCase, int toffset, String other,
			int ooffset, int len) {
		return message.regionMatches(ignoreCase, toffset, other, ooffset, len);
	}

	public boolean startsWith(String prefix, int toffset) {
		return message.startsWith(prefix, toffset);
	}

	public boolean startsWith(String prefix) {
		return message.startsWith(prefix);
	}

	public boolean endsWith(String suffix) {
		return message.endsWith(suffix);
	}

	public int hashCode() {
		return message.hashCode();
	}

	public int indexOf(int ch) {
		return message.indexOf(ch);
	}

	public int indexOf(int ch, int fromIndex) {
		return message.indexOf(ch, fromIndex);
	}

	public int lastIndexOf(int ch) {
		return message.lastIndexOf(ch);
	}

	public int lastIndexOf(int ch, int fromIndex) {
		return message.lastIndexOf(ch, fromIndex);
	}

	public int indexOf(String str) {
		return message.indexOf(str);
	}

	public int indexOf(String str, int fromIndex) {
		return message.indexOf(str, fromIndex);
	}

	public int lastIndexOf(String str) {
		return message.lastIndexOf(str);
	}

	public int lastIndexOf(String str, int fromIndex) {
		return message.lastIndexOf(str, fromIndex);
	}

	public String substring(int beginIndex) {
		return message.substring(beginIndex);
	}

	public String substring(int beginIndex, int endIndex) {
		return message.substring(beginIndex, endIndex);
	}

	public CharSequence subSequence(int beginIndex, int endIndex) {
		return message.subSequence(beginIndex, endIndex);
	}

	public String concat(String str) {
		return message.concat(str);
	}

	public String replace(char oldChar, char newChar) {
		return message.replace(oldChar, newChar);
	}

	public boolean matches(String regex) {
		return message.matches(regex);
	}

	public boolean contains(CharSequence s) {
		return message.contains(s);
	}

	public String replaceFirst(String regex, String replacement) {
		return message.replaceFirst(regex, replacement);
	}

	public String replaceAll(String regex, String replacement) {
		return message.replaceAll(regex, replacement);
	}

	public String replace(CharSequence target, CharSequence replacement) {
		return message.replace(target, replacement);
	}

	public String[] split(String regex, int limit) {
		return message.split(regex, limit);
	}

	public String[] split(String regex) {
		return message.split(regex);
	}

	public String toLowerCase(Locale locale) {
		return message.toLowerCase(locale);
	}

	public String toLowerCase() {
		return message.toLowerCase();
	}

	public String toUpperCase(Locale locale) {
		return message.toUpperCase(locale);
	}

	public String toUpperCase() {
		return message.toUpperCase();
	}

	public String trim() {
		return message.trim();
	}

	public String toString() {
		return message.toString();
	}

	public char[] toCharArray() {
		return message.toCharArray();
	}

	public String intern() {
		return message.intern();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
