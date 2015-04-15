package com.isslam.kidsquran;

import java.util.ArrayList;

import com.isslam.kidsquran.model.AudioClass;
import com.isslam.kidsquran.model.DownloadClass;
import com.isslam.kidsquran.model.Suras;
import com.isslam.kidsquran.utils.GlobalConfig;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class DownloadService extends Service {
	public static enum DownloadStatus {
		Empty, STARTED, INPROGRESS, FINISH, ERROR, CANCEL
	}

	private MediaPlayer mp = null;
	private DownloadStatus downloadStatus = DownloadStatus.Empty;
	private final IBinder dBinder = new DownloadBinder();
	private DownloadInterfaceListener mCallback;
	private static int classID = 9981; // just a number
	private final ArrayList<DownloadInterfaceListener> mListeners = new ArrayList<DownloadInterfaceListener>();
	private final ArrayList<DownloadClass> downloads = new ArrayList<DownloadClass>();
	private int progress = 0;

	public int getProgress() {
		return progress;
	}

	public DownloadStatus getDownloadStatus() {
		return downloadStatus;
	}

	public DownloadService() {

	}

	@Override
	public void onDestroy() {
		Log.e("Download Destroy", "Download Destroy");
		stopForeground(true);

	}

	public interface DownloadInterfaceListener {
		public void onDownloadPreExecute(DownloadClass downloadClass);

		public void onDownloadPostExecute(DownloadClass downloadClass);

		public void onDownloadProgressUpdate(DownloadClass downloadClass,
				Integer progress);

		public void onDownloadCancel();

		public void onDownloadError(DownloadClass downloadClass);

		public void onZipExtracting(DownloadClass downloadClass);

	}

	public void registerListener(DownloadInterfaceListener listener) {
		mListeners.add(listener);
	}

	public void unregisterListener(DownloadInterfaceListener listener) {
		mListeners.remove(listener);
	}

	public class DownloadBinder extends Binder {
		DownloadService getService() {
			return DownloadService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return dBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e("start", "download service");
		return Service.START_STICKY;
	}

	public ArrayList<DownloadClass> getDownloads() {
		return downloads;
	}

	int idCounter = 0;

	public Boolean CheckSuraIsDownloading(AudioClass audioClass) {
		for (int i = 0; i < downloads.size(); i++) {
			AudioClass _audioClass = downloads.get(i).getAudioClass();
			if (audioClass.getReciterId() == _audioClass.getReciterId()
					&& audioClass.getVerseId() == _audioClass.getVerseId()) {
				return true;
			}
		}
		return false;
	}

	int count = 1;
	int currentIndex = 0;
	int ayahCount = 0;
	int ayahIndexcount = 1;
	int surasCount = 37;

	public void CancelDownloads() {
		if (downloaderThread != null)
			downloaderThread.interrupt();
		downloadStatus = DownloadStatus.CANCEL;
		currentIndex = 500;
		count = 1;
		ayahIndexcount = 1;
		ayahCount = 0;

	}

	AudioClass audioClass;

	public void DownloadSuras(AudioClass _audioClass) {
		audioClass = _audioClass;
		downloadStatus = DownloadStatus.STARTED;
		count = 1;
		currentIndex = 0;
		ayahCount = 0;
		ayahIndexcount = 1;
		DownloadSurasNow(audioClass);
	}

	public void DownloadSurasNow(AudioClass audioClass) {

		ayahCount = audioClass.getAyahCount();

		String id = 1 + "";
		if (id.length() == 2)
			id = "0" + id;
		if (id.length() == 1)
			id = "00" + id;
		String versesId = audioClass.getVerseId() + "";
		if (versesId.length() == 2)
			versesId = "0" + versesId;
		if (versesId.length() == 1)
			versesId = "00" + versesId;
		versesId = versesId + id;

		DownloadClass downloadClass = new DownloadClass();
		downloadClass.setAudioClass(audioClass);
		downloadClass.setId(idCounter + classID);
		downloadClass.setProgress(-1);
		downloads.add(downloadClass);
		downloadStatus = DownloadStatus.INPROGRESS;
		// BuildNotificationId(idCounter + classID, downloadClass,0);
		Log.e("Download-->", downloadClass.getAudioClass().getAudioPath());
		downloaderThread = new DownloaderThread(downloadClass,
				DownloadService.this, versesId);
		downloaderThread.start();

	}

	DownloaderThread downloaderThread;
	public Handler activityHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * Handling MESSAGE_UPDATE_PROGRESS_BAR: 1. Get the current
			 * progress, as indicated in the arg1 field of the Message. 2.
			 * Update the progress bar.
			 */
			case MESSAGE_UPDATE_PROGRESS_BAR:
				for (int i = mListeners.size() - 1; i >= 0; i--) {
					mListeners.get(i).onDownloadProgressUpdate(
							downloads.get(0), 0);
				}

				break;

			/*
			 * Handling MESSAGE_CONNECTING_STARTED: 1. Get the URL of the file
			 * being downloaded. This is stored in the obj field of the Message.
			 * 2. Create an indeterminate progress bar. 3. Set the message that
			 * should be sent if user cancels. 4. Show the progress bar.
			 */
			case MESSAGE_CONNECTING_STARTED:

				break;

			/*
			 * Handling MESSAGE_DOWNLOAD_STARTED: 1. Create a progress bar with
			 * specified max value and current value 0; assign it to
			 * progressDialog. The arg1 field will contain the max value. 2. Set
			 * the title and text for the progress bar. The obj field of the
			 * Message will contain a String that represents the name of the
			 * file being downloaded. 3. Set the message that should be sent if
			 * dialog is canceled. 4. Make the progress bar visible.
			 */
			case MESSAGE_DOWNLOAD_STARTED:
				// Log.e("started", "started");
				// Log.e("args", msg.arg1 + "");
				for (int i = mListeners.size() - 1; i >= 0; i--) {
					mListeners.get(i).onDownloadPreExecute(null);
				}
				// obj will contain a String representing the file name

				break;
			case MESSAGE_ZIP_EXTRACTED:
				// Log.e("started", "started");
				// Log.e("args", msg.arg1 + "");
				for (int i = mListeners.size() - 1; i >= 0; i--) {
					mListeners.get(i).onZipExtracting(downloads.get(0));
				}
				// obj will contain a String representing the file name

				break;

			/*
			 * Handling MESSAGE_DOWNLOAD_COMPLETE: 1. Remove the progress bar
			 * from the screen. 2. Display Toast that says download is complete.
			 */
			case MESSAGE_DOWNLOAD_COMPLETE:
				Log.e("---->", count + " : " + ayahCount + " : " + currentIndex);

				downloadStatus = DownloadStatus.FINISH;

				for (int i = mListeners.size() - 1; i >= 0; i--) {
					mListeners.get(i).onDownloadPostExecute(downloads.get(0));
				}
				for (int j = 0; j < downloads.size(); j++) {

					downloads.remove(j);

				}

				if (downloads.isEmpty()) {
					Intent _intent = new Intent(getApplicationContext(),
							DownloadService.class);
					stopService(_intent);
				}

				break;

			/*
			 * Handling MESSAGE_DOWNLOAD_CANCELLED: 1. Interrupt the downloader
			 * thread. 2. Remove the progress bar from the screen. 3. Display
			 * Toast that says download is complete.
			 */
			case MESSAGE_DOWNLOAD_CANCELED:
				for (int j = 0; j < downloads.size(); j++) {

					downloads.remove(j);

				}
				for (int i = mListeners.size() - 1; i >= 0; i--) {
					mListeners.get(i).onDownloadCancel();
				}
				Intent _intent = new Intent(getApplicationContext(),
						DownloadService.class);
				stopService(_intent);
				break;

			/*
			 * Handling MESSAGE_ENCOUNTERED_ERROR: 1. Check the obj field of the
			 * message for the actual error message that will be displayed to
			 * the user. 2. Remove any progress bars from the screen. 3. Display
			 * a Toast with the error message.
			 */
			case MESSAGE_ENCOUNTERED_ERROR:
				for (int j = 0; j < downloads.size(); j++) {
					if (downloads.get(j).getId() == msg.arg1) {
						for (int i = mListeners.size() - 1; i >= 0; i--) {
							mListeners.get(i).onDownloadError(downloads.get(j));
						}
						downloads.get(j).setProgress(1000);
						// downloads.remove(j);
						break;
					}
				}
				break;

			default:
				// nothing to do here
				break;
			}
		}
	};
	public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
	public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
	public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
	public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
	public static final int MESSAGE_CONNECTING_STARTED = 1004;
	public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
	public static final int MESSAGE_ZIP_EXTRACTED = 1006;

	public void BuildNotificationId(int _id, DownloadClass downloadClass,
			int _progress) {

		final int id = _id;

		final DownloadClass _audioClass = downloadClass;
		final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		final Builder mBuilder = new NotificationCompat.Builder(this);
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		mBuilder.setContentIntent(pi);
		mBuilder.setAutoCancel(true);
		String image = downloadClass.getAudioClass().getImage().split(".jpg")[0];
		image = "reciter_1";
		int reciterIconId = this.getResources().getIdentifier(image,
				"drawable", this.getPackageName());

		mBuilder.setContentTitle(
				getResources().getString(R.string.reciters_pre) + " "
						+ downloadClass.getAudioClass().getReciterName())
				.setContentText(
						getResources().getString(R.string.download_progress)
								+ " "
								+ downloadClass.getAudioClass().getVerseName()
								+ " - " + progress + " %")
				.setSmallIcon(reciterIconId);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.e("count:->", _audioClass.getProgress() + "");
					progress = _audioClass.getProgress();

					while (progress < 100) {
						progress = _audioClass.getProgress();
						// Log.e("t:->",t+"");
						mBuilder.setProgress(100, progress, false);
						mBuilder.setContentText(getResources().getString(
								R.string.download_progress)
								+ " - " + progress + " %");
						mNotifyManager.notify(id, mBuilder.build());
						// Sleep for 5 seconds
						Thread.sleep(300);
					}
					if (progress < 200) {
						mBuilder.setProgress(100, 100, false);
						mBuilder.setContentText(getResources().getString(
								R.string.download_complete_pre)
								+ getResources().getString(
										R.string.download_complete_after) + " ");
						mNotifyManager.notify(id, mBuilder.build());
					}

					for (int j = 0; j < downloads.size(); j++) {
						if (downloads.get(j).getId() == id) {

							downloads.remove(j);
							break;
						}
					}
					Log.e("downloads Size", downloads.size() + "");
					if (downloads.isEmpty()) {
						Log.e("downloads Size", "empty");
						Intent _intent = new Intent(getApplicationContext(),
								DownloadService.class);
						stopService(_intent);
					}
					Log.e("downloads Size", "cancel: " + id);
					mNotifyManager.cancel(id);

				} catch (InterruptedException e) {
					e.printStackTrace();

				}

			}
		}).start();

		startForeground(id, mBuilder.build());

	}

}
