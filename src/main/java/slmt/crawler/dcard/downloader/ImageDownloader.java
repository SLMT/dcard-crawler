package slmt.crawler.dcard.downloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import slmt.crawler.dcard.analyzer.PostParser;
import slmt.crawler.dcard.util.HttpUtils;
import slmt.crawler.dcard.util.IOUtils;

public class ImageDownloader {
	Logger logger = Logger.getLogger(ImageDownloader.class.getName());
	
	private File srcDir, saveDir;
	
	public ImageDownloader(String sourcePath, String savePath) {
		srcDir = new File(sourcePath);
		if (!srcDir.exists()) {
			if (logger.isLoggable(Level.SEVERE))
				logger.severe("source dir: " + srcDir + " doesn't exist");
			throw new IllegalArgumentException("source dir: " + srcDir + " doesn't exist");
		}
		
		saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
			
			if (logger.isLoggable(Level.INFO))
				logger.info("creates a new directory at " + saveDir);
		}
	}
	
	public void downloadImages() {
		try {
			// For each file in the source directory
			String[] fileNames = srcDir.list();
			for (String fileName : fileNames) {
				// Only process txt file
				if (!fileName.contains("txt"))
					continue;
				
				if (logger.isLoggable(Level.INFO))
					logger.info("downloading images in " + fileName);
				
				// Retrieves URLs
				File file = new File(srcDir, fileName);
				String content = IOUtils.loadAsString(file);
				String imageFilePrefix = fileName.replace(".txt", "").concat("_");
				List<String> imageUrls = PostParser.getImageUrls(content);
				
				// Downloads images
				int count = 0;
				for (String url : imageUrls) {
					InputStream in = HttpUtils.constructInputStream(url);
					IOUtils.saveToAFile(saveDir, imageFilePrefix + count + ".jpg", in);
					count++;
				}
				
				if (logger.isLoggable(Level.INFO))
					logger.info("finishes downloading for " + fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
