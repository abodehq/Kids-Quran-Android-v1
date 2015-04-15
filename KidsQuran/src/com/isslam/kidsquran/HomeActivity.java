package com.isslam.kidsquran;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HomeActivity extends FragmentActivity {

	protected int _splashTime = 15000; // Time before Run App Main Activity

	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		context = HomeActivity.this;

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// application full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_home);// Splash Layout

		ImageView btn_home_play = (ImageView) findViewById(R.id.btn_home_play);
		btn_home_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubViewPagerActivity
				Intent i = new Intent();
				i.setClass(context, ViewPagerActivity.class);
				startActivity(i);
			}
		});
		ImageView btn_suras = (ImageView) findViewById(R.id.btn_suras);
		btn_suras.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubViewPagerActivity
				Intent i = new Intent();
				i.setClass(context, SurasActivity.class);
				startActivity(i);
			}
		});
		ImageView btn_reciters = (ImageView) findViewById(R.id.btn_reciters);
		btn_reciters.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(context, RecitersActivity.class);
				startActivity(i);
			}
		});
		ImageView btn_achi = (ImageView) findViewById(R.id.btn_achi);
		btn_achi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				// i.setClass(context, AchievementsActivity.class);
				i.setClass(context, AwardsActivity.class);

				startActivity(i);
			}
		});
		final FloatingActionButton btn_share = (FloatingActionButton) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = getString(R.string.share_body) + "\n"
						+ "http://play.google.com/store/apps/details?id="
						+ context.getPackageName();
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						getString(R.string.share_title));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				startActivity(Intent.createChooser(sharingIntent,
						getString(R.string.share_title)));

			}
		});

		final FloatingActionButton btn_exit = (FloatingActionButton) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				exitByBackKey();
			}
		});
		FloatingActionButton btn_rate = (FloatingActionButton) findViewById(R.id.btn_rate);
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

}
