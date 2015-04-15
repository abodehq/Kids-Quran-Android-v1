package com.isslam.kidsquran;

import java.io.IOException;
import java.util.ArrayList;

import com.davemorrissey.labs.subscaleview.ScaleImageView;

import com.isslam.kidsquran.MyMediaPlayer.PlayerInterfaceListener;
import com.isslam.kidsquran.R.id;
import com.isslam.kidsquran.controllers.AudioListManager;
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

public class WordsMeaningFragment extends DialogFragment {

	@Override
	public void onStart() {
		super.onStart();

		// safety check
		if (getDialog() == null) {
			return;
		}

		getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

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

		View v = inflater.inflate(R.layout.ly_words_meaning, container, false);

		ImageView btn_recording_close = (ImageView) v
				.findViewById(R.id.btn_recording_close);

		btn_recording_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				getDialog().cancel();

			}
		});

		getDialog().setCancelable(false);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		fontPath = "fonts/kaleelah.ttf";
		tf = Typeface.createFromAsset(getResources().getAssets(), fontPath);

		txt_words = (TextView) v.findViewById(R.id.txt_words);
		txt_words.setTypeface(tf);
		ShowMeaning();
		return v;
	}

	TextView txt_words;

	public void ShowMeaning() {
		Log.e("sura", AudioListManager.suraId + " : " + AudioListManager.ayahId);
		ArrayList<Tafseer> tafseerList = GlobalConfig.GetmyDbHelper()
				.get_tafseer_by_id(AudioListManager.suraId,
						AudioListManager.ayahId);
		String txt = "";
		Log.e("Length", tafseerList.size() + " : ");
		for (int i = 0; i < tafseerList.size(); i++) {
			String title = tafseerList.get(i).getTitle();
			String content = tafseerList.get(i).getContent();
			txt += (title + " : " + content + " \n ");
			Log.e("title->", title + " : " + content);

		}
		if (tafseerList.size() > 0)
			txt_words.setText(txt);
		else
			txt_words.setText("..·« ÌÊÃœ  ›”Ì— ·Â–Â «·¬Ì…..");
	}

}
