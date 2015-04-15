package com.isslam.kidsquran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.isslam.kidsquran.MyMediaPlayer.PlayerInterfaceListener;
import com.isslam.kidsquran.R.layout;
import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.controllers.GiftsManager;
import com.isslam.kidsquran.model.AwardsOrder;
import com.isslam.kidsquran.model.Suras;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;
import com.plattysoft.leonids.ParticleSystem;

import android.R.bool;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ViewPagerActivity extends FragmentActivity implements
		PlayerInterfaceListener {

	private static String[] IMAGES;
	private ViewPager page;
	private PagerAdapter pagerAdapter;
	public static int lastPage = 0;
	ImageView img_disabled;
	ImageView btn_eye;
	ImageView img_ayah;
	ImageView img_sura_place;
	boolean isEyeEnabled = false;
	Handler handler;
	Runnable runnable;
	Animation anim;
	Animation animout;
	ImageView btn_memory;
	ImageView img_header_bg;
	MyMediaPlayer myMediaPlayer;
	boolean isPlaying = true;
	ImageView btn_toggle_play;
	ImageView btn_prev;
	ImageView btn_next;
	ImageView btn_record;
	int sound_id = 0;
	int suraId = 114;
	boolean pressed = false;
	ImageView btn_meaning;
	ImageView btn_toggle_mute;
	ImageView btn_toggle_repeat;
	RecordingFragment recordingFragment = null;

	ImageView gem_img;
	ParticleSystem ps;
	RelativeLayout rl;
	boolean GemCreated = false;
	ArrayList<Integer> ayat;

	private void addGemGift() {
		GiftsManager giftsManager = GiftsManager.getInstance();
		ArrayList<AwardsOrder> awardsOrder = giftsManager.GetAwardsList(this);

		boolean bShowGem = false;
		for (int i = 0; i < awardsOrder.size(); i++) {
			Log.e("--award-- ", "id: " + awardsOrder.get(i).getSuraId());
			Log.e("--info-- ", "Sura: " + AudioListManager.suraId);
			if (awardsOrder.get(i).getSuraId() == AudioListManager.suraId
					|| awardsOrder.get(i).getSuraId() == -1) {
				if (awardsOrder.get(i).getAyahId() == AudioListManager.ayahId
						|| awardsOrder.get(i).getAyahId() == -1) {

					bShowGem = true;
					break;
				}
			}
		}
		Log.e("--finish-- ", "" + bShowGem);
		if (bShowGem) {

			rl = (RelativeLayout) findViewById(R.id.pagerContainer);
			if (GemCreated == false)

			{
				gem_img = new ImageView(this);
				gem_img.setImageResource(R.drawable.gems);
				gem_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showGiftsCollectedDialog();
						rl.removeView(v);
						ps.cancel();
						GemCreated = false;
					}
				});

				Handler initHandler = new Handler();

				Runnable initRunnable = new Runnable() {
					@Override
					public void run() {

						rl.removeView(gem_img);
						ps.cancel();
						GemCreated = false;

					}
				};
				initHandler.postDelayed(initRunnable, 6000);

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);

				Random r = new Random();

				lp.leftMargin = r.nextInt(rl.getWidth() - 50);
				lp.topMargin = r.nextInt(rl.getHeight() - 50);
				// gem_img.setTop(r.nextInt(rl.getWidth()-50));
				// gem_img.setLeft(r.nextInt(rl.getHeight()-50));
				rl.addView(gem_img, lp);

				// new ParticleSystem(this, 50, R.drawable.star_pink, 300)
				// .setSpeedRange(0.1f, 0.25f)
				// .emit(lp.leftMargin+rl.getLeft(), lp.topMargin+rl.getTop(),
				// 100);
				// .emit(gem_img, 100);
				ps = new ParticleSystem(this, 100, R.drawable.star_white, 1000);
				ps.setScaleRange(0.7f, 1.3f);
				ps.setSpeedModuleAndAngleRange(0.07f, 0.16f, 0, 180);
				ps.setRotationSpeedRange(90, 180);
				ps.setAcceleration(0.00013f, 90);
				ps.setFadeOut(200, new AccelerateInterpolator());
				ps.emit(lp.leftMargin + rl.getLeft() + 35,
						lp.topMargin + rl.getTop() + 25, 30, 6000);

				GemCreated = true;
			}
		}

	}

	private void memorySura() {
		int width = this.getResources().getDisplayMetrics().widthPixels;
		rl = (RelativeLayout) findViewById(R.id.pagerContainer);
		new ParticleSystem(this, 80, R.drawable.confeti2, 10000)
				.setSpeedModuleAndAngleRange(0f, 0.1f, 180, 180)
				.setRotationSpeed(144).setAcceleration(0.000017f, 90)
				.emit(width, 0, 8, 10000);

		new ParticleSystem(this, 80, R.drawable.confeti3, 10000)
				.setSpeedModuleAndAngleRange(0f, 0.1f, 0, 0)
				.setRotationSpeed(144).setAcceleration(0.000017f, 90)
				.emit(0, 0, 8, 10000);
	}

	private void playSounds(int position) {

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
		String mp3Path = suraId + fileName + ".mp3";
		AudioListManager audioListManager = AudioListManager.getInstance();
		String reciterId = audioListManager.getSelectedReciter().getId() + "";
		Log.e("-->", reciterId);
		if (Utils.isFileExist(reciterId, mp3Path)) {
			mp3Path = Utils.getLocalPath(reciterId) + "/" + suraId + fileName
					+ ".mp3";
			Log.e("-->", mp3Path);
			myMediaPlayer.PlayNewMp3(mp3Path, 1);
		} else {
			// mp3Path = "sounds/1/" + suraId + fileName + ".mp3";
			// myMediaPlayer.PlayNewMp3(mp3Path, 0);
		}
		addGemGift();

	}

	private void playSoundsChilds(int position) {

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
		AudioListManager audioListManager = AudioListManager.getInstance();
		String reciterId = audioListManager.getSelectedReciter().getId() + "";
		String path = suraId + fileName + ".mp3";
		// String mp3Path = "sounds/1/" + suraId + fileName + "_c.mp3";
		String mp3Path = suraId + fileName + "_c.mp3";
		mp3Path = Utils.getLocalPath(reciterId) + "/" + suraId + fileName
				+ "_c.mp3";

		if (Utils.isFileExist("recordings/" + AudioListManager.suraId, path)) {
			mp3Path = Utils.getLocalPath("recordings") + "/"
					+ AudioListManager.suraId + "/" + suraId + fileName
					+ ".mp3";
			Log.e("-->", mp3Path);
			myMediaPlayer.PlayNewMp3(mp3Path, 1);
		} else {
			String _mp3Path = suraId + fileName + "_c.mp3";
			if (Utils.isFileExist(reciterId, _mp3Path))
				myMediaPlayer.PlayNewMp3(mp3Path, 1);
			else {
				sound_id = 0;
				if (isRepeate)
					OnPageSelectedAyah(page.getCurrentItem());
				else {
					if (page.getCurrentItem() == sura.getAyahCount() - 1) {
						isPlaying = false;
						if (runnable != null)
							handler.removeCallbacks(runnable);
						myMediaPlayer.stop();
						btn_toggle_play.setImageResource(R.drawable.drw_play);
					} else
						page.setCurrentItem(page.getCurrentItem() + 1);
				}
				// page.setCurrentItem(page.getCurrentItem() + 1);
			}
		}
		// String mp3Path = "sounds/1/" + suraId + fileName + "_c.mp3";

	}

	public void updateRecordButton() {
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

		String path = suraId + fileName + ".mp3";
		if (Utils.isFileExist("recordings/" + AudioListManager.suraId, path)) {
			btn_record.setImageResource(R.drawable.drw_record_close);
		} else {
			btn_record.setImageResource(R.drawable.drw_record);

		}
	}

	ImageView img_reciter_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(layout.activity_main);

		AudioListManager audioListManager = AudioListManager.getInstance();
		String reciterId = audioListManager.getSelectedReciter().getId() + "";

		img_reciter_title = (ImageView) findViewById(R.id.img_reciter_title);

		int resId = getResources()
				.getIdentifier("reciter_home_title_" + reciterId, "drawable",
						getPackageName());
		img_reciter_title.setImageDrawable(getResources().getDrawable(resId));

		ayat = new ArrayList<Integer>();
		img_header_bg = (ImageView) findViewById(R.id.img_header_bg);
		img_header_bg.setVisibility(View.INVISIBLE);
		img_sura_place = (ImageView) findViewById(R.id.img_sura_place);
		img_sura_place.setVisibility(View.INVISIBLE);
		img_ayah = (ImageView) findViewById(R.id.img_ayah);
		img_ayah.setVisibility(View.INVISIBLE);
		img_disabled = (ImageView) findViewById(R.id.img_disabled);
		img_disabled.setVisibility(View.GONE);
		img_ayah.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				page.setCurrentItem(0);
			}
		});
		btn_eye = (ImageView) findViewById(R.id.btn_eye);
		btn_eye.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isEyeEnabled) {
					img_disabled.setVisibility(View.VISIBLE);
					btn_eye.setImageResource(R.drawable.sura_eye_close);
				} else {
					img_disabled.setVisibility(View.GONE);
					btn_eye.setImageResource(R.drawable.sura_eye_open);
				}
				isEyeEnabled = !isEyeEnabled;
			}
		});
		btn_eye.setVisibility(View.INVISIBLE);
		// -----------------------------
		btn_memory = (ImageView) findViewById(R.id.btn_memory);
		btn_memory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!pressed) {
					AudioListManager _audioListManager = AudioListManager
							.getInstance();
					_audioListManager.UpdateMemory(AudioListManager.suraId, 1);
					btn_memory.setImageResource(R.drawable.btn_memory_pressed);
					memorySura();
				} else {
					AudioListManager _audioListManager = AudioListManager
							.getInstance();
					_audioListManager.UpdateMemory(AudioListManager.suraId, 0);
					btn_memory.setImageResource(R.drawable.btn_memory);
				}

				pressed = !pressed;
			}
		});
		btn_memory.setVisibility(View.INVISIBLE);
		// -----------------------------------
		btn_next = (ImageView) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				page.setCurrentItem(page.getCurrentItem() + 1);
			}
		});
		btn_next.setVisibility(View.INVISIBLE);
		// -----------------------------------
		btn_prev = (ImageView) findViewById(R.id.btn_prev);
		btn_prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				page.setCurrentItem(page.getCurrentItem() - 1);

			}
		});
		btn_prev.setVisibility(View.INVISIBLE);
		// -----------------------------------

		btn_record = (ImageView) findViewById(R.id.btn_record);
		btn_record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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

				String path = suraId + fileName + ".mp3";
				if (Utils.isFileExist("recordings/" + AudioListManager.suraId,
						path)) {
					Utils.deleteFile("recordings/" + AudioListManager.suraId,
							path);
					btn_record.setImageResource(R.drawable.drw_record);
				} else {
					StopPlaying();
					// TODO Auto-generated method stub
					showDialogFragment();
					btn_record.setImageResource(R.drawable.drw_record_close);

				}
				//
				// btn_record.setImageResource(R.drawable.drw_record_close);

			}
		});
		btn_record.setVisibility(View.INVISIBLE);
		btn_toggle_play = (ImageView) findViewById(R.id.btn_toggle_play);
		btn_toggle_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isPlaying) {
					isPlaying = !isPlaying;
					if (runnable != null)
						handler.removeCallbacks(runnable);
					myMediaPlayer.stop();
					btn_toggle_play.setImageResource(R.drawable.drw_play);
				} else {
					isPlaying = !isPlaying;
					ayah_Selected(page.getCurrentItem());
					btn_toggle_play.setImageResource(R.drawable.drw_pause);
				}

			}
		});
		btn_toggle_play.setVisibility(View.INVISIBLE);
		btn_meaning = (ImageView) findViewById(R.id.btn_meaning);
		btn_meaning.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// StopPlaying();
				showMeaningFragment();

			}
		});
		btn_meaning.setVisibility(View.INVISIBLE);

		btn_toggle_mute = (ImageView) findViewById(R.id.btn_toggle_mute);
		btn_toggle_mute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// StopPlaying();
				ToggleMuteSound();

			}
		});

		btn_toggle_repeat = (ImageView) findViewById(R.id.btn_toggle_repeat);
		btn_toggle_repeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// StopPlaying();
				if (isRepeate) {
					btn_toggle_repeat.setImageResource(R.drawable.btn_repeat);
				} else {
					btn_toggle_repeat
							.setImageResource(R.drawable.btn_repeat_pressed);
				}
				isRepeate = !isRepeate;

			}
		});

		btn_toggle_mute.setVisibility(View.GONE);
		btn_toggle_repeat.setVisibility(View.GONE);
		img_reciter_title.setVisibility(View.GONE);

		Handler initHandler = new Handler();

		Runnable initRunnable = new Runnable() {
			@Override
			public void run() {

				RunInit();

			}
		};
		initHandler.postDelayed(initRunnable, 500);

	}

	boolean isRepeate = false;

	private void ToggleMuteSound() {
		if (myMediaPlayer.PlauerVolumeStatus()) {
			myMediaPlayer.Unmute();
			btn_toggle_mute.setImageResource(R.drawable.drw_unmute);
		} else {

			myMediaPlayer.mutePlayer();
			btn_toggle_mute.setImageResource(R.drawable.drw_mute);
		}
	}

	private void RunInit() {

		FillData();
		setImages();
		btn_memory.setVisibility(View.VISIBLE);
		if (sura.getMemorize() == 1) {
			pressed = true;
			btn_memory.setImageResource(R.drawable.btn_memory_pressed);
		} else {
			pressed = false;
			btn_memory.setImageResource(R.drawable.btn_memory);
		}
		pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		page = (ViewPager) findViewById(R.id.myviewpager);
		page.setAdapter(pagerAdapter);
		page.setOffscreenPageLimit(3);
		page.setOnPageChangeListener((OnPageChangeListener) pagerAdapter);
		RelativeLayout ly_main_bg = (RelativeLayout) findViewById(R.id.ly_main_bg);
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		setAyah(0);
		myMediaPlayer = MyMediaPlayer.getInstance(this);
		myMediaPlayer.registerListener(this);

		Animation anim = AnimationUtils.loadAnimation(this, R.anim.popup_show);
		img_header_bg.startAnimation(anim);
		img_header_bg.setVisibility(View.VISIBLE);

		Animation animstrip = AnimationUtils.loadAnimation(this,
				R.anim.strip_show);
		btn_next.startAnimation(animstrip);
		btn_next.setVisibility(View.VISIBLE);

		btn_eye.setVisibility(View.VISIBLE);

		img_sura_place.setVisibility(View.VISIBLE);
		img_ayah.setVisibility(View.VISIBLE);

		Handler initHandler = new Handler();

		Runnable initRunnable = new Runnable() {
			@Override
			public void run() {

				PlayAnimation();

			}
		};
		initHandler.postDelayed(initRunnable, 500);

	}

	private void PlayAnimation() {

		Animation animBottom = AnimationUtils.loadAnimation(this,
				R.anim.grow_from_bottom);
		btn_toggle_play.startAnimation(animBottom);
		btn_toggle_play.setVisibility(View.VISIBLE);

		Animation animBottom2 = AnimationUtils.loadAnimation(this,
				R.anim.grow_from_bottom_2);
		btn_meaning.startAnimation(animBottom2);
		btn_meaning.setVisibility(View.VISIBLE);

		Animation animBottom3 = AnimationUtils.loadAnimation(this,
				R.anim.grow_from_bottom_3);
		btn_record.startAnimation(animBottom3);

		AudioListManager audioListManager = AudioListManager.getInstance();
		String reciterId = audioListManager.getSelectedReciter().getId() + "";

		btn_record.setVisibility(View.VISIBLE);
		btn_toggle_play.setImageResource(R.drawable.drw_pause);

		Log.e("-->", reciterId);
		if (!Utils.isFileExist(reciterId, GlobalConfig.filetocheck)) {
			NoRecitersFragment noRecitersFragment = new NoRecitersFragment();
			noRecitersFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
					android.R.style.Theme_Holo_NoActionBar_Fullscreen);

			noRecitersFragment.show(getSupportFragmentManager(),
					"noRecitersFragment");
		}
		ayah_Selected(0);

	}

	private void StopPlaying() {
		isPlaying = false;
		if (runnable != null)
			handler.removeCallbacks(runnable);
		myMediaPlayer.stop();
		btn_toggle_play.setImageResource(R.drawable.drw_play);
	}

	private void ayah_Selected(final int position) {
		sound_id = 0;
		btn_prev.setVisibility(View.VISIBLE);
		btn_next.setVisibility(View.VISIBLE);
		if (position == 0) {
			btn_prev.setVisibility(View.INVISIBLE);

		}
		if (position == conut) {
			btn_next.setVisibility(View.INVISIBLE);
		}
		AudioListManager.ayahId = position;
		if ((GlobalConfig.GetmyDbHelper().get_tafseer_by_id(
				AudioListManager.suraId, AudioListManager.ayahId)).size() > 0) {
			btn_meaning.setVisibility(View.VISIBLE);
		} else {
			btn_meaning.setVisibility(View.GONE);
		}
		setAyah(position);
		if (handler == null)
			handler = new Handler();
		if (runnable != null)
			handler.removeCallbacks(runnable);
		runnable = new Runnable() {
			@Override
			public void run() {
				double num = ((Math.random() * 1000) % 360) - 180;
				playSounds(position);

			}
		};

		AudioListManager audioListManager = AudioListManager.getInstance();
		String reciterId = audioListManager.getSelectedReciter().getId() + "";

		Log.e("-->", reciterId);
		if (Utils.isFileExist(reciterId, GlobalConfig.filetocheck)) {

			btn_toggle_mute.setVisibility(View.VISIBLE);
			btn_toggle_repeat.setVisibility(View.VISIBLE);
			img_reciter_title.setVisibility(View.VISIBLE);
			handler.postDelayed(runnable, 600);
		} else {
			btn_record.setVisibility(View.GONE);
			btn_toggle_play.setVisibility(View.GONE);
			btn_toggle_mute.setVisibility(View.GONE);
			btn_toggle_repeat.setVisibility(View.GONE);
			img_reciter_title.setVisibility(View.GONE);
		}

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

		String path = suraId + fileName + ".mp3";
		// String mp3Path = "sounds/1/" + suraId + fileName + "_c.mp3";

		if (Utils.isFileExist("recordings/" + AudioListManager.suraId, path)) {
			btn_record.setImageResource(R.drawable.drw_record_close);
		} else {
			btn_record.setImageResource(R.drawable.drw_record);
		}
	}

	private void showDialogFragment() {

		if (recordingFragment == null)
			recordingFragment = new RecordingFragment();
		recordingFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		recordingFragment
				.show(getSupportFragmentManager(), "recordingFragment");
		recordingFragment.setCancelable(false);

	}

	WordsMeaningFragment wordsMeaningFragment = null;

	private void showMeaningFragment() {
		if (wordsMeaningFragment == null)
			wordsMeaningFragment = new WordsMeaningFragment();
		wordsMeaningFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);

		wordsMeaningFragment.show(getSupportFragmentManager(),
				"wordsMeaningFragment");

	}

	GiftsCollectedFragment giftsCollectedFragment = null;

	private void showGiftsCollectedDialog() {
		if (giftsCollectedFragment == null)
			giftsCollectedFragment = new GiftsCollectedFragment();
		giftsCollectedFragment.setStyle(DialogFragment.STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);

		giftsCollectedFragment.show(getSupportFragmentManager(),
				"giftsCollectedFragment");

	}

	@Override
	public void onBackPressed() {
		if (runnable != null)
			handler.removeCallbacks(runnable);
		myMediaPlayer.stop();
		myMediaPlayer.unregisterListener(this);
		StopPlaying();
		finish();
		super.onBackPressed();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onStop() {
		StopPlaying();
		super.onStop();
	}

	@Override
	public void onResume() {

		super.onResume();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}

	@TargetApi(16)
	public void OnpageSelected(final int position, final int lastPage) {
		OnPageSelectedAyah(position);
	}

	private void OnPageSelectedAyah(int position) {
		isPlaying = true;
		btn_toggle_play.setImageResource(R.drawable.drw_pause);
		ayah_Selected(position);
		if (wordsMeaningFragment != null)
			((WordsMeaningFragment) wordsMeaningFragment).ShowMeaning();
	}

	int conut = 0;
	Suras sura;

	private void FillData() {
		AudioListManager audioListManager = AudioListManager.getInstance();
		suraId = AudioListManager.suraId;
		audioListManager.updateSuraList();
		sura = audioListManager.getSelectedSura();
		Log.e("updated sura", sura.getId() + " : " + sura.getStars());
		int conut = sura.getAyahCount() + 1;
		IMAGES = new String[conut - 1];
		for (int i = 0; i < conut - 1; i++) {
			if (i == 0)
				IMAGES[i] = "s_b_0.png";
			else
				IMAGES[i] = "images/" + suraId + "/s_" + suraId + "_" + (i)
						+ ".png";
		}

	}

	private void setImages() {

		int resId = getResources().getIdentifier("s_t_" + suraId, "drawable",
				getPackageName());
		img_header_bg.setImageDrawable(getResources().getDrawable(resId));

		resId = getResources().getIdentifier("sura_place_" + sura.getPlaceId(),
				"drawable", getPackageName());
		img_sura_place.setImageDrawable(getResources().getDrawable(resId));

	}

	private void setAyah(int position) {
		int resId = -1;

		if (position == 0) {
			AudioListManager audioListManager = AudioListManager.getInstance();
			Suras sura = audioListManager.getSelectedSura();
			conut = sura.getAyahCount() - 1;
			resId = getResources().getIdentifier("ayah_" + conut, "drawable",
					getPackageName());

		} else {
			resId = getResources().getIdentifier("ayah_" + position,
					"drawable", getPackageName());

		}
		if (resId != -1)
			img_ayah.setImageDrawable(getResources().getDrawable(resId));
		if (position == 0) {
			double num2 = ((Math.random() * 1000) % 360) - 180;
			num2 = 117;
			img_ayah.setColorFilter(ColorFilterGenerator
					.adjustHue((float) num2));

		} else {
			img_ayah.clearColorFilter();
		}

	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
			implements ViewPager.OnPageChangeListener {

		private FragmentManager _fm;

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
			_fm = fm;
		}

		@Override
		public Fragment getItem(int position) {
			return new ViewPagerFragment(IMAGES[position]);
		}

		public void clearAll() // Clear all page
		{
			List<Fragment> fragments = _fm.getFragments();
			for (int i = 0; i < fragments.size(); i++)
				_fm.beginTransaction().remove(fragments.get(i)).commit();
			fragments.clear();
		}

		@Override
		public int getCount() {
			return IMAGES.length;
		}

		@Override
		public void onPageScrollStateChanged(int position) {
			// TODO Auto-generated method stub
			// Log.e("stateChanged","state : "+position);
		}

		@Override
		public void onPageScrolled(int arg0, float position, int arg2) {
			// TODO Auto-generated method stub
			// Log.e("stateChanged","state : "+position);

		}

		@Override
		public void onPageSelected(int position) {
			// Log.e("stateChanged","state : "+position);
			// TODO Auto-generated method stub
			((ViewPagerActivity) ViewPagerActivity.this).OnpageSelected(
					position, lastPage);
			lastPage = position;
		}
	}

	@Override
	public void onCompletionMp3() {
		// TODO Auto-generated method stub
		if (isPlaying) {
			AudioListManager audioListManager = AudioListManager.getInstance();
			Suras sura = audioListManager.getSelectedSura();
			Log.e("stars", sura.getId() + " : " + sura.getStars() + " : ");
			boolean isexist = false;
			if (sura.getStars() == 0) {
				for (int i = 0; i < ayat.size(); i++) {
					if (ayat.get(i) == page.getCurrentItem()) {
						isexist = true;
						break;
					}
				}
			} else
				isexist = true;

			Log.e("isexist", isexist + " : ");
			if (!isexist) {
				ayat.add(page.getCurrentItem());

				Log.e("info", ayat.size() + " : " + sura.getAyahCount());
				if (ayat.size() == sura.getAyahCount()) {
					Log.e("finishListen!!", ":D");
					StarsCollectedFragment starsCollectedFragment = new StarsCollectedFragment();
					starsCollectedFragment.setStyle(
							DialogFragment.STYLE_NO_FRAME,
							android.R.style.Theme_Holo_NoActionBar_Fullscreen);

					starsCollectedFragment.show(getSupportFragmentManager(),
							"starsCollectedFragment");
					audioListManager.UpdateStras(sura.getId(), 1);
				} else {
					Log.e("Not Finished!!", ":(");
				}

			}

			if (sound_id == 0) {
				playSoundsChilds(0);
				sound_id = 1;
			} else {
				sound_id = 0;

				if (isRepeate)
					OnPageSelectedAyah(page.getCurrentItem());
				else {
					if (page.getCurrentItem() == sura.getAyahCount() - 1) {
						isPlaying = false;
						if (runnable != null)
							handler.removeCallbacks(runnable);
						myMediaPlayer.stop();
						btn_toggle_play.setImageResource(R.drawable.drw_play);
					} else
						page.setCurrentItem(page.getCurrentItem() + 1);
				}
			}
		}

	}

	@Override
	public void onErrorMp3() {
		// TODO Auto-generated method stub

	}

	@Override
	public void isPreparedMp3() {
		// TODO Auto-generated method stub

	}

	@Override
	public void TogglePlay(Boolean status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onstartMp3() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNewMp3() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onToggleRepeat(Boolean bRepeat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onToggleShuffle(Boolean bShuffle) {
		// TODO Auto-generated method stub

	}

}
