package com.lh.rapid.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lh.rapid.R;

public class SelectNumberView extends LinearLayout {

	private ImageView mPlus;
	private ImageView mMinus;
	private TextView mTvQuantity;
	private int mQuantity = 1;
	private int mMax = 99;
	private int mMin = 1;
	private ISelectCallback mSelectCB;
	private ISelectCallback2 mSelectCB2;
	private int index = -1;   //记录在ListView中的位置

	public SelectNumberView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SelectNumberView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SelectNumberView(Context context) {
		super(context);
		init(context);
	}

	public SelectNumberView(Context context, int quantity) {
		super(context);
		mQuantity = quantity;
		init(context);

	}

	/**
	 * 设置listview中位置
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.select_number, this);
		mPlus = (ImageView) view.findViewById(R.id.iv_plus);
		mMinus = (ImageView) view.findViewById(R.id.iv_minus);
		mTvQuantity = (TextView) view.findViewById(R.id.tv_quantity);
		mTvQuantity.setText(String.valueOf(mQuantity));

		if (mQuantity == mMin) {
			mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		}
		if (mQuantity == mMax) {
			mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		}

		mMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				minus();
			}
		});

		mPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				plus();
			}
		});
	}

	private void plus() {
		if (mQuantity < mMax) {
			mQuantity++;
			if (mQuantity == mMax){
				mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
			}
		} else {
			if(mSelectCB != null)
				mSelectCB.onMaxQuantity();
		}

		if (mQuantity > mMin) {
			mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		}

		mTvQuantity.setText(String.valueOf(mQuantity));
		if(mSelectCB != null)
			mSelectCB.onResult(index, mQuantity);
	}

	private void minus() {
		if (mQuantity > mMin) {
			mQuantity--;
			if (mQuantity == mMin) {
				mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
			}
		} else {
			if(mSelectCB != null)
				mSelectCB.onMinQuantity();
		}

		if (mQuantity < mMax) {
			mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		}
		mTvQuantity.setText(String.valueOf(mQuantity));
		if(mSelectCB != null)
			mSelectCB.onResult(index, mQuantity);
	}

	public void setMaxLimit(int max) {
		mMax = Math.min(max, 99);
	}

	public void setMinLimit(int min) {
		mMin = min;
	}

	public void setSelectCallback(ISelectCallback cb) {
		mSelectCB = cb;
	}
	public void setSelectCallback2(ISelectCallback2 cb) {
		mSelectCB2 = cb;
	}

	public int getQuantity(){
		return mQuantity;
	}

	public void setQuantity(int value){
		mQuantity = value;
		mTvQuantity.setText(String.valueOf(value));
		if (mQuantity == mMax){
			mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		} else {
			mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		}
		if (mQuantity == mMin) {
			mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		} else {
			mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		}
	}

	public void reset(int quantity) {
		mMinus.setClickable(true);
		mPlus.setClickable(true);
		mQuantity = quantity;
		mTvQuantity.setText(String.valueOf(mQuantity));

		if (mQuantity == mMin) {
			mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		}else{
			mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		}
		if (mQuantity == mMax) {
			mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		}else{
			mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		}
	}

	public void setDisable(){
		mMinus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_minus));
		mMinus.setClickable(false);
		mPlus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_add));
		mPlus.setClickable(false);
	}

	public void setHasNoProduct(){
		mTvQuantity.setText("0");
		mMinus.setEnabled(false);
		mPlus.setEnabled(false);
	}

	public interface ISelectCallback {
		void onResult(int index, int qualitity);
		void onMaxQuantity();
		void onMinQuantity();
	}

	public interface ISelectCallback2 {
		void onResult(int qualitity);

		void onMaxQuantity();

		void onMinQuantity();
	}

}
