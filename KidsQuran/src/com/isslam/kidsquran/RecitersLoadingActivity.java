package com.isslam.kidsquran;

import java.io.File;
import java.util.ArrayList;

import com.isslam.kidsquran.DownloadService.DownloadBinder;
import com.isslam.kidsquran.DownloadService.DownloadInterfaceListener;
import com.isslam.kidsquran.DownloadService.DownloadStatus;
import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.model.AudioClass;
import com.isslam.kidsquran.model.DownloadClass;
import com.isslam.kidsquran.model.Reciters;
import com.isslam.kidsquran.model.Suras;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class RecitersLoadingActivity extends FragmentActivity implements
		DownloadInterfaceListener {

	Context context;
	public static int pos = 0;
	// connect to service
	DownloadService dService;
	boolean dBound = false;
	private ServiceConnection dConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			DownloadBinder binder = (DownloadBinder) service;
			dService = binder.getService();
			dBound = true;
			dService.registerListener(RecitersLoadingActivity.this);

			img_reciter.getLayoutParams().height = (int) getResources()
					.getDimension(R.dimen.reciter_img_height);
			img_reciter.getLayoutParams().width = (int) getResources()
					.getDimension(R.dimen.reciter_img_width);
			if (mProgress != null) {
				ToggleSelectedReciters();

			}

			// downloadReciter();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.e("server", "Download disconnect");
			dBound = false;
		}
	};

	private void ToggleSelectedReciters() {
		if (Utils.isFileExist(reciterId + "", GlobalConfig.filetocheck)) {
			mProgress.setVisibility(View.INVISIBLE);
			ly_cancel_load.setVisibility(View.INVISIBLE);
			ly_load.setVisibility(View.INVISIBLE);
			ly_select.setVisibility(View.VISIBLE);
			if (reciterId == AudioListManager.reciterId) {
				fl_selected.setVisibility(View.VISIBLE);
				ly_select.setVisibility(View.INVISIBLE);
				// img_reciter.l
				img_reciter.getLayoutParams().height = (int) getResources()
						.getDimension(R.dimen.reciter_img_height_large);
				img_reciter.getLayoutParams().width = (int) getResources()
						.getDimension(R.dimen.reciter_img_width_large);

			}
		} else {
			if (dService.getDownloadStatus() == DownloadStatus.INPROGRESS) {
				mProgress.setVisibility(View.VISIBLE);
				ly_pb_container.setVisibility(View.VISIBLE);
				ly_cancel_load.setVisibility(View.VISIBLE);
				ly_load.setVisibility(View.INVISIBLE);
				ly_select.setVisibility(View.INVISIBLE);
				fl_selected.setVisibility(View.GONE);
			}
		}
	}

	int selectedReciterPosition = 0;
	int reciterId = 1;
	ImageView img_reciter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_reciters_loading);
		context = this;
		Intent i = getIntent();
		selectedReciterPosition = i.getIntExtra("selectedReciter", 0);
		pos = selectedReciterPosition;
		img_reciter = (ImageView) findViewById(R.id.img_reciter);
		ImageView img_reciter_name = (ImageView) findViewById(R.id.img_reciter_name);
		AudioListManager audioListManager = AudioListManager.getInstance();
		reciterId = audioListManager.getSelectedReciters()
				.get(selectedReciterPosition).getId();
		String image = "reciter_" + (reciterId);
		Context context = img_reciter.getContext();
		int id = context.getResources().getIdentifier(image, "drawable",
				context.getPackageName());
		try {
			// image_icon
			// .setImageDrawable(activity.getResources().getDrawable(id));
			img_reciter.setImageResource(id);

		} catch (Exception e) {

		}
		image = "reciter_title_" + (reciterId);
		context = img_reciter_name.getContext();
		id = context.getResources().getIdentifier(image, "drawable",
				context.getPackageName());
		try {
			// image_icon
			// .setImageDrawable(activity.getResources().getDrawable(id));
			img_reciter_name.setImageResource(id);

		} catch (Exception e) {

		}
		ImageView btn_load = (ImageView) findViewById(R.id.btn_load);
		btn_load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ly_pb_container.setVisibility(View.VISIBLE);
				ly_cancel_load.setVisibility(View.VISIBLE);
				ly_load.setVisibility(View.INVISIBLE);
				downloadReciter();
			}
		});

		ImageView btn_select = (ImageView) findViewById(R.id.btn_select);
		btn_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AudioListManager audioListManager = AudioListManager
						.getInstance();
				// TODO Auto-generated method stub
				Log.e("selectedReciterPosition", selectedReciterPosition + "");
				Log.e("reciter",
						""
								+ audioListManager.getSelectedReciters()
										.get(selectedReciterPosition).getId());

				Reciters reciter = audioListManager.getSelectedReciters().get(
						selectedReciterPosition);
				audioListManager.setSelectedReciter(reciter, v.getContext());
				ToggleSelectedReciters();
			}
		});

		ImageView btn_cancel_load = (ImageView) findViewById(R.id.btn_cancel_load);
		btn_cancel_load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dBound) {
					dService.CancelDownloads();
					ly_pb_container.setVisibility(View.GONE);
					ly_cancel_load.setVisibility(View.INVISIBLE);
					ly_load.setVisibility(View.VISIBLE);

				}
				// TODO Auto-generated method stub

			}
		});
		ImageView btn_close = (ImageView) findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Intent intent = new Intent(this, DownloadService.class);
		this.bindService(intent, dConnection, Context.BIND_AUTO_CREATE);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.progress_green);
		mProgress = (ProgressBar) findViewById(R.id.progressbar1);
		mProgress.setProgress(0); // Main Progress
		mProgress.setSecondaryProgress(0); // Secondary Progress
		mProgress.setMax(100); // Maximum Progress
		mProgress.setProgressDrawable(drawable);

		ly_pb_container = (RelativeLayout) findViewById(R.id.ly_pb_container);
		ly_pb_container.setVisibility(View.GONE);

		ly_cancel_load = (RelativeLayout) findViewById(R.id.ly_cancel_load);
		ly_cancel_load.setVisibility(View.INVISIBLE);

		ly_load = (RelativeLayout) findViewById(R.id.ly_load);
		ly_load.setVisibility(View.VISIBLE);

		ly_select = (RelativeLayout) findViewById(R.id.ly_select);
		ly_select.setVisibility(View.INVISIBLE);
		fl_selected = (FrameLayout) findViewById(R.id.fl_selected);
		fl_selected.setVisibility(View.GONE);
		ly_main_bg = (RelativeLayout) findViewById(R.id.ly_main_bg);
		// ColorMatrix matrix = new ColorMatrix();
		// matrix.setSaturation(0);
		// ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
		// matrix);
		// ly_main_bg.getBackground().setColorFilter(filter);

		String path = Environment.getExternalStorageDirectory()
				+ "/"
				+ GlobalConfig.audioRootPath
				+ "/"
				+ audioListManager.getSelectedReciters()
						.get(selectedReciterPosition).getId();
		Log.e("-reciter-",
				selectedReciterPosition
						+ " : "
						+ audioListManager.getSelectedReciters()
								.get(selectedReciterPosition).getId());
		// File f = new File(path);
		// filesCount = Utils.getDirectoryfilesCount(f);

	}

	// int filesCount = 0;
	RelativeLayout ly_pb_container;
	RelativeLayout ly_cancel_load;
	RelativeLayout ly_load;
	RelativeLayout ly_select;
	RelativeLayout ly_main_bg;
	FrameLayout fl_selected;
	ProgressBar mProgress;
	int count = 1;
	int currentIndex = 0;
	ArrayList<Suras> verses;
	int ayahCount = 0;

	private void downloadReciter() {
		verses = GlobalConfig.GetmyDbHelper().get_verses(GlobalConfig.lang_id);
		Suras verse = verses.get(currentIndex);
		ayahCount = verse.getAyahCount();
		if (dBound) {
			Intent intent = new Intent(getApplicationContext(),
					DownloadService.class);
			// intent.putExtra(MyMediaPlayerService.START_PLAY, true);
			startService(intent);
			AudioListManager audioListManager = AudioListManager.getInstance();
			Reciters reciter = audioListManager.getSelectedReciters().get(
					selectedReciterPosition);
			Log.e("--->", selectedReciterPosition + " : " + reciter.getId());
			// Reciters reciters = audioListManager.getSelectedReciter().
			AudioClass audioClass = new AudioClass();
			audioClass.setAyahCount(verse.getAyahCount());
			audioClass.setImage("reciter1.jpg");
			audioClass.setPlaceId(1);
			audioClass.setReciterId(reciter.getId());
			audioClass.setReciterName(reciter.getName());

			String versesId = verse.getId() + "";
			if (versesId.length() == 2)
				versesId = "0" + versesId;
			if (versesId.length() == 1)
				versesId = "00" + versesId;

			audioClass.setAudioPath(reciter.getAudioBasePath());
			audioClass.setVerseId(Integer.parseInt(versesId));
			audioClass.setVerseName("asd");
			dService.DownloadSuras(audioClass);

		}
	}

	@Override
	public void onDownloadPreExecute(DownloadClass downloadClass) {
		// TODO Auto-generated method stub

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onDownloadPostExecute(DownloadClass downloadClass) {
		Log.e("finish", "finish");
		// TODO Auto-generated method stub
		if (dBound) {
			int progress = dService.getProgress();
			// ObjectAnimator animation = ObjectAnimator.ofInt(mProgress,
			// "progress",
			// progress, progress + 1);
			// animation.setDuration(990);
			// animation.setInterpolator(new DecelerateInterpolator());
			// animation.start();
			mProgress.setProgress(progress);
			if (dService.getDownloadStatus() == DownloadStatus.FINISH) {

				ly_select.setVisibility(View.VISIBLE);
				ly_pb_container.setVisibility(View.GONE);
				ly_cancel_load.setVisibility(View.INVISIBLE);
				ly_load.setVisibility(View.INVISIBLE);
			}
			waitingFragment.dismiss();
		}

	}

	@Override
	public void onZipExtracting(DownloadClass downloadClass) {
		// TODO Auto-generated method stub
		Log.e("zip", "zip");
		showWaitingDialog();

	}

	@Override
	public void onDownloadProgressUpdate(DownloadClass downloadClass,
			Integer progress) {
		mProgress.setProgress(downloadClass.getProgress());
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadError(DownloadClass downloadClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {

		super.onResume();
		if (!dBound) {

			Intent intent = new Intent(RecitersLoadingActivity.this,
					DownloadService.class);
			bindService(intent, dConnection, Context.BIND_AUTO_CREATE);
		}

	}

	@Override
	public void onBackPressed() {

		// ly_main_bg.getBackground().clearColorFilter();
		finish();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.e("Main activity", "---->destroy");
		if (dBound) {
			dService.unregisterListener(RecitersLoadingActivity.this);
			unbindService(dConnection);
			dBound = false;
		}

	}

	WaitingFragment waitingFragment = null;

	private void showWaitingDialog() {
		if (waitingFragment == null)
			waitingFragment = new WaitingFragment();
		waitingFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);

		waitingFragment.show(getSupportFragmentManager(), "dialog");
		waitingFragment.setCancelable(false);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (dBound) {
			dService.unregisterListener(RecitersLoadingActivity.this);
			unbindService(dConnection);
			dBound = false;
		}
		// ly_main_bg.getBackground().clearColorFilter();
	}

	@Override
	public void onDownloadCancel() {
		// TODO Auto-generated method stub
		mProgress.setProgress(0);
	}

}
