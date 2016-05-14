package slmt.crawler.dcard.analyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import slmt.crawler.dcard.json.Post;

public class PostParser {
	
	// http[s]://imgur.com/XXXXXXX
	private static Pattern IMGUR_URL = Pattern.compile("https?:\\/\\/imgur\\.com\\/[a-zA-Z0-9]+");
	// http[s]://i.imgur.com/XXXXXXX.[jpg|png]
	private static Pattern IMGUR_FILE_URL = Pattern.compile("https?:\\/\\/i\\.imgur\\.com\\/[a-zA-Z0-9]+\\.jpg");
	
	public static List<String> getImageUrls(Post post) {
		return getImageUrls(post.version.get(0).content);
	}
	
	public static List<String> getImageUrls(String article) {
		List<String> list = new LinkedList<String>();
		
		// Try match imgur url
		Matcher matcher = IMGUR_URL.matcher(article);
		while (matcher.find())
		{
		    String fileUrl = convertImgurUrlToFileUrl(matcher.group(0));
		    list.add(fileUrl);
		}
		
		// Try match imgur file url
		matcher = IMGUR_FILE_URL.matcher(article);
		while (matcher.find())
		    list.add(matcher.group(0));

		return list;
	}
	
	private static String convertImgurUrlToFileUrl(String url) {
		// We assume it is a JPG file
		return url.replace("imgur.com", "i.imgur.com").concat(".jpg");
	}
}
