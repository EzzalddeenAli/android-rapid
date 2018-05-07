package com.android.frameproj.library.widget;

//banner自定义  轮播view

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.frameproj.library.R;
import com.android.frameproj.library.util.ScreenUtil;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

public class AdvertView extends RelativeLayout {

	private com.daimajia.slider.library.SliderLayout mSliderLayout;
	private List<BaseSliderView> mListPages = new ArrayList<BaseSliderView>();
	private int mCurrentItem;
	private LinearLayout mIndicatorContainer;
	private List<ImageView> mListPoint = new ArrayList<ImageView>();
//	private List<String> mListTitle = new ArrayList<String>();
	private IPageScrollStateChangeListener mPageScrollStateChangeListener;

	public AdvertView(Context context) {
		super(context);
		init(context);
	}

	public AdvertView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.advert_layout, this);
		mSliderLayout = (com.daimajia.slider.library.SliderLayout) view.findViewById(R.id.ad_view);
		mIndicatorContainer = (LinearLayout) view.findViewById(R.id.ll_indicator);

		mSliderLayout.setOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
				if(mPageScrollStateChangeListener != null)
					mPageScrollStateChangeListener.onPageScrollStateChanged(state);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				int size = mListPoint.size();
				if(size == 0)
					position = 0;
				else
					position = position % size;
				for (int i = 0; i < size; i++) {
					if (i == position) {
						mListPoint.get(i).setImageResource(R.drawable.banner_indicator_selected);
					} else
						mListPoint.get(i).setImageResource(R.drawable.banner_indicator_normal);
				}
				mCurrentItem = position;
			}

		});
	}

	public void setAdvert(List<BaseSliderView> views) {
		mListPages.clear();
		mListPages.addAll(views);
//		mListTitle.clear();
		mListPoint.clear();
		mIndicatorContainer.removeAllViews();
//		mCurrentItem = mSliderLayout.getCurrentPosition();
		mSliderLayout.removeAllSliders();
		mCurrentItem = 0;
		int size = mListPages.size();
		for (int i = 0; i < size; i++) {
			if(size > 1){
				ImageView view = addIndicator();
				mListPoint.add(view);
				if (mCurrentItem == i) {
					view.setImageResource(R.drawable.banner_indicator_selected);
				} else
					view.setImageResource(R.drawable.banner_indicator_normal);
				mIndicatorContainer.addView(view);
			}
			mSliderLayout.addSlider(mListPages.get(i));
		}
		mSliderLayout.refresh();
//		mSliderLayout.setCurrentPosition(0);
		mSliderLayout.startAutoCycle();

	}


	public void startAutoScroll() {
		mSliderLayout.startAutoCycle();
	}

	public void stopAutoScroll() {
		mSliderLayout.stopAutoCycle();
	}

//	public void setIndicatorMarginBottom(int marginBottom){
//		LayoutParams params = (LayoutParams) mIndicatorContainer.getLayoutParams();
//		params.setMargins(0, 0, 0, SysUtil.dip2px(getContext(), marginBottom));
//		mIndicatorContainer.setLayoutParams(params);
//	}

	private ImageView addIndicator() {
		ImageView img = new ImageView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ScreenUtil.dipToPx(getContext(), 7), ScreenUtil.dipToPx(getContext(), 7));
		lp.setMargins(ScreenUtil.dipToPx(getContext(), 5), 0, ScreenUtil.dipToPx(getContext(), 5), 0);
		img.setLayoutParams(lp);
		return img;
	}

	public void setPageScrollStateChangeListener(IPageScrollStateChangeListener pageScrollStateChangeListener) {
		mPageScrollStateChangeListener = pageScrollStateChangeListener;
	}

	public interface IPageScrollStateChangeListener{
		void onPageScrollStateChanged(int state);
	}
}
