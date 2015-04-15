package com.isslam.kidsquran;

import java.io.File;
import java.io.IOException;

import com.davemorrissey.labs.subscaleview.ScaleImageView;
import com.isslam.kidsquran.RecordingMediaPlayer.PlayerInterfaceListener;
import com.isslam.kidsquran.R.id;
import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordingFragment extends DialogFragment implements
		PlayerInterfaceListener {

	private RecMicToMp3 mRecMicToMp3 = new RecMicToMp3(
			Environment.getExternalStorageDirectory() + "/abed.mp3", 8000);
	private static final String BUNDLE_ASSET = "asset";
	private String asset = "images/78/s_78_38.png";
	private Handler mHandler = new Handler();
	private Handler recordingHandler = new Handler();

	@Override
	public void onStart() {

		super.onStart();

		// safety check
		if (getDialog() == null) {
			return;
		}

		setStyle(STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		// ... other stuff you want to do in your onStart() method
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		Log.e("Dialog", "cancel");
		myMediaPlayer.unregisterListener(this);

		mHandler.removeCallbacks(mUpdateTimeTask);
		myMediaPlayer.stop();

		if (animHandler != null)
			animHandler.removeCallbacks(animRunnable);
		HideRecord();

		((ViewPagerActivity) getActivity()).updateRecordButton();

	}

	ImageView recording_icon;
	ImageView btn_recording_speaker;
	String mp3Path = "";
	AnimationDrawable frameAnimation;

	private void PlayRecordingAnimation() {
		recording_icon
				.setBackgroundResource(R.drawable.recorder_icon_animations);
		frameAnimation = (AnimationDrawable) recording_icon.getBackground();
		recording_icon.post(new Runnable() {
			@Override
			public void run() {
				frameAnimation.start();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.e("dialog", "create");
		View v = inflater.inflate(R.layout.ly_recordings, container, false);
		setStyle(STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		getDialog().setCancelable(false);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		// View tv = v.findViewById(R.id.text);
		// ((TextView)tv).setText("This is an instance of MyDialogFragment");

		ImageView btn_toggle_play = (ImageView) v
				.findViewById(R.id.btn_toggle_play);
		btn_toggle_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// mRecMicToMp3.start();

				String fileName = AudioListManager.ayahId + "";
				if (fileName.length() == 2)
					fileName = "0" + fileName;
				if (fileName.length() == 1)
					fileName = "00" + fileName;

				String suraId = AudioListManager.suraId + "";
				if (suraId.length() == 2)
					suraId = "0" + suraId;
				if (suraId.length() == 1)
					suraId = "00" + suraId;
				AudioListManager audioListManager = AudioListManager
						.getInstance();

				mp3Path = "sounds/"
						+ audioListManager.getSelectedReciter().getId() + "/"
						+ suraId + fileName + ".mp3";

				String path = suraId + fileName + ".mp3";
				if (Utils.isFileExist("recordings/" + AudioListManager.suraId,
						path)) {
					String recordingPath = Utils.getLocalPath("recordings")
							+ "/" + AudioListManager.suraId + "/" + suraId
							+ fileName + ".mp3";
					myMediaPlayer.PlayNewMp3(recordingPath, 1);
				}

			}
		});

		ImageView btn_record = (ImageView) v.findViewById(R.id.btn_record);
		btn_record.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				ly_recording_third.setVisibility(View.GONE);
				ly_recording_second.setVisibility(View.VISIBLE);
				startRecord();

			}
		});

		ImageView btn_recording_finish = (ImageView) v
				.findViewById(R.id.btn_recording_finish);
		btn_recording_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mHandler.removeCallbacks(mUpdateTimeTask);
				myMediaPlayer.stop();
				mRecMicToMp3.stop();
				getDialog().cancel();

			}
		});

		ImageView btn_accept = (ImageView) v
				.findViewById(R.id.btn_start_recording);

		btn_accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ly_recording_second.setVisibility(View.VISIBLE);
				ly_recording_first.setVisibility(View.GONE);
				// mRecMicToMp3.start();

				String fileName = AudioListManager.ayahId + "";
				if (fileName.length() == 2)
					fileName = "0" + fileName;
				if (fileName.length() == 1)
					fileName = "00" + fileName;

				String suraId = AudioListManager.suraId + "";
				if (suraId.length() == 2)
					suraId = "0" + suraId;
				if (suraId.length() == 1)
					suraId = "00" + suraId;
				AudioListManager audioListManager = AudioListManager
						.getInstance();

				// mp3Path="sounds/"+audioListManager.getSelectedReciter().getId()+"/"+suraId+fileName+".mp3";
				btn_recording_speaker.setVisibility(View.VISIBLE);
				// myMediaPlayer.PlayNewMp3(mp3Path,0);

				mp3Path = Utils.getLocalPath(audioListManager
						.getSelectedReciter().getId() + "")
						+ "/" + suraId + fileName + ".mp3";
				Log.e("path", mp3Path);
				myMediaPlayer.PlayNewMp3(mp3Path, 1);
				mHandler.postDelayed(mUpdateTimeTask, 100);
			}
		});
		ImageView btn_recording_close = (ImageView) v
				.findViewById(R.id.btn_recording_close);

		btn_recording_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mHandler.removeCallbacks(mUpdateTimeTask);
				myMediaPlayer.stop();

				if (animHandler != null)
					animHandler.removeCallbacks(animRunnable);
				HideRecord();
				getDialog().cancel();

			}
		});

		mRecMicToMp3.setHandle(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case RecMicToMp3.MSG_REC_STARTED:

					break;
				case RecMicToMp3.MSG_REC_STOPPED:

					break;
				case RecMicToMp3.MSG_ERROR_GET_MIN_BUFFERSIZE:

					break;
				case RecMicToMp3.MSG_ERROR_CREATE_FILE:

					break;
				case RecMicToMp3.MSG_ERROR_REC_START:

					break;
				case RecMicToMp3.MSG_ERROR_AUDIO_RECORD:

					break;
				case RecMicToMp3.MSG_ERROR_AUDIO_ENCODE:

					break;
				case RecMicToMp3.MSG_ERROR_WRITE_FILE:

					break;
				case RecMicToMp3.MSG_ERROR_CLOSE_FILE:

					break;
				default:
					break;
				}
			}
		});

		asset = "images/" + AudioListManager.suraId + "/s_"
				+ AudioListManager.suraId + "_" + AudioListManager.ayahId
				+ ".png";
		if (AudioListManager.ayahId == 0)
			asset = "images/besm_allah.png";
		img_ayah = (ScaleImageView) v.findViewById(id.img_ayah);
		img_ayah.setImageAsset(asset);
		img_ayah.setVisibility(View.VISIBLE);

		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.progress_green);
		mProgress = (ProgressBar) v.findViewById(R.id.progressbar1);
		mProgress.setProgress(0); // Main Progress
		mProgress.setSecondaryProgress(0); // Secondary Progress
		mProgress.setMax(100); // Maximum Progress
		mProgress.setProgressDrawable(drawable);

		myMediaPlayer = RecordingMediaPlayer.getInstance(v.getContext());
		myMediaPlayer.registerListener(this);
		// myMediaPlayer.CreateMediaPlayer();
		recording_icon = (ImageView) v.findViewById(id.recording_icon);
		recording_icon.setVisibility(View.GONE);
		btn_recording_speaker = (ImageView) v
				.findViewById(id.btn_recording_speaker);
		btn_recording_speaker.setVisibility(View.GONE);

		ly_recording_first = (RelativeLayout) v
				.findViewById(id.ly_recording_first);
		ly_recording_second = (RelativeLayout) v
				.findViewById(id.ly_recording_second);
		ly_recording_second.setVisibility(View.GONE);
		ly_recording_first.setVisibility(View.VISIBLE);

		ly_recording_third = (RelativeLayout) v
				.findViewById(id.ly_recording_third);
		ly_recording_third.setVisibility(View.GONE);

		img_num_animation = (ImageView) v.findViewById(id.img_num_animation);

		File folder = new File(Environment.getExternalStorageDirectory() + "/"
				+ GlobalConfig.audioRootPath);
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		if (success) {
			folder = new File(Environment.getExternalStorageDirectory() + "/"
					+ GlobalConfig.audioRootPath + "/" + "recordings");
			if (!folder.exists()) {
				success = folder.mkdir();
			}

			if (success) {
				folder = new File(Environment.getExternalStorageDirectory()
						+ "/" + GlobalConfig.audioRootPath + "/recordings/"
						+ AudioListManager.suraId);
				if (!folder.exists()) {
					success = folder.mkdir();
				}
			}
		}
		// PlayRecordingAnimation();
		return v;
	}

	ScaleImageView img_ayah;
	ProgressBar mProgress;
	RelativeLayout ly_recording_first;
	RelativeLayout ly_recording_second;
	RelativeLayout ly_recording_third;
	RecordingMediaPlayer myMediaPlayer;

	@Override
	public void onCompletionMp3() {
		// TODO Auto-generated method stub

		startRecord();

	}

	ImageView img_num_animation;

	@SuppressLint("NewApi")
	private void startRecord() {
		img_num_animation.setVisibility(View.VISIBLE);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.progress_red);
		btn_recording_speaker.setVisibility(View.GONE);
		img_ayah.setVisibility(View.GONE);
		ly_recording_second.setVisibility(View.GONE);
		mProgress.setProgress(0);
		mProgress.setProgressDrawable(drawable);
		mHandler.removeCallbacks(mUpdateTimeTask);
		myMediaPlayer.stop();
		img_num_animation.setBackgroundResource(R.drawable.numbers_animations);
		frameAnimation = (AnimationDrawable) img_num_animation.getBackground();
		img_num_animation.post(new Runnable() {
			@Override
			public void run() {
				frameAnimation.start();
			}
		});
		animHandler = new Handler();
		animRunnable = new Runnable() {
			public void run() {
				img_num_animation.setVisibility(View.GONE);
				startRecord1();
			}
		};

		animHandler.postDelayed(animRunnable, 1600);

	}

	Handler animHandler = null;
	Runnable animRunnable = null;

	@SuppressLint("NewApi")
	private void startRecord1() {

		Log.e("record", "start rec");
		ly_recording_second.setVisibility(View.VISIBLE);
		AssetFileDescriptor afd;
		if (!isAdded()) {
			return;
		}
		img_ayah.setVisibility(View.VISIBLE);
		// afd = getResources().getAssets().openFd(mp3Path);
		// MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		// metaRetriever.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

		String out = "";
		// get mp3 info

		// convert duration to minute:seconds
		// String duration =
		// metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		metaRetriever.setDataSource(mp3Path);
		String duration = metaRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		Log.v("time", duration);
		recording_icon.setVisibility(View.VISIBLE);

		PlayRecordingAnimation();

		String fileName = AudioListManager.ayahId + "";
		if (fileName.length() == 2)
			fileName = "0" + fileName;
		if (fileName.length() == 1)
			fileName = "00" + fileName;

		String suraId = AudioListManager.suraId + "";
		if (suraId.length() == 2)
			suraId = "0" + suraId;
		if (suraId.length() == 1)
			suraId = "00" + suraId;

		String filePath = Environment.getExternalStorageDirectory() + "/"
				+ GlobalConfig.audioRootPath + "/recordings/"
				+ AudioListManager.suraId + "/" + suraId + fileName + ".mp3";
		mRecMicToMp3.setAudioPath(filePath);
		mRecMicToMp3.start();
		step = 100;
		recording_animation = Integer.parseInt(duration) + 1000;
		recordingHandler.removeCallbacks(mRecordingTask);
		recordingHandler.postDelayed(mRecordingTask, 100);

		Log.e("init", "complete");

	}

	int recording_animation = 1000;

	private void HideRecord() {
		if (recordingHandler != null)
			recordingHandler.removeCallbacks(mRecordingTask);

		if (mRecMicToMp3 != null)
			mRecMicToMp3.stop();
		ly_recording_second.setVisibility(View.GONE);
		recording_icon.setVisibility(View.GONE);
		ly_recording_third.setVisibility(View.VISIBLE);
		myMediaPlayer.unregisterListener(this);
	}

	@Override
	public void onErrorMp3() {
		// TODO Auto-generated method stub

	}

	@Override
	public void isPreparedMp3() {
		// TODO Auto-generated method stub
		Log.e("init", "prepeared");

	}

	@Override
	public void TogglePlay(Boolean status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onstartMp3() {
		// TODO Auto-generated method stub
		Log.e("init", "start");

	}

	@Override
	public void onInitNewMp3() {
		// TODO Auto-generated method stub
		Log.e("init", "init");

	}

	@Override
	public void onToggleRepeat(Boolean bRepeat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onToggleShuffle(Boolean bShuffle) {
		// TODO Auto-generated method stub

	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			try {

				if (myMediaPlayer.getIsprepared()) {
					long totalDuration = 0;
					long currentDuration = 0;

					totalDuration = myMediaPlayer.getDuration();
					currentDuration = myMediaPlayer.getCurrentPosition();

					// Updating progress bar
					int progress = (int) (Utils.getProgressPercentage(
							currentDuration, totalDuration));
					// Log.d("Progress", ""+progress);
					mProgress.setProgress(progress);
				}
				// Running this thread after 100 milliseconds
				mHandler.postDelayed(this, 100);

			} catch (Exception e) {

			}
		}
	};
	int step = 100;
	private Runnable mRecordingTask = new Runnable() {
		public void run() {
			try {
				Log.e("start", "record" + step);
				if (step < recording_animation) {
					step += 100;
					int progress = (int) (Utils.getProgressPercentage(step,
							recording_animation));
					// Log.d("Progress", ""+progress);
					progress = 100 - progress;
					mProgress.setProgress(progress);
					recordingHandler.postDelayed(this, 100);
				} else {
					AudioListManager audioListManager = AudioListManager
							.getInstance();

					if (Utils.getDirectoryfilesCount(new File(Environment
							.getExternalStorageDirectory()
							+ "/"
							+ GlobalConfig.audioRootPath
							+ "/recordings/"
							+ AudioListManager.suraId)) == audioListManager
							.getSelectedSura().getAyahCount()) {

						if (GlobalConfig.GetmyDbHelper().get_record(
								AudioListManager.suraId) == 0) {
							audioListManager.UpdateRecords(
									AudioListManager.suraId, 1);
							showRecordingFinishDialog();
						}

					}
					// Log.e("-->",audioListManager.getSelectedSura().getAyahCount()+" : "+Utils.getDirectoryfilesCount(new
					// File(Environment.getExternalStorageDirectory()+"/"+GlobalConfig.audioRootPath+"/recordings/"+AudioListManager.suraId)));

					HideRecord();

				}

			} catch (Exception e) {

			}
		}
	};
	RecordFinishFragment recordFinishFragment = null;

	private void showRecordingFinishDialog() {
		if (recordFinishFragment == null)
			recordFinishFragment = new RecordFinishFragment();
		recordFinishFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);

		recordFinishFragment.show(getActivity().getSupportFragmentManager(),
				"recordFinishFragment");

	}

}
