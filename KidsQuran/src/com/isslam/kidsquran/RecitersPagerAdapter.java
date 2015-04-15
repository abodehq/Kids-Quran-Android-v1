package com.isslam.kidsquran;

import com.isslam.kidsquran.controllers.AudioListManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class RecitersPagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {

	private boolean swipedLeft = false;
	public static int lastPage = 1;
	private MyLinearLayout cur = null;

	private RecitersActivity context;
	private FragmentManager fm;
	private float scale;
	private boolean IsBlured;
	private static float minAlpha = 0.6f;
	private static float maxAlpha = 1f;
	private static float minDegree = 60.0f;
	private int counter = 0;

	public static float getMinDegree() {
		return minDegree;
	}

	public static float getMinAlpha() {
		return minAlpha;
	}

	public static float getMaxAlpha() {
		return maxAlpha;
	}

	AudioListManager audioListManager;

	public RecitersPagerAdapter(RecitersActivity recitersActivity,
			FragmentManager fm) {
		super(fm);
		audioListManager = AudioListManager.getInstance();
		this.fm = fm;
		this.context = recitersActivity;
	}

	@Override
	public Fragment getItem(int position) {

		// make the first pager bigger than others
		if (position == MainActivity.FIRST_PAGE)
			scale = MainActivity.BIG_SCALE;
		else {
			scale = MainActivity.SMALL_SCALE;
			IsBlured = true;

		}

		Log.d("position", String.valueOf(position));
		curFragment = ReciterFragment.newInstance(context, position, scale,
				IsBlured);
		cur = getRootView(position);

		// ImageView contentbg=(ImageView)cur.findViewById(R.id.contentbg);

		return curFragment;
	}

	Fragment curFragment;

	@Override
	public int getCount() {
		return audioListManager.getSelectedReciters().size();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

		if (positionOffset >= 0f && positionOffset <= 1f) {
			positionOffset = positionOffset * positionOffset;
			cur = getRootView(position);

			/*
			 * To animate it properly we must understand swipe direction this
			 * code adjusts the rotation according to direction.
			 */

		}

	}

	@Override
	public void onPageSelected(int position) {

		/*
		 * to get finger swipe direction
		 */

		Log.e("selected", "selected");

		if (lastPage <= position) {
			swipedLeft = true;
		} else if (lastPage > position) {
			swipedLeft = false;
		}
		Log.e("--->", context + "");
		if (context != null)
			((RecitersActivity) context).OnpageSelected(position, lastPage);
		if (getFragment(lastPage) != null)
			getFragment(lastPage).RotateBgImage(false);
		if (getFragment(position) != null)
			getFragment(position).RotateBgImage(true);
		Log.e("positions", position + " : " + lastPage);
		lastPage = position;

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == 1) {
			Log.e("changed", "changed");
			if (context != null)
				((RecitersActivity) context).onPageChanged();

		}
		if (state == 0) {
			if (context != null)
				((RecitersActivity) context).OnpageRemains();
		}
		Log.e("state", state + "");

	}

	private ReciterFragment getFragment(int position) {
		ReciterFragment ly;
		try {
			ly = (ReciterFragment) fm.findFragmentByTag(this
					.getFragmentTag(position));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return ly;
	}

	private MyLinearLayout getRootView(int position) {
		MyLinearLayout ly;
		try {
			ly = (MyLinearLayout) fm
					.findFragmentByTag(this.getFragmentTag(position)).getView()
					.findViewById(R.id.root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		if (ly != null)
			return ly;
		return null;
	}

	private String getFragmentTag(int position) {
		return "android:switcher:" + context.pager.getId() + ":" + position;
	}
}
