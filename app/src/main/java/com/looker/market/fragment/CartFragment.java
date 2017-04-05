package com.looker.market.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.looker.market.MainActivity;
import com.looker.market.R;
import com.looker.market.adapter.CartAdapter;
import com.looker.market.bean.ShoppingCart;
import com.looker.market.util.CartProvider;
import com.looker.market.widget.MToolbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener{

    private static final int ACTION_EDIT = 1;
    private static final int ACTION_COMPLETE = 2;

    private View mView;
    private CartProvider mCartProvider;
    private RecyclerView mRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTotal;
    private Button mBtnDelete;
    private Button mBtnOrder;

    private MToolbar mToolbar;


    private static final String TAG = "CartFragment";
    private CartAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      if (mView == null){
            mView = inflater.inflate(R.layout.fragment_cart, container, false);
        }

        mCartProvider = new CartProvider(getContext());

        initView(mView);
        showData();
        return mView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mTotal = (TextView) view.findViewById(R.id.txt_total);
        mBtnOrder = (Button) view.findViewById(R.id.btn_order);
        mBtnDelete = (Button) view.findViewById(R.id.btn_del);

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.delCart();
            }
        });

    }

    public void showData(){

        List<ShoppingCart> carts = mCartProvider.getAll();
        mAdapter = new CartAdapter(carts, getContext(), R.layout.template_cart, mCheckBox, mTotal);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void refreshData(){

        mAdapter.clear();
        List<ShoppingCart> carts = mCartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            MainActivity activity = (MainActivity) context;
            mToolbar = (MToolbar) activity.findViewById(R.id.toolbar);
            changeToolbar();
        }
    }

    public void changeToolbar(){
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.tab_cart);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    @Override
    public void onClick(View view) {

       int action = (int) view.getTag();
        switch (action){
            case ACTION_EDIT:
                showDelControl();
                break;
            case ACTION_COMPLETE:
                hideDelControl();
        }

    }

    private void showDelControl() {
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);
        mToolbar.getRightButton().setText("完成");
        mTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDelete.setVisibility(View.VISIBLE);

        mCheckBox.setChecked(false);
        mAdapter.checkedAll_None(false);

    }

    public void hideDelControl(){
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mToolbar.getRightButton().setText("编辑");
        mTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDelete.setVisibility(View.GONE);

        mCheckBox.setChecked(true);
        mAdapter.checkedAll_None(true);

        mAdapter.showTotalPrice();
    }
}
