package com.isslam.kidsquran;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AwardsAdapter extends FragmentPagerAdapter implements
ViewPager.OnPageChangeListener {



	private boolean swipedLeft=false;
	public static int lastPage=0;
	private LinearLayout cur = null;

	private AwardsActivity context;
	private FragmentManager fm;
	private float scale;
	private boolean IsBlured;
	private static float minAlpha=0.6f;
	private static float maxAlpha=1f;
	private static float minDegree=60.0f;
	private int counter=0;

	public static float getMinDegree()
	{
		return minDegree;
	}
	public static float getMinAlpha()
	{
		return minAlpha;
	}
	public static float getMaxAlpha()
	{
		return maxAlpha;
	}
	
	public AwardsAdapter(AwardsActivity achievementsActivity, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = achievementsActivity;
	}

	@Override
	public Fragment getItem(int position) 
	{
		Log.d("position", String.valueOf(position));
		curFragment= AwardsFragment.newInstance(context, position, scale,IsBlured);
		cur = getRootView(position);
		return curFragment;
	}
	Fragment curFragment;

	@Override
	public int getCount()
	{		
		return 7;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) 
	{	
		
	}

	@Override
	public void onPageSelected(int position) {


		if(lastPage<=position)
		{
			swipedLeft=true;
		}
		else if(lastPage>position)
		{
			swipedLeft=false;
		}
		Log.e("--->",context+"");
		if(context!=null)
		((AwardsActivity)context).OnpageSelected(position,lastPage);
		
		lastPage=position;
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if(state==1)
		{
			Log.e("changed","changed");
			if(context!=null)
				((AwardsActivity)context).onPageChanged();
				
		}
		if(state==0)
		{
			if(context!=null)
				((AwardsActivity)context).OnpageRemains();
		}
		Log.e("state",state+"");
				
	}

	private SuraFragment getFragment(int position)
	{
		SuraFragment ly;
		try {
			ly = (SuraFragment) 
					fm.findFragmentByTag(this.getFragmentTag(position))
					;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return ly;
	}

	private LinearLayout getRootView(int position)
	{
		LinearLayout ly;
		try {
			ly = (LinearLayout) 
					fm.findFragmentByTag(this.getFragmentTag(position))
					.getView().findViewById(R.id.root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		if(ly!=null)
			return ly;
		return null;
	}

	private String getFragmentTag(int position)
	{
		return "android:switcher:" + context.pager.getId() + ":" + position;
	}
}
