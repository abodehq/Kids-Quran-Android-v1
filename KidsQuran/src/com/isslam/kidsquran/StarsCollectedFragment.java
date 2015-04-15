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
import com.plattysoft.leonids.ParticleSystem;

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
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StarsCollectedFragment extends DialogFragment {

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setStyle(STYLE_NO_FRAME,
				android.R.style.Theme_Holo_NoActionBar_Fullscreen);

		View v = inflater
				.inflate(R.layout.ly_stars_collected, container, false);
		ImageView btn_close = (ImageView) v.findViewById(R.id.btn_close);

		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				getDialog().cancel();

			}
		});

		getDialog().setCancelable(false);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		return v;
	}

}
