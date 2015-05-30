package slmt.crawler.dcard.converter;

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

import slmt.crawler.dcard.converter.csv.CSVComment;
import slmt.crawler.dcard.converter.csv.CSVPost;
import slmt.crawler.dcard.converter.json.Comment;
import slmt.crawler.dcard.converter.json.Post;
import slmt.crawler.dcard.converter.json.Version;

import com.alibaba.fastjson.JSON;

public class CSVConverter {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	private static String inputDirName;
	private static String outputDirName;

	public static void main(String[] args) {

		// Check the number of arguments
		if (args.length < 2) {
			System.out.println("Arguments: [Input Dir] [Output Dir]");
			return;
		}

		// Get arguments
		inputDirName = args[0];
		outputDirName = args[1];

		try {
			// Timer
			long totalStart = System.nanoTime();

			// Read the list of input files
			File inputDir = new File(inputDirName);
			String[] inputFiles = inputDir.list();

			// Create output files
			BufferedWriter postOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(outputDirName, "posts.csv")),
					"UTF-8"));
			BufferedWriter comOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(
							new File(outputDirName, "comments.csv")), "UTF-8"));

			// Convert files
			for (String fileName : inputFiles) {
				String idPart = fileName.substring(0, 5);
				String typePart = fileName.substring(6);

				if (typePart.equals("json")) {
					long start = System.nanoTime();
					String json = loadFile(new File(inputDirName, fileName));
					int postId = Integer.parseInt(idPart);

					Post post = JSON.parseObject(json, Post.class);
					CSVPost csvPost = convertToCSV(postId, post);
					saveToFile(csvPost, postOut, comOut);

					long time = System.nanoTime() - start;
					System.out.println("Process post." + postId + " took "
							+ time / 1000000 + "ms.");
				}
			}

			// Close files
			postOut.close();
			comOut.close();

			// Timer end
			long totalTime = System.nanoTime() - totalStart;
			System.out.println("Whole program took " + totalTime / 1000000
					+ "ms.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveToFile(CSVPost post, BufferedWriter postOut,
			BufferedWriter comOut) throws IOException {
		// Write post
		postOut.append(post.toString());
		postOut.newLine();

		// Write comments
		for (CSVComment com : post.comments) {
			comOut.append(com.toString());
			comOut.newLine();
		}
	}

	private static CSVPost convertToCSV(int postId, Post jsonPost) {
		CSVPost post = new CSVPost();

		try {
			// Convert post
			post.pid = postId;
			post.fourm = jsonPost.forum_alias;
			post.pinned = jsonPost.pinned;
			post.reply = jsonPost.reply;
			post.createdAt = getDateInLong(jsonPost.createdAt);
			post.updatedAt = getDateInLong(jsonPost.updatedAt);
			post.likeCount = jsonPost.likeCount;

			post.posterGender = jsonPost.member.gender;
			post.posterSchool = jsonPost.member.school;
			post.posterDepart = jsonPost.member.department;
			post.anonymousSchool = jsonPost.anonymousSchool;
			post.anonymousDepartment = jsonPost.anonymousDepartment;

			Version v = jsonPost.version.get(0);
			post.title = v.title.replace("\"", "\"\""); // for CSV format
			post.content = v.content.replace("\"", "\"\""); // for CSV format

			// Convert comments
			int cid = 1;
			for (Comment jsonComment : jsonPost.comment) {
				CSVComment comment = new CSVComment();

				comment.cid = cid++;
				comment.postId = postId;
				comment.like = jsonComment.like;
				comment.hidden = jsonComment.hidden;
				comment.posterGender = jsonComment.member.gender;
				comment.posterSchool = jsonComment.member.school;
				comment.posterDepart = jsonComment.member.department;
				comment.anonymous = jsonComment.anonymous;
				comment.host = jsonComment.host;

				v = jsonComment.version.get(0);
				comment.content = v.content.replace("\"", "\"\""); // for CSV
																	// format
				comment.postTime = getDateInLong(v.createdAt);

				post.comments.add(comment);
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("Post " + postId + " parse failed.");
		}

		return post;
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

	private static long getDateInLong(String dateString) {
		try {
			return DATE_FORMAT.parse(dateString).getTime();
		} catch (ParseException e) {
			try {
				return Long.parseLong(dateString);
			} catch (NumberFormatException ex) {
				throw new RuntimeException("Parse date: " + dateString
						+ " failed.");
			}
		}
	}
}
