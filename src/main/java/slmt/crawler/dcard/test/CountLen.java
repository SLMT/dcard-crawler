package slmt.crawler.dcard.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import slmt.crawler.dcard.converter.json.Post;

import com.alibaba.fastjson.JSON;

public class CountLen {
	
	public static void main(String[] args) throws IOException {
		String json = loadFile(new File("/Users/SLMT/fun/posts", "17319.json"));
		Post post = JSON.parseObject(json, Post.class);
		
		System.out.println("Len: " + post.comment.get(18).version.get(0).content.length());
	}
	
	private static String loadFile(File file) throws IOException {
		// Open input stream
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		// Read file
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();

		// To string
		return response.toString();
	}
}
