package com.lh.rapid.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.frameproj.library.util.InputUtil;
import com.android.frameproj.library.util.SysUtil;
import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;

/**
 * Created by yuyong on 2015/6/9.
 * 集合了搜索的Tilte图标以及背景
 */
public class MyActionBar extends RelativeLayout implements View.OnClickListener {

    private View mActionBarView;
    private View mSearchFull;
    private View mSearchPart;
//    private View mSearch;
    private View mMessage;
    private View mBg;
    private RelativeLayout mIvBack;
    private ImageView mIvRight;
    private ImageView mIvSearch;
    private ImageView mIvSearchClearPart;
    private ImageView mIvSearchFull;
    private TextView mTvRight;
    private TextView mTvTitle;
    private TextView mTvNotice;
    private EditText mEtSearchPart;
//    private EditText mEtSearch;
    private ImageView mBack;

    private IActionBarClickListener mBackClickListener;
    private IActionBarClickListener mRightTvClickLitener;
    private IActionBarClickListener mRightIvClickLitener;
    private IActionBarClickListener mSearchFullClickLitener;

    private ISearchListener mSearchListener;
    private PopupWindow popupWin;
    private View line;
    private Context mContext;

    public MyActionBar(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mActionBarView = LayoutInflater.from(getContext()).inflate(R.layout.kitchen_actionbar, this);
        mBack = (ImageView) mActionBarView.findViewById(R.id.iv_actionbar_back1);
        mBg = mActionBarView.findViewById(R.id.rl_actionbar_bg);//背景id
        mSearchFull = mActionBarView.findViewById(R.id.rl_search_full);//搜索图标
        mSearchPart = mActionBarView.findViewById(R.id.rl_search_part);//包含搜索图标，删除，editText
        mMessage = mActionBarView.findViewById(R.id.rl_message_right);//运用在最后的个人用户中的title，带信封
        mIvBack = (RelativeLayout) mActionBarView.findViewById(R.id.iv_actionbar_back);//back图标
        mIvRight = (ImageView) mActionBarView.findViewById(R.id.iv_actionbar_right);//删除的图标（垃圾桶图标）
        mIvSearch = (ImageView) mActionBarView.findViewById(R.id.iv_actionbar_search_part);//查找的图标
        mIvSearchClearPart = (ImageView) mActionBarView.findViewById(R.id.iv_actionbar_search_clear);//X（删除EditText内容）
        mIvSearchFull = (ImageView) mActionBarView.findViewById(R.id.iv_actionbar_search_full);//搜索图标
        mTvRight = (TextView) mActionBarView.findViewById(R.id.tv_actionbar_right);//title
        mTvTitle = (TextView) mActionBarView.findViewById(R.id.tv_actionbar_title);//title
        mTvNotice = (TextView) mActionBarView.findViewById(R.id.tv_actionbar_notice);//应该是购物车提示
        mEtSearchPart = (EditText) mActionBarView.findViewById(R.id.et_actionbar_search);//搜索框

//        mSearch = mActionBarView.findViewById(R.id.rl_search);//应该是搜索的背景
//        mEtSearch = (EditText) mActionBarView.findViewById(R.id.et_search);//搜索框
//        mIvSearchClear = (ImageView) mActionBarView.findViewById(R.id.iv_search_clear);//清除EditText（X）

        line = mActionBarView.findViewById(R.id.line);

        mTvTitle.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mSearchFull.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        mIvSearchClearPart.setOnClickListener(this);
//        mIvSearchClear.setOnClickListener(this);
        mEtSearchPart.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (mSearchListener != null) {
                        InputUtil.hideSoftInput((BaseActivity)mContext);
                        mSearchListener.onSearch(mEtSearchPart.getText().toString());
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });

        mEtSearchPart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEtSearchPart.getText().toString().equals("")) {
                    mIvSearchClearPart.setVisibility(GONE);
                } else {
                    mIvSearchClearPart.setVisibility(VISIBLE);
                }
            }
        });

//        mEtSearch.setOnEditorActionListener(new OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
//                    if (mSearchListener != null) {
//                        SoftInputUtil.hideShow(mEtSearch);
//                        mSearchListener.onSearch(mEtSearch.getText().toString());
//                        return true;
//                    }
//                    return false;
//                }
//                return false;
//            }
//        });
    }

    public void line(int visible) {
        line.setVisibility(visible);
    }

    public void setBgResource(int resource) {
        mBg.setBackgroundColor(Color.TRANSPARENT);
        mBg.setBackgroundResource(resource);
    }

    public void setPartSeacheText(String text) {
        if (mEtSearchPart != null)
            mEtSearchPart.setText(text);
    }

    public void setBgColor(int color) {
        mBg.setBackgroundColor(color);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setTitleColor(int color) {
        mTvTitle.setTextColor(color);
    }

    public void setTitle(int resid) {
        mTvTitle.setText(resid);
    }

    public void setLeftVisible(int visible) {
        mIvBack.setVisibility(visible);
    }

    public void setBackImage(int id) {
        mBack.setImageResource(id);
    }

    public void setRight(String text, int id) {
        mTvRight.setVisibility(VISIBLE);
        mIvRight.setVisibility(VISIBLE);
        mTvRight.setText(text);
        mIvRight.setImageResource(id);
    }

    public void setRightText(String text) {
        mIvRight.setVisibility(View.GONE);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(text);
    }

    public void setRightText(int resid) {
        mIvRight.setVisibility(View.GONE);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(resid);
    }

    public void setRightImage(int resid) {
        mIvRight.setVisibility(View.VISIBLE);
        mTvRight.setVisibility(View.GONE);
        mIvRight.setImageResource(resid);
    }

    public void setSearchFull(int bgResid, int searchResid) {
        mIvBack.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.GONE);
        mSearchFull.setVisibility(View.VISIBLE);
        mSearchFull.setBackgroundResource(bgResid);
        if (searchResid != -1)
            mIvSearchFull.setImageResource(searchResid);
    }

    public void setSearchPart() {
        mTvTitle.setVisibility(View.GONE);
        mSearchPart.setVisibility(View.VISIBLE);
    }

    public void setPopupWin(OnClickListener listener) {
        showPopup(listener);
    }


    public void clearSearchKeyPart() {
        mEtSearchPart.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_actionbar_back:
                if (mBackClickListener != null)
                    mBackClickListener.onActionBarClicked();
                break;
            case R.id.iv_actionbar_right:
                if (mRightIvClickLitener != null)
                    mRightIvClickLitener.onActionBarClicked();
                break;
            case R.id.tv_actionbar_right:
                if (mRightTvClickLitener != null)
                    mRightTvClickLitener.onActionBarClicked();
                break;
            case R.id.rl_search_full:
                if (mSearchFullClickLitener != null)
                    mSearchFullClickLitener.onActionBarClicked();
                break;
            case R.id.iv_actionbar_search_clear:
                mEtSearchPart.setText("");
                break;
        }
    }

    public void setBackClickListener(IActionBarClickListener backClickListener) {
        this.mBackClickListener = backClickListener;
    }

    public void setRightTvClickLitener(IActionBarClickListener rightTvClickLitener) {
        this.mRightTvClickLitener = rightTvClickLitener;
    }

    public void setRightIvClickLitener(IActionBarClickListener rightIvClickLitener) {
        this.mRightIvClickLitener = rightIvClickLitener;
    }

    public void setSearchFullClickLitener(IActionBarClickListener searchFullClickLitener) {
        this.mSearchFullClickLitener = searchFullClickLitener;
    }

    public void setSearchListener(ISearchListener searchListener) {
        this.mSearchListener = searchListener;
    }

    public interface IActionBarClickListener {
        void onActionBarClicked();
    }

    public interface ISearchListener {
        void onSearch(String key);
    }

    private void showPopup(OnClickListener listener) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_popupwindow_choose_broadcast, null);
        TextView all = (TextView) view.findViewById(R.id.tv_all_broadcast);
        TextView cy = (TextView) view.findViewById(R.id.tv_cy_broadcast);
        TextView xq = (TextView) view.findViewById(R.id.tv_xq_broadcast);
        all.setOnClickListener(listener);
        cy.setOnClickListener(listener);
        xq.setOnClickListener(listener);
        popupWin = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWin.setTouchable(true);
        popupWin.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWin.setBackgroundDrawable(new BitmapDrawable());
        popupWin.setOutsideTouchable(true);
        popupWin.showAsDropDown(mTvRight, SysUtil.dip2px(getContext(), -10), 0);
    }

    public void dissmisPopup() {
        if (popupWin != null)
            popupWin.dismiss();
    }
}
