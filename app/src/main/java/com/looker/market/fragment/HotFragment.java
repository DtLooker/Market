package com.looker.market.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.looker.market.Constants;
import com.looker.market.R;
import com.looker.market.adapter.HotWRecyclerAdapter;
import com.looker.market.adapter.decoration.DividerItemDecoration;
import com.looker.market.bean.Page;
import com.looker.market.bean.Wares;
import com.looker.market.okhttp.LoadCallback;
import com.looker.market.okhttp.OKHttpHelper;

import java.util.List;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private List<Wares> mWaresList;
    private HotWRecyclerAdapter mAdapter;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private MaterialRefreshLayout mMaterialRefreshLayout;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_hot, container, false);
        }

        initRefresh(mView);

        initRequest();

        initRecycler();

        return mView;
    }

    private void initRefresh(View view) {

        mMaterialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        mMaterialRefreshLayout.setLoadMore(true);
        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currentPage < totalPage){
                    loadMore();
                }else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void loadMore() {
        currentPage = ++ currentPage;
        state = STATE_MORE;
        initRequest();
    }

    private void refreshData() {
        currentPage = 1;
        state = STATE_REFRESH;
        initRequest();
    }

    private void initRecycler() {

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
    }

    private void initRequest() {
        String url = Constants.API.WARES_HOT + "?curPage=" + currentPage + "&pageSize=" + pageSize;

        OKHttpHelper.getInstance().get(url, new LoadCallback<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                List<Wares> waresList = waresPage.getList();
                mWaresList = waresList;

                currentPage = waresPage.getCurrentPage();
                pageSize = waresPage.getPageSize();
                totalPage = waresPage.getTotalPage();

                showList();
            }

            @Override
            public void onResponseErroe(Response response, int code, Exception e) {

            }
        });
    }

    private void showList() {

       switch (state){
           case STATE_NORMAL:
               mAdapter = new HotWRecyclerAdapter(mWaresList, getContext(), R.layout.template_hot_wares);
               mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
               mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
               mRecyclerView.setAdapter(mAdapter);
               break;

           case STATE_REFRESH:
               mAdapter.clear();
               mAdapter.addData(mWaresList);
               mRecyclerView.scrollToPosition(0);
               mMaterialRefreshLayout.finishRefresh();
               break;

           case STATE_MORE:
               mAdapter.addData(mAdapter.getDatas().size(), mWaresList);
               mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
               mMaterialRefreshLayout.finishRefreshLoadMore();
               break;
       }

    }

}
