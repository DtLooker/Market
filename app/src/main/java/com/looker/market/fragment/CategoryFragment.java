package com.looker.market.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.looker.market.Constants;
import com.looker.market.R;
import com.looker.market.adapter.BaseRecyclerAdapter;
import com.looker.market.adapter.CategoryAdapter;
import com.looker.market.adapter.MPagerAdapter;
import com.looker.market.adapter.WCategoryAdapter;
import com.looker.market.adapter.decoration.DividerGridItemDecoration;
import com.looker.market.adapter.decoration.DividerItemDecoration;
import com.looker.market.bean.Banner;
import com.looker.market.bean.Category;
import com.looker.market.bean.Page;
import com.looker.market.bean.Wares;
import com.looker.market.listener.MPagerListener;
import com.looker.market.okhttp.LoadCallback;
import com.looker.market.okhttp.OKHttpHelper;
import com.looker.market.widget.MyIndicator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private View mView;
    private OKHttpHelper mHelper = OKHttpHelper.getInstance();
    private RecyclerView mRecyclerLeft;

    private ViewPager mViewPager;
    private List<View> mViewList;

    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long categoryId = 0;
    private RecyclerView mRecyclerRight;

    private MaterialRefreshLayout materialRefreshLayout;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private WCategoryAdapter mWCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_category, container, false);
        }
        initView(mView);

        initRefresh();
        requestCategoryData();
        requestBannerData();

        return mView;
    }

    private void initRefresh() {

       materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currentPage < totalPage){
                    loadMoreDate();
                }else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });

    }

    private void loadMoreDate() {
        currentPage = ++currentPage;
        state = STATE_MORE;
        requestWaresData(categoryId);
    }

    private void refreshData() {
        currentPage = 1;
        state = STATE_REFRESH;
        requestWaresData(categoryId);
    }

    private void initView(View view) {
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        mRecyclerLeft = (RecyclerView) view.findViewById(R.id.recycler_left);

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        mRecyclerRight = (RecyclerView) view.findViewById(R.id.recycler_right);
    }

    private void requestCategoryData() {

        mHelper.get(Constants.API.CATEGORY_LIST, new LoadCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if (categories != null && categories.size() > 0){
                    categoryId = categories.get(0).getId();
                    requestWaresData(categoryId);
                }
            }

            @Override
            public void onResponseErroe(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(final List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(categories, getContext(), R.layout.template_single_text);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                currentPage = 1;
                state = STATE_NORMAL;

                Category category = categories.get(position);
                categoryId = category.getId();
                requestWaresData(categoryId);
            }
        });


        mRecyclerLeft.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerLeft.setAdapter(adapter);
        mRecyclerLeft.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void requestBannerData() {

        mHelper.get(Constants.API.BANNER, new LoadCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showBannerData(banners);
            }

            @Override
            public void onResponseErroe(Response response, int code, Exception e) {

            }
        });
    }

    private void showBannerData(List<Banner> banners) {
        mViewList = new ArrayList<>();

        for (Banner banner : banners) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.template_banner, null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.temp_banner_iv);
            Glide.with(getContext()).load(banner.getImgUrl()).into(imageView);
            mViewList.add(inflate);
        }
        mViewPager.setAdapter(new MPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new MPagerListener(new MyIndicator(getContext())));
    }

    private void requestWaresData(long categoryId) {

        String url = Constants.API.WARES_LIST + "?curPage=" + currentPage + "&pageSize=" + pageSize + "&categoryId=" + categoryId;

        mHelper.get(url, new LoadCallback<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currentPage = waresPage.getCurrentPage();
                pageSize = waresPage.getPageSize();
                totalPage = waresPage.getTotalPage();

                showWarsData(waresPage.getList());

            }

            @Override
            public void onResponseErroe(Response response, int code, Exception e) {

            }
        });
    }

    private void showWarsData(List<Wares> wares) {

        switch (state){
            case STATE_NORMAL:
                mWCategoryAdapter = new WCategoryAdapter(wares, getContext(), R.layout.template_grid_wares);

                mRecyclerRight.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mRecyclerRight.addItemDecoration(new DividerGridItemDecoration(getContext()));
                mRecyclerRight.setAdapter(mWCategoryAdapter);
                break;

            case STATE_REFRESH:
                mWCategoryAdapter.clear();
                mWCategoryAdapter.addData(wares);

                mRecyclerRight.scrollToPosition(0);
                materialRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:
                mWCategoryAdapter.addData(mWCategoryAdapter.getDatas().size(), wares);
                mRecyclerRight.scrollToPosition(mWCategoryAdapter.getDatas().size());
                materialRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }


}
