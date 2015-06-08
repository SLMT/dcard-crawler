package slmt.crawler.dcard.api;

public class ImgurAPI {
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String convertToDownloadURL(String url) {
		if (!url.contains("i.imgur.com"))
			url = url.replace("imgur.com", "i.imgur.com") + ".jpg";
		return url;
	}
	
}
