package slmt.crawler.dcard.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleDownloader {

	private static final String URL_PREFIX = "http://www.dcard.tw/api/post/all/";
	private static final int IO_BUFFER_SIZE = 4096;

	private static File dirLocation;
	private static int startIndex, endIndex;

	public static void main(String[] args) {

		// Check the number of arguments
		if (args.length < 3) {
			System.out.println("Arguments: [Save Dir] [Start Id] [End Id]");
			return;
		}

		// Get arguments
		try {
			dirLocation = new File(args[0]);
			startIndex = Integer.parseInt(args[1]);
			endIndex = Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe) {
			System.out.println("Arguments must be integers.");
			return;
		}

		// Check download directory
		if (!dirLocation.exists())
			dirLocation.mkdir();

		System.out.println("Start time: " + getCurrentDate());

		// Download documents
		try {
			for (int i = startIndex; i <= endIndex; i++) {
				long startTime = System.nanoTime();
				
				InputStream in = getInputStream(URL_PREFIX + i);

				if (in == null)
					System.out.println("Post no." + i + " is not found.");
				else {
					saveToAFile(String.format("%05d", i) + ".json", in);

					long time = (System.nanoTime() - startTime) / 1000000;
					System.out.println("Post no." + i + " is downloaded in "
							+ time + " ms.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Stop time: " + getCurrentDate());
	}

	private static InputStream getInputStream(String url) {
		try {
			// Set request
			HttpURLConnection con = (HttpURLConnection) new URL(url)
					.openConnection();
			con.setRequestMethod("GET");

			// Check response status
			int responseCode = con.getResponseCode();
			if (responseCode != 200)
				return null;

			return con.getInputStream();
		} catch (IOException e) {
			System.out.println("Exception when " + url + ": " + e.getMessage());
			return null;
		}
	}

	private static void saveToAFile(String fileName, InputStream in)
			throws IOException {
		FileOutputStream out = new FileOutputStream(new File(dirLocation,
				fileName));

		byte[] buffer = new byte[IO_BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1)
			out.write(buffer, 0, bytesRead);

		in.close();
		out.close();
	}

	private static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
