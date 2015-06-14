package slmt.crawler.dcard.analyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Parser {
	
	private static final int IMGUR_URL_LEN = 24; 
	
	public static void main(String[] args) {
		getImageURLs("秘書：http://imgur.com/S9omRBW");
	}
	
	public static List<String> getImageURLs(String article) {
		List<String> list = new LinkedList<String>();
		StringTokenizer tokenizer = new StringTokenizer(article);

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.contains("imgur")) {
				int start = token.indexOf("http");
				list.add(token.substring(start, start + IMGUR_URL_LEN));
			}
		}

		return list;
	}
}
