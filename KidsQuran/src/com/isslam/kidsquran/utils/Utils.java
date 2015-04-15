package com.isslam.kidsquran.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.isslam.kidsquran.R;
import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.controllers.SharedPreferencesManager;
import com.isslam.kidsquran.model.AudioClass;


public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	public static int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		
		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		// return percentage
		return percentage.intValue();
	}
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static boolean isFileExist(String folderId, String verseName) {
		String localPath = getLocalPath(folderId);
		File file = new File(localPath, verseName);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	public static boolean deleteFile(String folderId, String verseName) {
		String localPath = getLocalPath(folderId);
		File file = new File(localPath, verseName);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static String getLocalPath(String folderId) {
		return Environment.getExternalStorageDirectory() + "/"+GlobalConfig.audioRootPath+"/"
				+ folderId;
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static String getAudioMp3Name(int verseId) {
		String versesId = verseId + "";
		if (versesId.length() == 3)
			versesId = versesId + ".mp3";
		if (versesId.length() == 2)
			versesId = "0" + versesId + ".mp3";
		if (versesId.length() == 1)
			versesId = "00" + versesId + ".mp3";
		return versesId;
	}

	public static boolean deleteVerse(AudioClass audioClass) {
		File currentDir = new File("/sdcard/"+GlobalConfig.audioRootPath+"/"
				+ audioClass.getReciterId() + "/"
				+ getAudioMp3Name(audioClass.getVerseId()));
		if (currentDir.exists()) {
			return currentDir.delete();
		}
		return false;
	}

	public static boolean ifSuraDownloaded(Context context,
			AudioClass _audioClass) {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/"+GlobalConfig.audioRootPath+"/" + _audioClass.getReciterId());
		String versesId = getAudioMp3Name(_audioClass.getVerseId()) + "";

		if (dir.exists()) {
			File from = new File(dir, versesId);

			if (from.exists())
				return true;
		}
		return false;

	}

	public static void shareMp3(Context context, AudioClass audioClass) {
		if (ifSuraDownloaded(context, audioClass)) {
			String sharePath = Environment.getExternalStorageDirectory()
					.getPath()
					+ "/"+GlobalConfig.audioRootPath+"/"
					+ audioClass.getReciterId()
					+ "/"
					+ getAudioMp3Name(audioClass.getVerseId());
			Uri uri = Uri.parse(sharePath);
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("audio/*");
			share.putExtra(Intent.EXTRA_STREAM, uri);
			context.startActivity(Intent.createChooser(share,
					"Share Sound File"));
		} else {
			ShowNotDownloaded(context, audioClass);
		}

	}

	public static void SharePath(Context context, AudioClass audioClass) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = context.getString(R.string.share_body)
				+ "http://server11.mp3quran.net/shatri/001.mp3";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				context.getString(R.string.share_title));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent,
				context.getString(R.string.share_title)));
	}

	private static void ShowNotDownloaded(Context context, AudioClass audioClass) {

		new AlertDialog.Builder(context)
				.setTitle(
						context.getResources().getString(R.string.share_title))
				.setMessage(
						context.getResources().getString(
								R.string.share_condition))
				.setPositiveButton(
						context.getResources().getString(
								R.string.app_exit_confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	public static int getDirectoryfilesCount(File path) {
		int count=0;
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return -1;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					//do nothing
				} else {
					count++;
				}
			}
		}
		Log.e("Files Count -->","Files Count -->"+count);
		return count;
	}

	
	public static void updateLocal(Context context, String lang_local,
			String lang_id) {
		// SharedPreferences sharedPreferences = PreferenceManager
		// .getDefaultSharedPreferences(context);
		// String _local = sharedPreferences.getString("languages_preference",
		// "ar");
		// Log.e("_local", _local);
		GlobalConfig.local = lang_local;
		GlobalConfig.lang_id = lang_id;
		Locale locale = new Locale(lang_local);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;

		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());

		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(context);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager._lang_id, lang_id);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager._local, lang_local);
	}
	public static void unzip(File zipFile, File targetDirectory) throws IOException {
	    ZipInputStream zis = new ZipInputStream(
	            new BufferedInputStream(new FileInputStream(zipFile)));
	    try {
	        ZipEntry ze;
	        int count;
	        byte[] buffer = new byte[8192];
	        while ((ze = zis.getNextEntry()) != null) {
	            File file = new File(targetDirectory, ze.getName());
	            File dir = ze.isDirectory() ? file : file.getParentFile();
	            if (!dir.isDirectory() && !dir.mkdirs())
	                throw new FileNotFoundException("Failed to ensure directory: " +
	                        dir.getAbsolutePath());
	            if (ze.isDirectory())
	                continue;
	            FileOutputStream fout = new FileOutputStream(file);
	            try {
	                while ((count = zis.read(buffer)) != -1)
	                    fout.write(buffer, 0, count);
	            } finally {
	                fout.close();
	            }
	            /* if time should be restored as well
	            long time = ze.getTime();
	            if (time > 0)
	                file.setLastModified(time);
	            */
	        }
	    } finally {
	        zis.close();
	    }
	}

}