package com.isslam.kidsquran;

import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.model.Suras;
import com.isslam.kidsquran.utils.GlobalConfig;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SuraFragment extends Fragment {
	static SurasActivity activity = null;
	Handler handler = new Handler();
	Runnable r;

	public static Fragment newInstance(SurasActivity context, int pos,
			float scale, boolean IsBlured) {
		activity = context;

		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putBoolean("IsBlured", IsBlured);
		return Fragment.instantiate(context, SuraFragment.class.getName(), b);
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
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.ly_suras,
				container, false);
		myRotation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.rotator);
		Log.e("---", "---");
		for (int i = 0; i < 8; i++) {
			int img_id = activity.getResources().getIdentifier("img_sura_" + i,
					"id", activity.getPackageName());

			ImageView img_sura_0 = (ImageView) l.findViewById(img_id);
			int sura_id = (i + (78 + ((4 - pos) * 8)));
			Log.e("sura_id -:> ", sura_id + "");
			AudioListManager audioListManager = AudioListManager.getInstance();

			if (sura_id < 115) {
				Suras sura = audioListManager.getSuraBySuraId(sura_id);
				int heart_id = activity.getResources().getIdentifier(
						"ic_heart_" + i, "id", activity.getPackageName());
				ImageView img_heart = (ImageView) l.findViewById(heart_id);

				if (sura.getMemorize() == 1)
					img_heart.setImageResource(R.drawable.ic_heart);
				else
					img_heart.setImageResource(R.drawable.ic_heart_disable);

				int record_id = activity.getResources().getIdentifier(
						"ic_mic_" + i, "id", activity.getPackageName());
				ImageView img_record = (ImageView) l.findViewById(record_id);

				if (sura.getRecords() == 1)
					img_record.setImageResource(R.drawable.ic_mic);
				else
					img_record.setImageResource(R.drawable.ic_mic_disable);

				int stars_id = activity.getResources().getIdentifier(
						"ic_star_" + i, "id", activity.getPackageName());
				ImageView img_stars = (ImageView) l.findViewById(stars_id);

				if (sura.getStars() == 1)
					img_stars.setImageResource(R.drawable.ic_star);
				else
					img_stars.setImageResource(R.drawable.ic_star_disable);
			}

			String image = "s_" + sura_id;
			if (sura_id > 114) {
				int ly_id = activity.getResources().getIdentifier(
						"ly_sura_" + i, "id", activity.getPackageName());
				RelativeLayout ly_sura = (RelativeLayout) l.findViewById(ly_id);
				ly_sura.setVisibility(View.GONE);
			}
			int id = activity.getResources().getIdentifier(image, "drawable",
					activity.getPackageName());
			try {
				img_sura_0.setImageDrawable(activity.getResources()
						.getDrawable(id));

			} catch (Exception e) {

			}
			int img_bg_id = activity.getResources().getIdentifier(
					"img_bg_" + i, "id", activity.getPackageName());
			ImageView img_bg = (ImageView) l.findViewById(img_bg_id);
			img_bg.setTag(sura_id);
			img_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("sura", "sura : " + v.getTag());
					AudioListManager.suraId = (Integer) v.getTag();
					// AudioListManager
					// audioListManager=AudioListManager.getInstance();
					// Suras sura =
					// audioListManager.getSuraBySuraId(AudioListManager.suraId);
					// Log.e("---------------->",sura.getStars()+"");
					// audioListManager.setSelectedSura(sura);
					Intent i = new Intent();
					i.setClass(activity, ViewPagerActivity.class);
					startActivity(i);
					activity.finish();
				}
			});

		}
		Log.e("---", "---");
		LinearLayout root = (LinearLayout) l.findViewById(R.id.root);

		return l;
	}

}
