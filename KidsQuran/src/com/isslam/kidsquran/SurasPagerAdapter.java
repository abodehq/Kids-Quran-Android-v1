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

public class SurasPagerAdapter extends FragmentPagerAdapter implements
ViewPager.OnPageChangeListener {



	private boolean swipedLeft=false;
	public static int lastPage=0;
	private LinearLayout cur = null;

	private SurasActivity context;
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
	
	public SurasPagerAdapter(SurasActivity surasActivity, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = surasActivity;
	}

	@Override
	public Fragment getItem(int position) 
	{



		

		Log.d("position", String.valueOf(position));
		curFragment= SuraFragment.newInstance(context, position, scale,IsBlured);
		//cur = getRootView(position);
		
		//ImageView contentbg=(ImageView)cur.findViewById(R.id.contentbg);
		
	
		return curFragment;
	}
	Fragment curFragment;

	@Override
	public int getCount()
	{		
		return 5;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) 
	{	
		
		if (positionOffset >= 0f && positionOffset <= 1f)
		{
			positionOffset=positionOffset*positionOffset;
			//cur = getRootView(position);
			

			
			

			

			
			/*To animate it properly we must understand swipe direction
			 * this code adjusts the rotation according to direction.
			 */
			
		}
		
	}

	@Override
	public void onPageSelected(int position) {

/*
 * to get finger swipe direction
 */
	
			Log.e("selected","selected");
		
		
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
		((SurasActivity)context).OnpageSelected(position,lastPage);
		if(getFragment(lastPage)!=null)
		//getFragment(lastPage).RotateBgImage(false);
		if(getFragment(position)!=null)
		//getFragment(position).RotateBgImage(true);
		Log.e("positions",position+" : "+lastPage);
		lastPage=position;
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if(state==1)
		{
			Log.e("changed","changed");
			if(context!=null)
				((SurasActivity)context).onPageChanged();
				
		}
		if(state==0)
		{
			if(context!=null)
				((SurasActivity)context).OnpageRemains();
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
