package com.isslam.kidsquran;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
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
public class ExtractReciterThread extends Thread {

	private SplashScreenActivity parentActivity;
	private final int BUFFER_SIZE = 1024 * 10;
	private final String TAG = "Decompress";

	public void unzipFromAssets(Context context, String zipFile,
			String destination) {
		try {
			if (destination == null || destination.length() == 0)
				destination = context.getFilesDir().getAbsolutePath();
			InputStream stream = getClass().getResourceAsStream(zipFile);// context.getAssets().open(zipFile);
			Log.e("destination", destination);
			unzip(stream, destination);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dirChecker(String destination, String dir) {
		File f = new File(destination + dir);

		if (!f.isDirectory()) {
			boolean success = f.mkdirs();
			if (!success) {
				// Log.w(TAG, "Failed to create folder " + f.getName());
			}
		}
	}

	public void unzip(InputStream stream, String destination) {
		dirChecker(destination, "");
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			ZipInputStream zin = new ZipInputStream(stream);
			ZipEntry ze = null;

			while ((ze = zin.getNextEntry()) != null) {
				// Log.v(TAG, "Unzipping " + ze.getName());

				if (ze.isDirectory()) {
					dirChecker(destination, ze.getName());
				} else {
					File f = new File(destination + ze.getName());
					if (!f.exists()) {
						FileOutputStream fout = new FileOutputStream(
								destination + ze.getName());
						int count;
						while ((count = zin.read(buffer)) != -1) {
							fout.write(buffer, 0, count);
						}
						zin.closeEntry();
						fout.close();
					}
				}

			}
			zin.close();
		} catch (Exception e) {
			// Log.e(TAG, "unzip", e);
		}

	}

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
	Context context = null;
	String zipFile = "";
	String destination = "";

	public ExtractReciterThread(SplashScreenActivity _parentActivity,
			Context _context, String _zipFile, String _destination) {
		context = _context;
		zipFile = _zipFile;
		destination = _destination;
		parentActivity = _parentActivity;

	}

	/**
	 * Connects to the URL of the file, begins the download, and notifies the
	 * AndroidFileDownloader activity of changes in state. Writes the file to
	 * the root of the SD card.
	 */
	@Override
	public void run() {

		try {
			/* created Reciter Folder for the audio */
			File folder = new File(Environment.getExternalStorageDirectory()
					+ "/" + GlobalConfig.audioRootPath);
			boolean success = true;
			boolean isFileExist = true;
			if (!folder.exists()) {
				success = folder.mkdir();
			}
			Message msg;
			msg = Message.obtain(parentActivity.activityHandler, 100, 0, 0,
					null);
			parentActivity.activityHandler.sendMessage(msg);
			unzipFromAssets(context, zipFile, destination);
			msg = Message.obtain(parentActivity.activityHandler, 200, 0, 0,
					null);
			parentActivity.activityHandler.sendMessage(msg);

		} catch (Exception e) {

		}
	}

}