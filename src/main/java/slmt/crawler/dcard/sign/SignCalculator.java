package slmt.crawler.dcard.sign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

import slmt.crawler.dcard.converter.json.Comment;
import slmt.crawler.dcard.converter.json.Post;
import slmt.crawler.dcard.converter.json.User;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class SignCalculator {

	private static String inputDirName;
	private static String outputFileName;

	private static final int MAX_SIGN = 40;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static final Long ignoreDate;

	private static HashMap<String, SignCountRecord> signCounts, departCounts;

	private static class SignCountRecord {
		int count = 0;
		String firstOccur = "Nope";
		long firstOccurTime = Long.MAX_VALUE;
		HashMap<String, AtomicLong> fourmCounts = new HashMap<String, AtomicLong>();

		void updateFirstOccur(String position, long time) {
			if (time < firstOccurTime) {
				firstOccur = position;
				firstOccurTime = time;
			}
		}

		void addFourmCount(String fourm) {
			AtomicLong count = fourmCounts.get(fourm);
			if (count == null) {
				count = new AtomicLong();
				fourmCounts.put(fourm, count);
			}
			count.incrementAndGet();
		}

		String getHighestFourm() {
			String maxFourm = null;
			long maxCount = -1;

			for (Entry<String, AtomicLong> entry : fourmCounts.entrySet()) {
				String fourm = entry.getKey();
				long count = entry.getValue().get();

				if (count > maxCount) {
					maxFourm = fourm;
					maxCount = count;
				}
			}

			return maxFourm;
		}
	}

	static {
		long time = 0;
		try {
			time = DATE_FORMAT.parse("2014-12-22T00:00:00.000Z").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ignoreDate = time;
		// ignoreDate = 0L;

		signCounts = new HashMap<String, SignCountRecord>();
		departCounts = new HashMap<String, SignCountRecord>();
		departCounts.put("猼訑", new SignCountRecord());
		departCounts.put("西門慶", new SignCountRecord());
		departCounts.put("清大小吃部", new SignCountRecord());
		departCounts.put("陳冠希", new SignCountRecord());
		departCounts.put("牙壓學系", new SignCountRecord());
	}

	public static void main(String[] args) {

		// Check the number of arguments
		if (args.length < 2) {
			System.out.println("Arguments: [Input Dir] [Output File]");
			return;
		}

		// Get arguments
		inputDirName = args[0];
		outputFileName = args[1];

		try {
			// Timer
			long totalStart = System.nanoTime();

			// Read the list of input files
			File inputDir = new File(inputDirName);
			String[] inputFiles = inputDir.list();
			// String[] inputFiles = {"67824.json"};

			// Create output files
			BufferedWriter resultOut = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputFileName)));

			// Process files
			int processed = 0;
			for (String fileName : inputFiles) {
				String idPart = fileName.substring(0, 5);
				String typePart = fileName.substring(6);

				if (typePart.equals("json")) {
					String json = loadFile(new File(inputDirName, fileName));
					int postId = Integer.parseInt(idPart);

					// Read JSON
					Post post = null;
					try {
						post = JSON.parseObject(json, Post.class);
					} catch (JSONException e) {
						e.printStackTrace();
						System.out.println("Exception on " + postId);
						continue;
					}

					// Process Sign
					processSign(postId, post);

					// Log
					processed++;
					if (processed % 1000 == 0)
						System.out.println(processed
								+ " posts have been processed.");
				}
			}

			// Print results
			for (Entry<String, SignCountRecord> signCount : signCounts
					.entrySet()) {
				String sign = signCount.getKey();
				SignCountRecord record = signCount.getValue();
				if (record.count > 10)
					resultOut.append("\"" + sign + "\"\t" + record.count + "\t"
							+ record.firstOccur + "\t"
							+ record.getHighestFourm() + "\n");
			}
			for (Entry<String, SignCountRecord> signCount : departCounts
					.entrySet()) {
				String sign = signCount.getKey();
				SignCountRecord record = signCount.getValue();
				if (record.count > 10)
					resultOut.append("\"" + sign + "\"\t" + record.count + "\t"
							+ record.firstOccur + "\t"
							+ record.getHighestFourm() + "\n");
			}

			// Close files
			resultOut.close();

			// Timer end
			long totalTime = System.nanoTime() - totalStart;
			System.out.println("Whole program took " + totalTime / 1000000
					+ "ms.");

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private static void processSign(int postId, Post post) {
		try {
			findAndCountSign(
					"http://www.dcard.tw/f/meow/p/" + postId + "\tTop",
					post.version.get(0).content,
					DATE_FORMAT.parse(post.createdAt).getTime(),
					post.forum_alias, post.member);
			int floor = 1;
			for (Comment com : post.comment)
				findAndCountSign("http://www.dcard.tw/f/meow/p/" + postId
						+ "\tB" + floor++, com.version.get(0).content,
						DATE_FORMAT.parse(com.version.get(0).createdAt)
								.getTime(), post.forum_alias, com.member);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static void findAndCountSign(String position, String content,
			long time, String fourm, User member) {
		if (time > ignoreDate) {
			// Find department first
			if (member != null) {
				SignCountRecord record = departCounts.get(member.department);
				if (record != null) {
					record.count++;
					record.addFourmCount(fourm);
					record.updateFirstOccur(position, time);
					return;
				}
			}

			// Find the last line
			StringTokenizer st = new StringTokenizer(content, "\n");
			String lastToken = "";
			while (st.hasMoreTokens())
				lastToken = st.nextToken();

			// A sign longer than MAX_SIGN will not be recorded
			if (lastToken.length() < MAX_SIGN) {
				// Filter some words
				String sign = lastToken.toLowerCase();
				sign = sign.replace("by ", "");
				sign = sign.replace(" ", "");
				sign = sign.replaceAll("[\u0000-\u001f]", "");

				// Count sign
				if (sign.length() > 0) {
					SignCountRecord record = signCounts.get(sign);
					if (record == null) {
						record = new SignCountRecord();
						signCounts.put(sign, record);
					}
					record.count++;
					record.addFourmCount(fourm);
					record.updateFirstOccur(position, time);
				}
			}
		}
	}
}
