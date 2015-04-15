package com.isslam.kidsquran;

import java.io.IOException;
import java.util.ArrayList;

import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.controllers.SharedPreferencesManager;
import com.isslam.kidsquran.model.Reciters;
import com.isslam.kidsquran.utils.GlobalConfig;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecitersActivity extends FragmentActivity {
	public final static int PAGES = 10;
	// You can choose a bigger number for LOOPS, but you know, nobody will fling
	// more than 1000 times just in order to test your "infinite" ViewPager :D
	public final static int LOOPS = 10;
	public final static int FIRST_PAGE = 0;
	public final static float BIG_SCALE = 1.0f;
	public final static float SMALL_SCALE = 0.8f;
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
	ImageView img_header_bg;
	public RecitersPagerAdapter adapter;
	public ViewPager pager;
	RelativeLayout ly_main_bg;
	RelativeLayout main_layout;
	RelativeLayout header;
	Drawable backgrounds[] = new Drawable[2];
	Context context;

	public float convertFromDp(float input) {
		final float scale = getResources().getDisplayMetrics().density;
		return ((input - 0.5f) / scale);
	}

	Handler handler;
	Runnable runnable;
	Animation anim;
	Animation animout;

	public void onPageChanged() {
		Animation anim2 = AnimationUtils.loadAnimation(RecitersActivity.this,
				R.anim.hide_from_top);
		header.startAnimation(anim2);
	}

	public void OnpageRemains() {
		Animation anim2 = AnimationUtils.loadAnimation(RecitersActivity.this,
				R.anim.grow_from_top);
		header.startAnimation(anim2);
	}

	public void OnpageSelected(final int position, final int lastPage) {

		Animation anim2 = AnimationUtils.loadAnimation(RecitersActivity.this,
				R.anim.grow_from_top);

		header.startAnimation(anim2);
		if (handler == null)
			handler = new Handler();
		if (runnable != null)
			handler.removeCallbacks(runnable);
		runnable = new Runnable() {
			@Override
			public void run() {
				double num = ((Math.random() * 1000) % 360) - 180;

				AudioListManager audioListManager = AudioListManager
						.getInstance();
				int reciterId = audioListManager.getSelectedReciters()
						.get(position).getId();
				img_header_bg.setColorFilter(ColorFilterGenerator
						.adjustHue((float) num));
				String image = "reciter_title_" + reciterId;
				Context context = img_header_bg.getContext();
				int id = context.getResources().getIdentifier(image,
						"drawable", context.getPackageName());
				try {
					img_header_bg.setImageResource(id);

				} catch (Exception e) {

				}

			}
		};
		handler.postDelayed(runnable, 600);
	}

	@Override
	public void onResume() {

		super.onResume();

		adapter.notifyDataSetChanged();
		pager.setAdapter(adapter);
		pager.setCurrentItem(RecitersLoadingActivity.pos);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_reciters);
		context = this;
		ly_main_bg = (RelativeLayout) findViewById(R.id.ly_main_bg);
		header = (RelativeLayout) findViewById(R.id.header);
		img_header_bg = (ImageView) findViewById(R.id.img_header_bg);
		main_layout = (RelativeLayout) findViewById(R.id.main_layout);

		pager = (ViewPager) findViewById(R.id.myviewpager);
		adapter = new RecitersPagerAdapter(this,
				this.getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(adapter);
		pager.setCurrentItem(FIRST_PAGE);
		pager.setOffscreenPageLimit(3);

		// pager.setPageMargin(Integer.parseInt(getString(R.string.pagermargin)));

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

	}

}
