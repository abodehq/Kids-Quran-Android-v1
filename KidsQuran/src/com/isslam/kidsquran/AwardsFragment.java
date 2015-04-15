package com.isslam.kidsquran;

import java.util.ArrayList;

import com.isslam.kidsquran.controllers.GiftsManager;
import com.isslam.kidsquran.model.Gifts;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AwardsFragment extends Fragment {
	static AwardsActivity activity = null;
	Handler handler = new Handler();
	Runnable r;

	public static Fragment newInstance(AwardsActivity context, int pos,
			float scale, boolean IsBlured) {
		activity = context;

		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		return Fragment.instantiate(context, AwardsFragment.class.getName(), b);
	}

	public boolean showBg = false;

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
			}
		};
		int pos = this.getArguments().getInt("pos");
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.achivements,
				container, false);
		myRotation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.rotator);
		GiftsManager giftsManager = GiftsManager.getInstance();
		ArrayList<Gifts> gifts = giftsManager.getGiftsById(pos + 1);
		for (int i = 1; i < 7; i++) {
			int img_id = activity.getResources().getIdentifier("img_bg_" + i,
					"id", activity.getPackageName());
			ImageView img_sura_0 = (ImageView) l.findViewById(img_id);
			String image = "ach_" + (pos + 1) + "_" + i;
			if (gifts.get(i - 1).getStatus() == 0) {
				image = "ach_" + (pos + 1) + "_" + i + "_d";
				if (i == 6)
					image = "ach_disabled";

			}

			int id = activity.getResources().getIdentifier(image, "drawable",
					activity.getPackageName());
			try {
				img_sura_0.setImageDrawable(activity.getResources()
						.getDrawable(id));

			} catch (Exception e) {

			}
		}
		LinearLayout root = (LinearLayout) l.findViewById(R.id.root);
		float scale = this.getArguments().getFloat("scale");
		// root.setScaleBoth(scale);
		boolean isBlured = this.getArguments().getBoolean("IsBlured");

		return l;
	}

}
