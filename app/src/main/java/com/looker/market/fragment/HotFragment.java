package com.looker.market.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.looker.market.Constants;
import com.looker.market.R;
import com.looker.market.adapter.HotWRecyclerAdapter;
import com.looker.market.adapter.decoration.DividerItemDecoration;
import com.looker.market.bean.Page;
import com.looker.market.bean.Wares;
import com.looker.market.util.PageUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private HotWRecyclerAdapter mAdapter;

    private MaterialRefreshLayout mMaterialRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_hot, container, false);
        }

        mMaterialRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        PageUtil pageUtil = PageUtil.newBuilder().setUrl(Constants.API.WARES_HOT).setCanLoadMore(true)
                .setListener(new PageUtil.OnPageListener() {
                    @Override
                    public void load(List datas, int totalpage, int totalcount) {
                        mAdapter = new HotWRecyclerAdapter(datas, getContext(), R.layout.template_hot_wares);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void loadMore(List datas, int totalpage, int totalcount) {
                        mAdapter.clear();
                        mAdapter.addData(datas);
                        mRecyclerView.scrollToPosition(0);
                        mMaterialRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void refresh(List datas, int totalpage, int totalcount) {
                        mAdapter.addData(mAdapter.getDatas().size(), datas);
                        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                        mMaterialRefreshLayout.finishRefreshLoadMore();
                    }
                })
                .setPageSize(20)
                .setRefreshLayout(mMaterialRefreshLayout)
                .build(getContext(), new TypeToken<Page<Wares>>() {
                }.getType());

        pageUtil.request();

        return mView;
    }
}
