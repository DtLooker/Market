package com.looker.market.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.looker.market.Constants;
import com.looker.market.R;
import com.looker.market.adapter.HotWarsAdapter;
import com.looker.market.bean.Page;
import com.looker.market.bean.Wares;
import com.looker.market.okhttp.LoadCallback;
import com.looker.market.okhttp.OKHttpHelper;
import com.looker.market.widget.DeviderItemDecotation;

import java.util.List;

import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private List<Wares> mWaresList;
    private HotWarsAdapter mAdapter;

    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_hot, container, false);
        }

        initRecycler();

        return mView;
    }

    private void initRecycler() {

        initRequest();

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

    }

    private void initRequest() {
        String url = Constants.API.WARES_HOT + "?curPage=" + currentPage +"&pageSize=" + pageSize;

        OKHttpHelper.getInstance().get(url, new LoadCallback<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                List<Wares> waresList = waresPage.getList();
                mWaresList = waresList;

                currentPage = waresPage.getCurrentPage();
                pageSize = waresPage.getPageSize();

                showList();
            }

            @Override
            public void onResponseErroe(Response response, int code, Exception e) {

            }
        });
    }

    private void showList() {
        mAdapter = new HotWarsAdapter(mWaresList, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DeviderItemDecotation());
    }

}
