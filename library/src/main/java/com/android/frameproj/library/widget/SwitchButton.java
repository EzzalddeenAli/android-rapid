package com.android.frameproj.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.android.frameproj.library.R;

public class SwitchButton extends View {

    //选中时背景色
    private int selectColor;

    //未选中时背景色
    private int normalColor;

    //选择按钮颜色
    private int buttonColor;

    //选择按钮padding
    private int buttonPadding;

    //滑动动画时间
    private int switchRate;

    //是否选中
    private boolean isSelect = false;

    //是否滑动中
    private boolean isScroll = false;

    //是否有动画
    private boolean isAnim = false;

    private Paint paint;
    private RectF rectF;

    //重绘次数
    private int paintTimes = 0;

    private OnSwitchChangeListener onSwitchChangeListener;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySwitchButton, defStyle, 0);
        selectColor = typedArray.getColor(R.styleable.MySwitchButton_selectColor, Color.RED);
        normalColor = typedArray.getColor(R.styleable.MySwitchButton_normalColor, Color.GRAY);
        buttonColor = typedArray.getColor(R.styleable.MySwitchButton_buttonColor, Color.WHITE);
        buttonPadding = typedArray.getDimensionPixelSize(R.styleable.MySwitchButton_buttonPadding, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        switchRate = typedArray.getInteger(R.styleable.MySwitchButton_switchRate, 10);
        typedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //选择按钮半径
        float buttonRadius = height - buttonPadding * 2 >> 1;
        //动画要移动总距离
        float animDistance = width - buttonRadius * 2 - buttonPadding * 2;
        //每次移动距离
        float animEach = animDistance / switchRate;

        rectF = new RectF(0, 0, width, height);
        if (isSelect) {
            //移动过后再改变背景色
            if (paintTimes < switchRate && isAnim) {
                paint.setColor(normalColor);
                canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
            } else {
                paint.setColor(selectColor);
                canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
            }
            paint.setColor(buttonColor);
            if (isAnim) {
                canvas.drawCircle(buttonRadius + buttonPadding + animEach * paintTimes, buttonRadius + buttonPadding, buttonRadius, paint);
            } else {
                canvas.drawCircle(width - buttonRadius - buttonPadding, buttonRadius + buttonPadding, buttonRadius, paint);
            }
        } else {
            if (paintTimes < switchRate && isAnim) {
                paint.setColor(selectColor);
                canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
            } else {
                paint.setColor(normalColor);
                canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
            }
            paint.setColor(buttonColor);
            if (isAnim) {
                canvas.drawCircle(width - buttonRadius - buttonPadding - animEach * paintTimes, buttonRadius + buttonPadding, buttonRadius, paint);
            } else {
                canvas.drawCircle(buttonRadius + buttonPadding, buttonRadius + buttonPadding, buttonRadius, paint);
            }
        }

        //移动动画
        if (paintTimes < switchRate && isAnim) {
            paintTimes++;
            invalidate();
        } else {
            paintTimes = 0;
            isAnim = false;
            isScroll = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (!isScroll) {
                    isSelect = !isSelect;
                    isAnim = true;
                    isScroll = true;
                    postInvalidate();
                    if (onSwitchChangeListener != null) {
                        onSwitchChangeListener.onChange(isSelect);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return true;
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    public boolean isChecked() {
        return isSelect;
    }

    public void setCheck(boolean select) {
        isSelect = select;
        postInvalidate();
    }

    public interface OnSwitchChangeListener {
        void onChange(boolean isChecked);
    }
}