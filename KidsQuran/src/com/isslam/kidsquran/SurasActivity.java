package com.isslam.kidsquran;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SurasActivity extends FragmentActivity {
	public final static int PAGES = 5;
	// You can choose a bigger number for LOOPS, but you know, nobody will fling
	// more than 1000 times just in order to test your "infinite" ViewPager :D
	public final static int LOOPS = 10;
	public final static int FIRST_PAGE = 4;
	public final static float BIG_SCALE = 1.0f;
	public final static float SMALL_SCALE = 0.8f;
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

	public SurasPagerAdapter adapter;
	public ViewPager pager;
	RelativeLayout ly_main_bg;
	RelativeLayout main_layout;

	Drawable backgrounds[] = new Drawable[2];
	Context context;

	private void playSounds(int position) {

		// MediaManager mediaManager = MediaManager.getInstance(this);
		// mediaManager.setContext(this);
		// mediaManager.playSounds(position, 0);

	}

	public float convertFromDp(float input) {
		final float scale = getResources().getDisplayMetrics().density;
		return ((input - 0.5f) / scale);
	}

	Handler handler;
	Runnable runnable;
	Animation anim;
	Animation animout;

	public void onPageChanged() {

	}

	public void OnpageRemains() {

	}

	@TargetApi(16)
	public void OnpageSelected(final int position, final int lastPage) {
		Log.e("athkar", "athkar");

		ImageView img_pos = (ImageView) ly_dots.getChildAt(position);
		ImageView img_last = (ImageView) ly_dots.getChildAt(lastPage);

		img_last.setImageResource(R.drawable.dot);
		img_pos.setImageResource(R.drawable.dot_selected);

		Animation anim2 = AnimationUtils.loadAnimation(SurasActivity.this,
				R.anim.grow_from_top);

		if (handler == null)
			handler = new Handler();
		if (runnable != null)
			handler.removeCallbacks(runnable);
		runnable = new Runnable() {
			@Override
			public void run() {
				double num = ((Math.random() * 1000) % 360) - 180;

				OnpageSelectedNow(position, lastPage);
				playSounds(position);

			}
		};
		handler.postDelayed(runnable, 600);
	}

	@SuppressLint("NewApi")
	public void OnpageSelectedNow(int position, int lastPage) {

		String image = "img_bg_" + position;
		Log.e("image", image);
		Context context = ly_main_bg.getContext();
		int id = context.getResources().getIdentifier(image, "drawable",
				context.getPackageName());

		// Log.e("---->",id+"");
		backgrounds[0] = ly_main_bg.getBackground();// res.getDrawable(R.drawable.img_bg_1);
		// backgrounds[1] = getResources().getDrawable(id);
		backgrounds[1] = backgrounds[0];
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// main_layout.setBackgroundDrawable(backgrounds[0]);
			// ly_main_bg.setBackgroundDrawable(backgrounds[1]);

		} else {
			// main_layout.setBackground(backgrounds[0]);
			// ly_main_bg.setBackground(backgrounds[1]);
		}

		// anim = AnimationUtils.loadAnimation(SurasActivity.this,
		// R.anim.fade_in);
		// ly_main_bg.startAnimation(anim);

		// crossfader.startTransition(100);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suras);
		context = this;

		ly_dots = (LinearLayout) findViewById(R.id.ly_dots);
		createImages();

		ly_main_bg = (RelativeLayout) findViewById(R.id.ly_main_bg);

		main_layout = (RelativeLayout) findViewById(R.id.main_layout);

		// ColorFilterGenerator.setAlpha(img_header_bg, 0.8f);

		ImageView btn_next = (ImageView) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(pager.getCurrentItem() + 1);
			}
		});
		ImageView btn_prev = (ImageView) findViewById(R.id.btn_prev);
		btn_prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(pager.getCurrentItem() - 1);

			}
		});

		// String fontPath = "fonts/neckar.ttf";
		// Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

		loadingImage = (ImageView) findViewById(R.id.imageView1);
		final Animation myRotation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.rotator);
		loadingImage.startAnimation(myRotation);

		Handler initHandler = new Handler();

		Runnable initRunnable = new Runnable() {
			@Override
			public void run() {

				RunInit();

			}
		};
		initHandler.postDelayed(initRunnable, 500);

	}

	ImageView loadingImage;

	private void RunInit() {
		loadingImage.clearAnimation();
		loadingImage.setVisibility(View.GONE);
		pager = (ViewPager) findViewById(R.id.myviewpager);

		adapter = new SurasPagerAdapter(this, this.getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(adapter);
		pager.setCurrentItem(FIRST_PAGE);
		pager.setOffscreenPageLimit(3);
	}

	LinearLayout ly_dots;

	void createImages() {
		for (int i = 0; i < 5; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(R.drawable.dot);

			ly_dots.addView(imageView, i);
		}
	}

}
