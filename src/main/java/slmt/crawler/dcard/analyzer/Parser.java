package slmt.crawler.dcard.analyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Parser {
	public static List<String> getImageURLs(String article) {
		List<String> list = new LinkedList<String>();
		StringTokenizer tokenizer = new StringTokenizer(article);

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.contains("imgur"))
				list.add(token);
		}

		return list;
	}
}
