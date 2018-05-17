package com.lh.rapid.ui.userinfo;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.util.TimeUtils;
import com.android.frameproj.library.util.ToastUtil;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.lh.rapid.R;
import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.bean.SexBean;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.resetpassword.ResetPasswordActivity;
import com.lh.rapid.ui.widget.MyActionBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lh on 2018/5/15.
 */

public class UserInfoActivity extends BaseActivity implements UserInfoContract.View {

    @Inject
    UserInfoPresenter mPresenter;
    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_card_num)
    TextView mTvCardNum;
    @BindView(R.id.tv_name_prefix)
    TextView mTvNamePrefix;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.rl_name)
    RelativeLayout mRlName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout mRlPhone;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout mRlSex;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @BindView(R.id.rl_email)
    RelativeLayout mRlEmail;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.rl_birthday)
    RelativeLayout mRlBirthday;
    private TimePickerView mTimePickerView;

    @Override
    public int initContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initInjector() {
        DaggerUserInfoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        mPresenter.loadDate();
        mActionbar.setTitle("会员信息");
        mActionbar.setRightText("修改密码");
        mActionbar.setRightTvClickLitener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                Intent intent = new Intent(UserInfoActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {
        // 初始化性别 1:男；0：女
        SexBean sexBean = new SexBean(1, "男");
        options1Items.add(sexBean);
        sexBean = new SexBean(0, "女");
        options1Items.add(sexBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onLoadDateCompleted(AccountInfoBean accountInfoBean) {
        mEtName.setText(accountInfoBean.getNickName());
        mTvPhone.setText(accountInfoBean.getPhone());
        mTvCardNum.setText(accountInfoBean.getCardId());
        mEtEmail.setText(accountInfoBean.getEmail());
        mTvBirthday.setText(accountInfoBean.getBirthday());
    }

    @Override
    public void accountInfoCompletedError() {
        finish();
    }

    @Override
    public void accountInfoCompletedSuccess(String string) {
        ToastUtil.showToast(string);
        finish();
    }

    @Override
    public void onBackPressedSupport() {
        isExit();
    }

    private void isExit() {
        mPresenter.accountInfoCompleted(mEtName.getText().toString().trim(),
                sex == -1 ? "" : sex + "",
                mEtEmail.getText().toString().trim(),
                mTvBirthday.getText().toString().trim());
    }

    private List<SexBean> options1Items = new ArrayList<>();
    private int sex = -1;

    @OnClick(R.id.rl_sex)
    public void mRlSex() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(UserInfoActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                SexBean sexBean = options1Items.get(options1);
                sex = sexBean.getSex();
                mTvSex.setText(sexBean.getDesc());
            }
        }).setTitleText("性别选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(options1Items);//三级选择器
        pvOptions.show();
    }

    @OnClick(R.id.rl_birthday)
    public void mRlBirthday() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(2000, 0, 1);
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2018, 12, 31);
        mTimePickerView = new TimePickerView.Builder(UserInfoActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mTvBirthday.setText(TimeUtils.getTime(date).substring(0, 10));
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
                        final Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
                        final TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTimePickerView.returnData(btnSubmit);
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTimePickerView.dismiss();
                            }
                        });
                        tvTitle.setText("生日选择");
                    }
                })
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        mTimePickerView.show();
    }

}