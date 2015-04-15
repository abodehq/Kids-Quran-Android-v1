package com.isslam.kidsquran;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ScaleImageView;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.isslam.kidsquran.R.id;
import com.isslam.kidsquran.R.layout;

public class ViewPagerFragment extends Fragment {

	private static final String BUNDLE_ASSET = "asset";

	private String asset;

	public ViewPagerFragment() {
	}

	public ViewPagerFragment(String asset) {
		this.asset = asset;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(layout.view_pager_page, container,
				false);

		if (savedInstanceState != null) {
			if (asset == null && savedInstanceState.containsKey(BUNDLE_ASSET)) {
				asset = savedInstanceState.getString(BUNDLE_ASSET);
			}
		}
		if (asset != null) {
			ScaleImageView imageView = (ScaleImageView) rootView
					.findViewById(id.imageView);
			imageView.setImageAsset(asset);
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		View rootView = getView();
		if (rootView != null) {
			outState.putString(BUNDLE_ASSET, asset);
		}
	}

}
