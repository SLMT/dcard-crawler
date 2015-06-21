package slmt.crawler.dcard;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.util.IOUtils;

import com.alibaba.fastjson.JSON;

public class Test {

	public static void main(String[] args) throws IOException {
		// 讀取 json 字串
		String json = IOUtils.loadAsString(new File("C:/Users/SLMT/Desktop/dcard_100000", "250000.json"));
		
		// 將文章轉成 java 物件
		Post post = JSON.parseObject(json, Post.class);
		
		// => 再存文章
		StringTokenizer st = new StringTokenizer(post.version.get(0).content, "\r\n");
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			System.out.println(token);
			/*
			System.out.print("Len: " + token.length() + ", token: [");
			char[] chars = token.toCharArray();
			for (char ch : chars)
				System.out.print((int)ch + " ");
			System.out.println("]");
			*/
		}
	}

}
