package com.looker.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.looker.market.adapter.HotWRecyclerAdapter;
import com.looker.market.adapter.decoration.DividerItemDecoration;
import com.looker.market.bean.Page;
import com.looker.market.bean.Wares;
import com.looker.market.util.PageUtil;
import com.looker.market.widget.MToolbar;

import java.util.List;

public class WareListActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, PageUtil.OnPageListener<Wares> {

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;

    private TextView summary;
    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private MToolbar mToolbar;
    private HotWRecyclerAdapter mAdapter;

    private int orderBy = 0;
    private long campaignId = 0;
    private TabLayout mTabLayout;
    private PageUtil pageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        getSupportActionBar().hide();

        campaignId = getIntent().getLongExtra(Constants.Campaign_ID, 0);

        initView();
        initToolbar();
        intTab();
        requestData();
    }

    private void initView() {
        summary = (TextView) findViewById(R.id.text_summary);
        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void initToolbar() {
        mToolbar = (MToolbar) findViewById(R.id.toolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WareListActivity.this.finish();
            }
        });
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.drawable.icon_grid_32));
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.setRightButtonOnClickListener(this);
    }

    private void intTab() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);

        mTabLayout.addOnTabSelectedListener(this);

    }

    private void requestData() {

        pageUtil = PageUtil.newBuilder().setUrl(Constants.API.WARES_CAMPAIN_LIST)
                .putParams("campaignId", campaignId)
                .putParams("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setCanLoadMore(true)
                .setListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());

        pageUtil.request();
    }

    @Override
    public void load(List<Wares> datas, int totalpage, int totalcount) {

        summary.setText("共有"+totalcount+"件商品");

        if (mAdapter == null){
            mAdapter = new HotWRecyclerAdapter(datas, this, R.layout.template_hot_wares);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(WareListActivity.this));
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(WareListActivity.this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }else {
            mAdapter.refreshData(datas);
        }
    }

    @Override
    public void loadMore(List<Wares> datas, int totalpage, int totalcount) {
       mAdapter.loadMoreData(datas);
    }

    @Override
    public void refresh(List<Wares> datas, int totalpage, int totalcount) {
        mAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }


    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();

        if (ACTION_LIST == action){
            mToolbar.setRightButtonIcon(getResources().getDrawable(R.drawable.icon_list_32));
            mToolbar.getRightButton().setTag(ACTION_GIRD);

            mAdapter.resetLayout(R.layout.template_grid_wares);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        }else if (ACTION_GIRD == action){

            mToolbar.setRightButtonIcon(getResources().getDrawable(R.drawable.icon_grid_32));
            mToolbar.getRightButton().setTag(ACTION_LIST);

            mAdapter.resetLayout(R.layout.template_hot_wares);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pageUtil.putParam("orderBy", orderBy);
        pageUtil.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
