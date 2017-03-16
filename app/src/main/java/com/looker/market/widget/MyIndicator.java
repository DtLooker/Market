package com.looker.market.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.looker.market.R;

/**
 * Created by looker on 2017/3/15.
 */

public class MyIndicator extends View {

    //indicator圆的个数
    private int mNum = 4;
    //indicator半径
    private int mRadius = 8;
    //前画笔的颜色
    private int mForeColor;
    //后画笔的颜色
    private int mBackColor;
    private Paint mForePaint;
    private Paint mBackPaint;

    //indicator圆每个圆心的距离
    private int mGapWidth;
    //圆滑动时候的偏移量
    private float mOffset;

    public MyIndicator(Context context) {
        this(context, null);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //indicator圆每个圆心的距离
        mGapWidth = 3 * mRadius;

        //初始化画笔
        initPaint();

        if (attrs != null) {
            TypedArray array = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.MyIndicator, defStyleAttr, 0);

            int count = array.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case R.styleable.MyIndicator_indicator_num:
                        mNum = array.getInteger(R.styleable.MyIndicator_indicator_num, mNum);
                        break;
                    case R.styleable.MyIndicator_indicator_radius:
                        mRadius = array.getInteger(R.styleable.MyIndicator_indicator_radius, mRadius);
                        break;
                    case R.styleable.MyIndicator_forePaint_color:
                        mForeColor = array.getInteger(R.styleable.MyIndicator_forePaint_color,
                                getResources().getColor(R.color.darkorange));
                        break;
                    case R.styleable.MyIndicator_backPaint_color:
                        mBackColor = array.getInteger(R.styleable.MyIndicator_backPaint_color,
                                getResources().getColor(R.color.gray));
                        break;
                }
            }
            array.recycle();

        }
    }

    private void initPaint() {
        mForePaint = new Paint();
        //抗锯齿
        mForePaint.setAntiAlias(true);
        //实心
        mForePaint.setStyle(Paint.Style.FILL);
        mForePaint.setColor(mForeColor);
        //设置画笔粗细
        mForePaint.setStrokeWidth(2);

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackPaint.setColor(mBackColor);
        mBackPaint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(2*mRadius*mNum + (mNum-1)*mRadius + 10, 2*mRadius + 10 );
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(2*mRadius*mNum + (mNum-1)*mRadius + 10, heightSize);
        }else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 2*mRadius + 10);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mNum; i++) {
            canvas.drawCircle(10 + mGapWidth * i, 10, mRadius, mBackPaint);
        }
        canvas.drawCircle(10 + mOffset, 10, mRadius, mForePaint);
        //重绘 ,不调用此方法，前景不会变化
        invalidate();
    }

    public void setOffset(int position, float offset) {
        mOffset = (position + offset) * 3 * mRadius;
    }
}
