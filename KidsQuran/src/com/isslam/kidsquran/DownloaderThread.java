package com.isslam.kidsquran;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.isslam.kidsquran.model.DownloadClass;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;

/**
 * Downloads a file in a thread. Will send messages to the AndroidFileDownloader
 * activity to update the progress bar.
 */
public class DownloaderThread extends Thread {
	// constants
	private static final int DOWNLOAD_BUFFER_SIZE = 4096;

	// instance variables
	private DownloadClass downloadClass;
	private String downloadUrl;
	private String versesId;
	private String init_versesId;
	private DownloadService parentActivity;

	/**
	 * Instantiates a new DownloaderThread object.
	 * 
	 * @param downloadService
	 * @param versesId
	 * 
	 * @param parentActivity
	 *            Reference to AndroidFileDownloader activity.
	 * @param inUrl
	 *            String representing the URL of the file to be downloaded.
	 */
	public DownloaderThread(DownloadClass _downloadClass,
			DownloadService downloadService, String _versesId) {
		versesId = _versesId;

		parentActivity = downloadService;
		downloadClass = _downloadClass;
		init_versesId = downloadClass.getAudioClass().getReciterId() + "";
		downloadUrl = downloadClass.getAudioClass().getAudioPath();// + versesId
		// + ".mp3";
	}

	/**
	 * Connects to the URL of the file, begins the download, and notifies the
	 * AndroidFileDownloader activity of changes in state. Writes the file to
	 * the root of the SD card.
	 */
	@Override
	public void run() {
		URL url;
		URLConnection conn;
		int fileSize, lastSlash;
		String fileName;
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		File outFile;
		FileOutputStream fileStream;
		Message msg;

		try {
			/* created Reciter Folder for the audio */
			File folder = new File(Environment.getExternalStorageDirectory()
					+ "/" + GlobalConfig.audioRootPath);
			boolean success = true;
			if (!folder.exists()) {
				success = folder.mkdir();
			}

			/* check if the sound file is already downloaded */
			boolean isFileExist = false;
			if (success) {
				if (Utils.isFileExist(downloadClass.getAudioClass()
						.getReciterId() + "", GlobalConfig.filetocheck)) {
					msg = Message.obtain(parentActivity.activityHandler,
							DownloadService.MESSAGE_DOWNLOAD_COMPLETE,
							downloadClass.getId(), 0, null);
					parentActivity.activityHandler.sendMessage(msg);
					isFileExist = true;
					this.interrupt();

				}
			}
			if (!isFileExist) {

				url = new URL(downloadUrl);

				conn = url.openConnection();

				conn.setUseCaches(false);
				fileSize = conn.getContentLength();

				lastSlash = url.toString().lastIndexOf('/');
				fileName = "file.bin";
				if (lastSlash >= 0) {
					fileName = url.toString().substring(lastSlash + 1);
				}
				if (fileName.equals("")) {
					fileName = "file.bin";
				}

				// notify download start
				int fileSizeInKB = fileSize / 1024;
				msg = Message.obtain(parentActivity.activityHandler,
						DownloadService.MESSAGE_DOWNLOAD_STARTED,
						downloadClass.getId(), 0, null);
				parentActivity.activityHandler.sendMessage(msg);

				downloadClass.setProgress(0);

				versesId = downloadClass.getAudioClass().getReciterId()
						+ "_temp" + GlobalConfig.zipExtension;
				// start download
				inStream = new BufferedInputStream(conn.getInputStream());
				outFile = new File("/sdcard/" + GlobalConfig.audioRootPath
						+ "/" + versesId);
				fileStream = new FileOutputStream(outFile);
				outStream = new BufferedOutputStream(fileStream,
						DOWNLOAD_BUFFER_SIZE);
				byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
				int bytesRead = 0, totalRead = 0;
				while (!isInterrupted()
						&& (bytesRead = inStream.read(data, 0, data.length)) >= 0) {
					outStream.write(data, 0, bytesRead);
					totalRead += bytesRead;
					int totalReadInKB = totalRead / 1024;
					// Log.e("progress", totalRead +" : "+ fileSize+"");

					downloadClass
							.setProgress((int) (((float) totalRead / (float) fileSize) * 100));
					msg = Message
							.obtain(parentActivity.activityHandler,
									DownloadService.MESSAGE_UPDATE_PROGRESS_BAR,
									(int) (((float) totalRead / (float) fileSize) * 100),
									0, null);
					parentActivity.activityHandler.sendMessage(msg);
				}

				outStream.close();
				fileStream.close();
				inStream.close();

				if (isInterrupted()) {
					msg = Message.obtain(parentActivity.activityHandler,
							DownloadService.MESSAGE_DOWNLOAD_CANCELED,
							downloadClass.getId(), 0, null);
					parentActivity.activityHandler.sendMessage(msg);
					outFile.delete();
				} else {
					downloadClass.setProgress(101);

					File dir = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ GlobalConfig.audioRootPath);

					String versesTo = init_versesId + GlobalConfig.zipExtension;
					versesId = init_versesId + "_temp"
							+ GlobalConfig.zipExtension;
					if (dir.exists()) {
						File from = new File(dir, versesId);
						File to = new File(dir, versesTo);
						if (from.exists()) {
							from.renameTo(to);
							msg = Message.obtain(
									parentActivity.activityHandler,
									DownloadService.MESSAGE_ZIP_EXTRACTED,
									downloadClass.getId(), 0, null);
							parentActivity.activityHandler.sendMessage(msg);
							try {
								Utils.unzip(to, new File("/sdcard/KidsQuran/"
										+ downloadClass.getAudioClass()
												.getReciterId()));
								to.delete();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// Actions to do after 10 seconds
						}
					}
					msg = Message.obtain(parentActivity.activityHandler,
							DownloadService.MESSAGE_DOWNLOAD_COMPLETE,
							downloadClass.getId(), 0, null);
					parentActivity.activityHandler.sendMessage(msg);
				}

			}
		} catch (MalformedURLException e) {
			msg = Message.obtain(parentActivity.activityHandler,
					DownloadService.MESSAGE_ENCOUNTERED_ERROR,
					downloadClass.getId(), 0, null);
			e.printStackTrace();
			Log.e("error", e.getMessage() + "");
			parentActivity.activityHandler.sendMessage(msg);

		} catch (FileNotFoundException e) {
			msg = Message.obtain(parentActivity.activityHandler,
					DownloadService.MESSAGE_ENCOUNTERED_ERROR,
					downloadClass.getId(), 0, null);
			e.printStackTrace();
			Log.e("error", e.getMessage() + "");
			parentActivity.activityHandler.sendMessage(msg);

		} catch (Exception e) {
			msg = Message.obtain(parentActivity.activityHandler,
					DownloadService.MESSAGE_ENCOUNTERED_ERROR,
					downloadClass.getId(), 0, null);
			e.printStackTrace();
			Log.e("error", e.getMessage() + "");
			parentActivity.activityHandler.sendMessage(msg);

		}
	}

}