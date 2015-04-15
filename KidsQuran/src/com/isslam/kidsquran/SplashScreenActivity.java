package com.isslam.kidsquran;

import java.util.Random;
import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.controllers.SharedPreferencesManager;
import com.isslam.kidsquran.model.DataBaseHelper;
import com.isslam.kidsquran.model.DataBaseHelper.DataBaseHelperInterface;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class SplashScreenActivity extends FragmentActivity implements
		DataBaseHelperInterface {

	protected int _splashTime = 15000; // Time before Run App Main Activity

	private Context context;

	LinearLayout ly_slogan;
	LinearLayout ly_app;
	RelativeLayout ly_ma;
	RelativeLayout ly_strip;
	FloatingActionButton btn_rate;
	FloatingActionButton btn_other;
	FrameLayout ly_enter;
	ImageView contentbg;
	ProgressBar mProgress;
	RelativeLayout ly_pb_container;
	private DataBaseHelper myDbHelper;
	DataBaseHelperInterface dataBaseHelperInterface;
	private Thread splashTread;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		context = SplashScreenActivity.this;

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// application full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.ly_splash_screen);// Splash Layout

		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(context);
		GlobalConfig.local = sharedPreferencesManager.GetStringPreferences(
				SharedPreferencesManager._local, GlobalConfig.local);
		GlobalConfig.lang_id = sharedPreferencesManager.GetStringPreferences(
				SharedPreferencesManager._lang_id, GlobalConfig.lang_id);
		Utils.updateLocal(context, GlobalConfig.local, GlobalConfig.lang_id);
		GlobalConfig._DBcontext = getApplicationContext().getPackageName();

		dataBaseHelperInterface = this;

		ly_pb_container = (RelativeLayout) findViewById(R.id.ly_pb_container);
		ly_pb_container.setVisibility(View.GONE);
		ly_slogan = (LinearLayout) findViewById(R.id.ly_slogan);
		ly_app = (LinearLayout) findViewById(R.id.ly_app);
		ly_ma = (RelativeLayout) findViewById(R.id.ly_main_splash);
		ly_strip = (RelativeLayout) findViewById(R.id.ly_strip);

		contentbg = (ImageView) findViewById(R.id.contentbg);
		contentbg.setVisibility(View.GONE);
		ly_enter = (FrameLayout) findViewById(R.id.ly_enter);
		ly_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ly_enter.setVisibility(View.GONE);
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(context, HomeActivity.class);
				startActivity(i);
				finish();
			}
		});
		ly_enter.setVisibility(View.GONE);

		btn_other = (FloatingActionButton) findViewById(R.id.btn_other);
		btn_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri
							.parse("https://play.google.com/store/apps/developer?id=islam+is+the+way+of+life"));
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ context.getPackageName())));
				}

			}
		});
		btn_other.setVisibility(View.GONE);
		btn_rate = (FloatingActionButton) findViewById(R.id.btn_rate);
		btn_rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("market://details?id="
						+ context.getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
						| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ context.getPackageName())));
				}

			}
		});
		btn_rate.setVisibility(View.GONE);

		Animation anim = AnimationUtils.loadAnimation(
				SplashScreenActivity.this, R.anim.fade_out);
		ly_app.startAnimation(anim);
		ly_slogan.setVisibility(View.GONE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {

				ly_slogan.setVisibility(View.VISIBLE);
				ly_app.setVisibility(View.GONE);
				Animation anim = AnimationUtils.loadAnimation(
						SplashScreenActivity.this, R.anim.fade_in);
				ly_slogan.startAnimation(anim);
				CopyFiles CopyFilesa = new CopyFiles();
				CopyFilesa.execute();
			}
		}, 3000);

		try {
			if (myDbHelper == null)
				myDbHelper = new DataBaseHelper(dataBaseHelperInterface);
		} catch (Exception e) {
			e.getStackTrace();
		}

		if (!myDbHelper.checkDataBase()) {
			// TextLoadingFirst.setVisibility(View.VISIBLE);
			// GlobalConfig.ShowLongToast(this,
			// " Õ„Ì· «·ﬂ «Ì ·√Ê· „—… /n «·—Ã«¡ «·«‰ Ÿ«—");
			// book_txt.setVisibility(book_txt.VISIBLE);

		}
		// /wait for 2 seconds and then show the loading image.

		final long DELAY = 1000; // in ms
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		Log.e("width", "w: " + screenWidth + "; H : " + screenHeight);

	}

	private void playanim() {

		Animation animf = AnimationUtils.loadAnimation(
				SplashScreenActivity.this, R.anim.strip_hide);
		ly_strip.startAnimation(animf);
		animf.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				ly_pb_container.setVisibility(View.GONE);
				ly_strip.setVisibility(View.GONE);
				btn_rate.setVisibility(View.VISIBLE);
				btn_other.setVisibility(View.VISIBLE);
				Animation animb = AnimationUtils.loadAnimation(
						SplashScreenActivity.this, R.anim.grow_from_bottom);
				btn_rate.startAnimation(animb);
				btn_other.startAnimation(animb);
				ly_enter.setVisibility(View.VISIBLE);
				Animation animc = AnimationUtils.loadAnimation(
						SplashScreenActivity.this, R.anim.grow_from_top_speed);
				ly_enter.startAnimation(animc);
				Animation myRotation = AnimationUtils.loadAnimation(
						SplashScreenActivity.this, R.anim.rotator);
				contentbg.setVisibility(View.VISIBLE);
				contentbg.startAnimation(myRotation);

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {// check if user Click on
													// the splash to start app
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// synchronized (splashTread) {
			// splashTread.notifyAll();
			// }
		}
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitByBackKey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void exitByBackKey() {

		CloseAppFragment closeAppFragment = new CloseAppFragment();
		closeAppFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		closeAppFragment.show(getSupportFragmentManager(), "closeappfragment");
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public void onRequestCompleted() {
		// TODO Auto-generated method stub

	}

	public class CopyFiles extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		Runnable run;

		@Override
		protected void onPostExecute(String x) {
			super.onPostExecute("");
			Log.e("onpost", "onpost");
			GlobalConfig.myDbHelper = myDbHelper;
			SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
					.getInstance(context);

			sharedPreferencesManager.SetupPreferences();
			AudioListManager audioListManager = AudioListManager.getInstance();
			int reciterId = sharedPreferencesManager.GetIntegerPreferences(
					SharedPreferencesManager._reciter_id, 4);
			audioListManager.updateAllreciters(reciterId, context);
			audioListManager.updateAllSuras();

			// unzipFromAssets(context, "/assets/1.zip",
			// Environment.getExternalStorageDirectory() +
			// "/"+GlobalConfig.audioRootPath+"/"
			// + "1/");
			if (!Utils.isFileExist("4", GlobalConfig.filetocheck)) {
				ly_pb_container.setVisibility(View.VISIBLE);
				ExtractReciterThread extractReciterThread = new ExtractReciterThread(
						SplashScreenActivity.this, context, "/assets/4.zip",
						Environment.getExternalStorageDirectory() + "/"
								+ GlobalConfig.audioRootPath + "/" + "4/");
				extractReciterThread.start();

				Resources res = getResources();
				Drawable drawable = res.getDrawable(R.drawable.progress_green);
				mProgress = (ProgressBar) findViewById(R.id.progressbar1);
				mProgress.setProgress(0); // Main Progress
				mProgress.setSecondaryProgress(0); // Secondary Progress
				mProgress.setMax(100); // Maximum Progress
				mProgress.setProgressDrawable(drawable);
				final Random r = new Random();
				final Handler handler = new Handler();
				run = new Runnable() {
					public void run() {
						int rand = r.nextInt(8);
						if (mProgress.getProgress() < 100)
							mProgress.setProgress(mProgress.getProgress()
									+ rand);
						if (mProgress.getProgress() > 100)
							mProgress.setProgress(100);
						handler.postDelayed(run, 900);
					}
				};
				handler.postDelayed(run, 900);
			} else {

				ly_pb_container.setVisibility(View.GONE);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						playanim();

						// startActivity(i);
						// finish();
						// Actions to do after 10 seconds
					}
				}, 2000);

			}

			if (sharedPreferencesManager.getBooleanPreferences(
					SharedPreferencesManager._first_run, true)) {
				sharedPreferencesManager.savePreferences(
						SharedPreferencesManager._first_run, false);
				// showLanguageslist();

			} else {

			}

		}

		@Override
		protected String doInBackground(Void... arg0) {
			Log.e("int", "int");
			myDbHelper.InitDB();
			// TODO Auto-generated method stub
			return "";
		}
	}

	public void showLanguageslist() {
		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(false);

		dialog.setTitle(context.getResources().getString(
				R.string.setting_language_title));
		ListView modeList = new ListView(context);
		final Context _context = context;
		modeList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String[] data = getResources().getStringArray(
						R.array.arr_languages_locals);
				String[] lang_ids = getResources().getStringArray(
						R.array.arr_languages_ids);
				Utils.updateLocal(_context, data[position], lang_ids[position]);

				dialog.cancel();
				Intent i = new Intent();
				i.setClass(_context, HomeActivity.class);
				startActivity(i);
				finish();

			}
		});
		String[] languages = getResources().getStringArray(
				R.array.arr_languages_items);

		LanguagesAdapter adapter1 = new LanguagesAdapter(context, languages);

		modeList.setAdapter(adapter1);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(modeList);
		dialog.show();

	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {
			showLanguageslist();

		}
	};

	public Handler activityHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * Handling MESSAGE_UPDATE_PROGRESS_BAR: 1. Get the current
			 * progress, as indicated in the arg1 field of the Message. 2.
			 * Update the progress bar.
			 */
			case 100:
				Log.e("start", "start");
				break;
			case 200:
				Log.e("END", "END");
				mProgress.setProgress(100);
				playanim();
				break;
			}
		}
	};

}
