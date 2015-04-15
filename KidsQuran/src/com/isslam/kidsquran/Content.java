package com.isslam.kidsquran;

import com.isslam.kidsquran.controllers.SharedPreferencesManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Content extends FragmentActivity {

	Context context;

	private void playSounds(int position) {
		MediaManager mediaManager = MediaManager.getInstance(this);
		mediaManager.setContext(this);
		mediaManager.playSounds(position, 1);
	}

	Handler handler;
	Runnable runnable;
	Animation anim2;
	ImageView img_header_bg;
	RelativeLayout main_layout;
	TextView txt_theker;

	@TargetApi(16)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.content_ly);
		context = this;
		main_layout = (RelativeLayout) findViewById(R.id.main_layout);
		// txt_theker = (TextView) findViewById(R.id.txt_theker);
		// txt_theker.setVisibility(View.GONE);
		String fontPath = "fonts/aref_light.ttf";// kufyan.otf
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		txt_theker.setTypeface(tf);
		// txt_theker.setText(athkar_titles);
		// txt_theker.setShadowLayer(30, 0, 0, Color.BLACK);
		anim2 = AnimationUtils.loadAnimation(this, R.anim.popup_show);
		img_header_bg = (ImageView) findViewById(R.id.img_header_bg);
		anim2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				ColorMatrix matrix = new ColorMatrix();
				matrix.setSaturation(0);
				txt_theker.setVisibility(View.VISIBLE);
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
						matrix);
				main_layout.getBackground().setColorFilter(filter);
			}
		});
		img_header_bg.setVisibility(View.INVISIBLE);

		String image = "img_bg_" + MyPagerAdapter.lastPage;

		Context _context = main_layout.getContext();
		int id = _context.getResources().getIdentifier(image, "drawable",
				_context.getPackageName());
		Drawable background = getResources().getDrawable(id);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			main_layout.setBackgroundDrawable(background);

		} else {

			main_layout.setBackground(background);
		}
		if (handler == null)
			handler = new Handler();
		if (runnable != null)
			handler.removeCallbacks(runnable);
		runnable = new Runnable() {
			@Override
			public void run() {
				playSounds(MyPagerAdapter.lastPage);

				img_header_bg.startAnimation(anim2);
				img_header_bg.setVisibility(View.VISIBLE);

			}
		};
		handler.postDelayed(runnable, 600);

		final FloatingActionButton btn_share = (FloatingActionButton) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);

				String shareBody = "" + "\n" + getString(R.string.share_extra)
						+ "\n"
						+ "http://play.google.com/store/apps/details?id="
						+ context.getPackageName();
				sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
				sendIntent.setType("text/plain");

				startActivity(Intent.createChooser(sendIntent, ""));

			}
		});

	}

	@Override
	public void onBackPressed() {

		GotoHome();

	}

	private void GotoHome() {
		MediaManager mediaManager = MediaManager.getInstance(this);
		mediaManager.setContext(this);
		mediaManager.StopSounds();
		if (main_layout != null)
			main_layout.getBackground().clearColorFilter();
		txt_theker.setVisibility(View.GONE);
		anim2 = AnimationUtils.loadAnimation(Content.this, R.anim.popup_hide);
		anim2.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				img_header_bg.setVisibility(View.GONE);
				finish();
			}
		});
		img_header_bg.startAnimation(anim2);
		img_header_bg.setVisibility(View.VISIBLE);
	}

}
