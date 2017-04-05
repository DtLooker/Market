package com.looker.market.util;


import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.looker.market.bean.Page;
import com.looker.market.okhttp.LoadCallback;
import com.looker.market.okhttp.OKHttpHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


/**
 * Created by looker on 2017/4/3.
 */

public class PageUtil {

    private static Builder builder;
    private OKHttpHelper httpHelper;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private PageUtil(){

        httpHelper = OKHttpHelper.getInstance();
        initRefresh();
    }

    public static Builder newBuilder(){
        builder = new Builder();
        return builder;
    }

    public void request(){
        requestData();
    }

    public void putParam(String key, Object value){
        builder.params.put(key, value);
    }

    private void initRefresh() {

        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.currentPage < builder.totalPage){
                    loadMore();
                }else {
                    Toast.makeText(builder.mContext, "无更多数据", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    private void requestData() {


        String url = buildUrl();

        httpHelper.get(url, new RequestCallback(builder.mContext));
    }

    private <T> void showList(List<T> datas, int totalpage, int totalcount) {

        if (datas == null || datas.size() <= 0){
            Toast.makeText(builder.mContext, "加载不到数据", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (state){
            case STATE_NORMAL:
                if (builder.listener != null){
                    builder.listener.load(datas, totalpage, totalcount);
                }
                break;

            case STATE_REFRESH:
                builder.mRefreshLayout.finishRefresh();
                if (builder.listener != null){
                    builder.listener.refresh(datas, totalpage, totalcount);
                }
                break;

            case STATE_MORE:
               builder.mRefreshLayout.finishRefreshLoadMore();
                if (builder.listener != null){
                    builder.listener.loadMore(datas, totalpage, totalcount);
                }
                break;
        }

    }

    private String buildUrl(){
        //String url = Constants.API.WARES_HOT + "?curPage=" + currentPage + "&pageSize=" + pageSize;
        return builder.url + "?" + buildUrlParams();
    }

    private String buildUrlParams(){
        HashMap<String, Object> map = builder.params;
        map.put("curPage", builder.currentPage);
        map.put("pageSize", builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")){
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private void loadMore() {
        state = STATE_MORE;
        builder.currentPage = ++ builder.currentPage;
        requestData();
    }

    private void refreshData() {
        state = STATE_REFRESH;
        builder.currentPage = 1;
        requestData();
    }

    public static class Builder{

        private String url;
        private HashMap<String, Object> params = new HashMap<>(5);
        private Context mContext;
        private boolean canLoadMore;
        private Type mType;

        private int currentPage = 1;
        private int pageSize = 10;
        private int totalPage = 1;
        private MaterialRefreshLayout mRefreshLayout;

        private OnPageListener listener;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder putParams(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public Builder setCanLoadMore(boolean canLoadMore) {
            this.canLoadMore = canLoadMore;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            mRefreshLayout = refreshLayout;
            return this;
        }

        public Builder setListener(OnPageListener listener) {
            this.listener = listener;
            return this;
        }

        public PageUtil build(Context context, Type type){

            this.mContext = context;
            this.mType = type;

            valid();
            return new PageUtil();
        }

        private void valid() {
            if(this.mContext==null)
                throw  new RuntimeException("Context can't be null");

            if(this.url==null || "".equals(this.url))
                throw  new RuntimeException("Url can't be  null");

            if(this.mRefreshLayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }

    }

    class RequestCallback<T> extends LoadCallback<Page<T>>{

        public RequestCallback(Context context) {
            super(context);

            super.mType = builder.mType;
        }

        @Override
        public void onSuccess(Response response, Page page) {

            builder.currentPage = page.getCurrentPage();
            builder.pageSize = page.getPageSize();
            builder.totalPage = page.getTotalPage();

            showList(page.getList(), page.getTotalPage(), page.getTotalCount());
        }

        @Override
        public void onResponseErroe(Response response, int code, Exception e) {
            Toast.makeText(builder.mContext, "加载数据失败", Toast.LENGTH_SHORT).show();

            if (state == STATE_REFRESH){
                builder.mRefreshLayout.finishRefresh();
            }else if (state == STATE_MORE){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }

    public interface OnPageListener<T>{

        void load(List<T> datas, int totalpage, int totalcount);
        void loadMore(List<T> datas, int totalpage, int totalcount);
        void refresh(List<T> datas, int totalpage, int totalcount);
    }
}
