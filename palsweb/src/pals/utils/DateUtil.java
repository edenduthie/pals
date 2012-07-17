package pals.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static int secondsInAnHour = 3600;
	private static int secondsInADay = 3600 * 24;
	private static int secondsInAYearApprox = 3600 * 24 * 365;
	
	public static String getNiceDate(Date date) {
		Date now = new Date();
		//new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		int secondsAgo = (int) (now.getTime() - date.getTime() ) /1000;
		if (secondsAgo < secondsInAnHour) {
			// less than an hour ago
			return (secondsAgo/60) + " minutes ago";
		} else if (secondsAgo < secondsInADay){
			// less than a day ago
			return (secondsAgo/secondsInAnHour) + " hours ago";
		} else if (secondsAgo < secondsInAYearApprox) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
			return sdf.format(date);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			return sdf.format(date);
		}
	}
}
