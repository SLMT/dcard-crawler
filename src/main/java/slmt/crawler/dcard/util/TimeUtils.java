package slmt.crawler.dcard.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	private static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	public static String getCurrentDateString() {
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
		Date date = new Date();
		return dateFormat.format(date);
	}
}
