package com.looker.market.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.looker.market.Constants;
import com.looker.market.R;
import com.looker.market.adapter.MPagerAdapter;
import com.looker.market.bean.Banner;
import com.looker.market.listener.MPagerListener;
import com.looker.market.okhttp.BaseCallback;
import com.looker.market.okhttp.OKHttpHelper;
import com.looker.market.widget.MyIndicator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View mView;

    /***
     * banner
     ***/
    private ViewPager mBannerPager;
    private MyIndicator mBannerIndicator;
    private List<Banner> mBanners;
    private List<View> mViewList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }

        initView(mView);
        requestBanner();
        return mView;
    }

    private void initView(View view) {
        /***初始化banner*/
        mBannerPager = (ViewPager) view.findViewById(R.id.indicator_pager);
        mBannerIndicator = (MyIndicator) view.findViewById(R.id.indicator);
    }

    private void requestBanner() {

        OKHttpHelper.getInstance().get(Constants.API.BANNER, new BaseCallback<List<Banner>>() {
            @Override
            public void onResponseBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners = banners;

                for (Banner banner : mBanners) {
                    Log.i("TAG", "banner.getImgUrl(): " + banner.getImgUrl());
                    Log.i("TAG", "banner.getName(): " + banner.getName());
                    Log.i("TAG", "banner.getDescription(): " + banner.getDescription());
                }
                initBanner();
//                initSlider();
            }

            @Override
            public void onResponseErroe(Response response, int code, Exception e) {

            }
        });

    }

    private void initBanner() {
        mViewList = new ArrayList<>();

        for (Banner banner : mBanners) {
            Log.i("TAG", "banner.getImgUrl(): " + banner.getImgUrl());
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.template_banner, null);
            ImageView bannerItem = (ImageView) inflate.findViewById(R.id.temp_banner_iv);
            Glide.with(getContext()).load(banner.getImgUrl()).into(bannerItem);
            mViewList.add(inflate);
        }
        mBannerPager.setAdapter(new MPagerAdapter(mViewList));
        mBannerPager.addOnPageChangeListener(new MPagerListener(mBannerIndicator));

    }


}
