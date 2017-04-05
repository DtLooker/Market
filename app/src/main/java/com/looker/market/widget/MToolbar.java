package com.looker.market.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.looker.market.R;


/**
 * Created by looker on 2017/3/15.
 */

public class MToolbar extends Toolbar {

    private View mView;
    private EditText mSearchView;
    private TextView mTitle;
    private Button mRightButton;

    public MToolbar(Context context) {
        this(context, null);
    }

    //初始化的时候默认加载第二个构造函数
    public MToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //获取自定义属性需要通过第三个构造函数
    public MToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.MToolbar, defStyleAttr, 0);
            int count = array.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case R.styleable.MToolbar_isShowSearchView:
                        boolean isShowSearchView = array.getBoolean(R.styleable.MToolbar_isShowSearchView, false);
                        if (isShowSearchView) {
                            showSearchView();
                            hideTitleView();
                        }
                        break;
                    case R.styleable.MToolbar_rightButtonIcon:
                        Drawable rightButtonIcon = array.getDrawable(R.styleable.MToolbar_rightButtonIcon);
                        //操作
                        setRightButtonIcon(rightButtonIcon);
                        break;
                }
            }
            array.recycle();
        }
    }

    private void initView() {
        if (mView == null){
            mView = LayoutInflater.from(getContext()).inflate(R.layout.toolbar, null);

            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            addView(mView, lp);
        }
    }

    public Button getRightButton(){
        return mRightButton;
    }

    public void setRightButtonIcon(Drawable icon) {
        if (icon != null){
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void setRightButtonOnClickListener(OnClickListener listener){
        mRightButton.setOnClickListener(listener);
    }

    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTitle != null){
            mTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView(){
        if (mSearchView != null){
            mSearchView.setVisibility(VISIBLE);
        }
    }

    public void hideSearchView(){
        if (mSearchView != null){
            mSearchView.setVisibility(GONE);
        }
    }

    public void showTitleView() {
        if (mTitle != null){
            mTitle.setVisibility(VISIBLE);
        }
    }

    public void hideTitleView() {
        if (mTitle != null){
            mTitle.setVisibility(GONE);
        }
    }
}
