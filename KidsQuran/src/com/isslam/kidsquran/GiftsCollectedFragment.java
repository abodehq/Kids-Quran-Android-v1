package com.isslam.kidsquran;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.davemorrissey.labs.subscaleview.ScaleImageView;

import com.isslam.kidsquran.MyMediaPlayer.PlayerInterfaceListener;
import com.isslam.kidsquran.R.id;
import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.controllers.GiftsManager;
import com.isslam.kidsquran.controllers.SharedPreferencesManager;
import com.isslam.kidsquran.model.AwardsOrder;
import com.isslam.kidsquran.model.Tafseer;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class GiftsCollectedFragment extends DialogFragment {

	@Override
	public void onStart() {
		super.onStart();
		setStyle(STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		// safety check
		if (getDialog() == null) {
			return;
		}

		// ... other stuff you want to do in your onStart() method
	}

	@Override
	public void onCancel(DialogInterface dialog) {

	}

	private String fontPath;
	private Typeface tf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setStyle(STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);

		View v = inflater
				.inflate(R.layout.ly_gifts_collected, container, false);
		ImageView btn_close = (ImageView) v.findViewById(R.id.btn_close);

		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				getDialog().cancel();

			}
		});

		GiftsManager giftsManager = GiftsManager.getInstance();
		ArrayList<AwardsOrder> awardsOrder = giftsManager.GetAwardsList(this
				.getActivity());
		Random random = new Random();
		int cat_id = awardsOrder.get(0).getCatId();
		int item_id = awardsOrder.get(0).getItemId();
		SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
				.getInstance(this.getActivity());
		int id = sharedPreferencesManager.GetIntegerPreferences(
				SharedPreferencesManager._award_order, 1);
		sharedPreferencesManager.savePreferences(
				SharedPreferencesManager._award_order, id + 1);
		ImageView img_item = (ImageView) v.findViewById(R.id.img_item);
		GlobalConfig.GetmyDbHelper().update_gift(cat_id, item_id, 1);
		int resId = getResources().getIdentifier(
				"ach_" + cat_id + "_" + item_id, "drawable",
				getDialog().getWindow().getContext().getPackageName());
		img_item.setImageResource(resId);
		getDialog().setCancelable(false);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		fontPath = "fonts/kaleelah.ttf";
		tf = Typeface.createFromAsset(getResources().getAssets(), fontPath);

		return v;
	}

}
