package com.looker.market.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.looker.market.R;

/**
 * Created by looker on 2016/12/3.
 */
public class MIndicator extends View {

    /**前画笔**/
    private Paint mForePaint;
    /**后画笔**/
    private Paint mBgpaint;
    /**偏移量**/
    private float mOffset;

    /**自定义**/
    /**indicator数量**/
    private int mNumber = 4;
    /**indicator半径**/
    private int mRadius = 8;
    /**indicator前画笔颜色**/
    private int mForeColor;
    /**indicator后画笔颜色**/
    private int mBgColor;
    /**indicator之间间距**/
    private int mGapWidth;



    public MIndicator(Context context) {
        super(context);
    }

    public MIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyIndicator);

        mNumber =typedArray.getInteger(R.styleable.MyIndicator_indicator_num, mNumber);
        mRadius = typedArray.getInteger(R.styleable.MyIndicator_indicator_radius, mRadius);

        mForeColor = typedArray
                .getColor(R.styleable.MyIndicator_forePaint_color, getResources().getColor(R.color.orangered));
        mBgColor = typedArray.getColor(R.styleable.MyIndicator_backPaint_color, getResources().getColor(R.color.gray));

        mGapWidth = 3 * mRadius;
        /**初始化画笔**/
        initPaint();
    }

    public void setOffset(int position, float offSet){
        mOffset = (position + offSet) * 3 * mRadius;
    }

    private void initPaint() {
        mForePaint = new Paint();
        //设置抗锯齿
        mForePaint.setAntiAlias(true);
        //设置样式  实心
        mForePaint.setStyle(Paint.Style.FILL);
        mForePaint.setColor(mForeColor);
        //设置画笔粗细
        mForePaint.setStrokeWidth(2);

        mBgpaint = new Paint();
        mBgpaint.setAntiAlias(true);
        mBgpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgpaint.setColor(mBgColor);
        mBgpaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mNumber; i++) {
            canvas.drawCircle(10 + i * mGapWidth, 10, mRadius, mBgpaint);
        }
        canvas.drawCircle(10 + mOffset, 10, mRadius, mForePaint);
        //重画 不调用此方法前景不会移动
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //因为源码中的wrap_content就是match_parent， 所以要重写，实现对wrap_content的支持
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension((mNumber - 1) * 3 * mRadius + 2 * mRadius + 10, 2 * mRadius + 10);
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension((mNumber - 1) * 3 * mRadius + 2 * mRadius + 10, heightSpecSize);
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, 2 * mRadius + 10);
        }
    }
}
