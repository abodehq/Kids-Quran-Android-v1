package com.isslam.kidsquran;

import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReciterFragment extends Fragment {
	static RecitersActivity activity = null;
	Handler handler = new Handler();
	Runnable r;

	public static Fragment newInstance(RecitersActivity context, int pos,
			float scale, boolean IsBlured) {
		activity = context;
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		return Fragment
				.instantiate(context, ReciterFragment.class.getName(), b);
	}

	public boolean showBg = false;
	ImageView myImage;
	ImageView image_icon;
	Animation myRotation;
	int pos = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		r = new Runnable() {
			@Override
			public void run() {
				Log.e("If", "If");
				myImage.setVisibility(View.VISIBLE);
				image_icon.clearColorFilter();
				myImage.startAnimation(myRotation);
			}
		};
		pos = this.getArguments().getInt("pos");
		Log.e("position:->", pos + "");
		AudioListManager audioListManager = AudioListManager.getInstance();
		int reciterId = audioListManager.getSelectedReciters().get(pos).getId();
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.mf,
				container, false);
		myRotation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.rotator);
		image_icon = (ImageView) l.findViewById(R.id.content);
		String image = "reciter_" + (reciterId);
		myImage = (ImageView) l.findViewById(R.id.contentbg);
		ImageView reciter_loading_btn = (ImageView) l
				.findViewById(R.id.reciter_loading_btn);
		if (Utils.isFileExist(reciterId + "", GlobalConfig.filetocheck)) {
			reciter_loading_btn.setVisibility(View.GONE);
		} else {
			reciter_loading_btn.setVisibility(View.VISIBLE);
		}
		if (pos != MyPagerAdapter.lastPage) {
			myImage.setVisibility(View.INVISIBLE);
		}

		Context context = image_icon.getContext();
		int id = context.getResources().getIdentifier(image, "drawable",
				context.getPackageName());
		try {
			// image_icon
			// .setImageDrawable(activity.getResources().getDrawable(id));
			image_icon.setImageResource(id);

		} catch (Exception e) {

		}
		image_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(activity, RecitersLoadingActivity.class);
				i.putExtra("selectedReciter", pos);
				startActivity(i);
			}
		});
		if (pos != MyPagerAdapter.lastPage) {
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(0);
			ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
			image_icon.setColorFilter(filter);
		}

		// TextView tv = (TextView) l.findViewById(R.id.viewID);
		// tv.setText("Position = " + pos);

		MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);
		// float scale = this.getArguments().getFloat("scale");
		// root.setScaleBoth(scale);
		boolean isBlured = this.getArguments().getBoolean("IsBlured");

		return l;
	}

	public void HideIcon(boolean status) {
		if (status)
			image_icon.setVisibility(View.VISIBLE);
		else
			image_icon.setVisibility(View.INVISIBLE);
	}

	public void RotateBgImage(boolean status) {
		if (myImage != null) {
			if (status) {

				handler.removeCallbacks(r);
				handler.postDelayed(r, 500);

			} else {
				myImage.clearAnimation();
				ColorMatrix matrix = new ColorMatrix();
				matrix.setSaturation(0);
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
						matrix);
				image_icon.setColorFilter(filter);
				myImage.setVisibility(View.GONE);
			}

		}
	}

}
