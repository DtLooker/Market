package com.looker.market.listener;

import android.support.v4.view.ViewPager;

import com.looker.market.widget.MyIndicator;

/**
 * Created by looker on 2017/3/15.
 */

public class MPagerListener implements ViewPager.OnPageChangeListener {

    private MyIndicator mIndicator;

    public MPagerListener(MyIndicator indicator) {
        mIndicator = indicator;

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.setOffset(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
