package com.looker.market.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by looker on 2017/3/4.
 */

public class DeviderItemDecotation extends RecyclerView.ItemDecoration{

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
        int orientation = getOrientation(parent);
        if (orientation == LinearLayoutManager.VERTICAL){
            outRect.top = 10;
            outRect.left = 5;
            outRect.right = 5;
        }else if (orientation == LinearLayoutManager.HORIZONTAL){
            outRect.left = 5;
        }
    }

    private int getOrientation(RecyclerView parent){
        if (parent.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        }else throw new IllegalArgumentException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }
}
