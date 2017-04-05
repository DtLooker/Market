package com.looker.market.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looker.market.R;

/**
 * Created by looker on 2017/3/24.
 */

public class NumAddSubView extends LinearLayout implements View.OnClickListener {

    private static final int DEFAULT_MAX = 1000;

    private int value;
    private int maxValue = DEFAULT_MAX;
    private int minValue;

    private OnButtonCliclListener mButtonCliclListener;
    private LayoutInflater mInflater;
    private Button mAddBtn;
    private Button mSubBtn;
    private TextView mTextCount;

    public void setButtonCliclListener(OnButtonCliclListener buttonCliclListener) {
        mButtonCliclListener = buttonCliclListener;
    }

    public NumAddSubView(Context context) {
        this(context, null);
    }

    public NumAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumAddSubView, defStyleAttr, 0);
        int count = typedArray.getIndexCount();

        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);

            switch (attr){

                case R.styleable.NumAddSubView_value:
                    value = typedArray.getInteger(R.styleable.NumAddSubView_value, 0);
                    setValue(value);
                    break;

                case R.styleable.NumAddSubView_maxValue:
                   maxValue = typedArray.getInteger(R.styleable.NumAddSubView_maxValue, 0);
                    setMaxValue(maxValue);
                    break;

                case R.styleable.NumAddSubView_minValue:
                    minValue = typedArray.getInteger(R.styleable.NumAddSubView_minValue, 0);
                    setMinValue(minValue);
                    break;

                case R.styleable.NumAddSubView_addBtn_bg:
                    Drawable addBtnBackground = typedArray.getDrawable(R.styleable.NumAddSubView_addBtn_bg);
                    setAddBtnBackground(addBtnBackground);
                    break;

                case R.styleable.NumAddSubView_subBtn_bg:
                    Drawable subBtnBackground = typedArray.getDrawable(R.styleable.NumAddSubView_subBtn_bg);
                    setSubBtnBackground(subBtnBackground);
                    break;

                case R.styleable.NumAddSubView_textView_bg:
                    Drawable textViewBackground = typedArray.getDrawable(R.styleable.NumAddSubView_textView_bg);
                    setTextViewBackground(textViewBackground);
                    break;

            }
        }
        typedArray.recycle();
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.number_add_sub, this, true);

        mAddBtn = (Button) view.findViewById(R.id.btn_add);
        mSubBtn = (Button) view.findViewById(R.id.btn_sub);
        mTextCount = (TextView) view.findViewById(R.id.txt_num);

        mAddBtn.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
    }

    public int getValue() {

        String val = mTextCount.getText().toString().trim();
        if (!TextUtils.isEmpty(val)){
            this.value = Integer.parseInt(val);
        }
        return value;
    }

    public void setValue(int value) {
        mTextCount.setText(value + "");
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_add:
                addNum();
                if (mButtonCliclListener != null){
                    mButtonCliclListener.onAddClick(view, value);
                }
                break;
            case R.id.btn_sub:
                subNum();
                if (mButtonCliclListener != null){
                    mButtonCliclListener.onSubClick(view, value);
                }
                break;
        }
    }

    public void addNum(){
        getValue();

        if (value < maxValue){
            value = value + 1;
        }
        mTextCount.setText(value + "");
    }

    public void subNum(){
        getValue();

        if (value > minValue){
            value = value - 1;
        }
        mTextCount.setText(value + "");
    }

    private void setTextViewBackground(int drawableId) {
       setTextViewBackground(getResources().getDrawable(drawableId));
    }

    private void setTextViewBackground(Drawable drawable) {
        mTextCount.setBackground(drawable);
    }

    private void setSubBtnBackground(Drawable drawable) {
        mAddBtn.setBackground(drawable);
    }

    private void setAddBtnBackground(Drawable drawable) {
        mSubBtn.setBackground(drawable);
    }

    public interface OnButtonCliclListener{
        void onAddClick(View view, int value);
        void onSubClick(View view, int value);
    }

}
