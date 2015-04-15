package com.isslam.kidsquran;

import com.isslam.kidsquran.controllers.AudioListManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

public class MyFragment extends Fragment {
	static MainActivity activity = null;
	Handler handler = new Handler();
	Runnable r;

	public static Fragment newInstance(MainActivity context, int pos,
			float scale, boolean IsBlured) {
		activity = context;
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);

		return Fragment.instantiate(context, MyFragment.class.getName(), b);
	}

	public boolean showBg = false;
	ImageView myImage;
	ImageView image_icon;
	Animation myRotation;

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
				// myImage.setVisibility(View.VISIBLE);
				image_icon.clearColorFilter();
				// myImage.startAnimation(myRotation);
			}
		};
		Log.e("create", "fragment_new");
		int pos = this.getArguments().getInt("pos");
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.mf,
				container, false);
		myRotation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.rotator);
		image_icon = (ImageView) l.findViewById(R.id.content);
		String image = "s_" + AudioListManager.suraId + "_" + pos;
		if (pos == 0)
			image = "s_b_0";
		myImage = (ImageView) l.findViewById(R.id.contentbg);
		if (pos != MyPagerAdapter.lastPage) {
			myImage.setVisibility(View.INVISIBLE);
		}
		myImage.setVisibility(View.INVISIBLE);

		Context context = image_icon.getContext();
		int id = context.getResources().getIdentifier(image, "drawable",
				context.getPackageName());
		try {
			image_icon
					.setImageDrawable(activity.getResources().getDrawable(id));

		} catch (Exception e) {

		}
		image_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(activity, Content.class);
				i.putExtra("selectedTab", "1");
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
		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);
		boolean isBlured = this.getArguments().getBoolean("IsBlured");

		return l;
	}

	public void HideIcon(boolean status) {
		if (status)
			image_icon.setVisibility(View.VISIBLE);
		else
			image_icon.setVisibility(View.INVISIBLE);
	}

	public void ClearMemory() {

		Drawable drawable = image_icon.getDrawable();
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable != null) {
				Bitmap bitmap = bitmapDrawable.getBitmap();
				if (bitmap != null && !bitmap.isRecycled())
					bitmap.recycle();
			}
		}
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
