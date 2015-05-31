package slmt.crawler.dcard.imagesaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.alibaba.fastjson.JSON;

public class ImageSaver {

	public static void main(String[] args) throws IOException {
		int numOfPosts = 100;
		int hasFind = 0;
		List<ImageInfo> images = new LinkedList<ImageInfo>();

		// Get all image urls
		System.out.println("Retrieving list...");
		for (int page = 1; hasFind < numOfPosts; page++) {
			// Retrieve list
			String data = retrieveData("http://www.dcard.tw/api/forum/sex/"
					+ page + "/");
			List<PostInfo> infos = JSON.parseArray(data, PostInfo.class);

			// Retrieve posts
			for (PostInfo info : infos) {
				if (!info.pinned && info.member.gender != null
						&& info.member.gender.equals("F")) {
					data = retrieveData("http://www.dcard.tw/api/post/all/"
							+ info.id);
					Post post = JSON.parseObject(data, Post.class);

					List<String> urls = getImageURLs(post.version.get(0).content);
					int num = 0;
					for (String imageUrl : urls) {
						images.add(new ImageInfo(
								convertToDownloadURL(imageUrl), info.id + "_"
										+ num + ".jpg"));
						num++;
					}
				}
				hasFind++;
			}
		}
		System.out.println("List retrieved.");

		// Download all images
		System.out.println("Start downloading images...");
		for (ImageInfo image : images)
			URLDownload.fileUrl(image.url, image.fileName, "images");
		System.out.println("Downloading completed.");
	}

	private static String retrieveData(String url) throws IOException {
		// Set request
		HttpURLConnection con = (HttpURLConnection) new URL(url)
				.openConnection();
		con.setRequestMethod("GET");

		// Check response status
		int responseCode = con.getResponseCode();
		if (responseCode != 200)
			return null;

		// Get response body
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();

		return response.toString();
	}

	private static List<String> getImageURLs(String article) {
		List<String> list = new LinkedList<String>();
		StringTokenizer tokenizer = new StringTokenizer(article);

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.contains("imgur"))
				list.add(token);
		}

		return list;
	}

	private static String convertToDownloadURL(String url) {
		if (!url.contains("i.imgur.com"))
			url = url.replace("imgur.com", "i.imgur.com") + ".jpg";
		return url;
	}
}
