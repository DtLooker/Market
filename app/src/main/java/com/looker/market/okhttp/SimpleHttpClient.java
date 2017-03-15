package com.looker.market.okhttp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by looker on 2017/3/15.
 */

public class SimpleHttpClient {

    private Builder mBuilder;

    private SimpleHttpClient(Builder builder){
        mBuilder = builder;
    }


    public void enqueue(BaseCallback callback){
        HttpManager.getInstance().request(this, callback);
    }

    public Request buildRequest(){
        Request.Builder builder = new Request.Builder();

        //get方式
        if (mBuilder.method == "GET"){
            builder.url(buildGetRequestParam());
            builder.get();

            //post方式
        }else if (mBuilder.method == "POST"){
            builder.post(buildRequestBody());
            builder.url(mBuilder.url);
        }
        return builder.build();
    }
    /***
     * 获取get请求参数
     *
     * @return
     */
    private String buildGetRequestParam(){
        if (mBuilder.mParams.size() <= 0){
            return mBuilder.url;
        }
        //拼接
        Uri.Builder builder = Uri.parse(mBuilder.url).buildUpon();
        for (RequestParam param : mBuilder.mParams) {
            builder.appendQueryParameter(param.getKey(),
                    param.getValue() ==null ? "":param.getValue().toString());
        }
        String url = builder.build().toString();
        return url;
    }

    /***
     * 获取request请求body
     *
     * @return
     */
    private RequestBody buildRequestBody(){

        //json格式
        if (mBuilder.isJson){
            JSONObject jsonObject = new JSONObject();
            for (RequestParam param : mBuilder.mParams) {
                try {
                    jsonObject.put(param.getKey(), param.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String json = jsonObject.toString();

            Log.d("SimpleRequest","request json="+json);
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        }

        //form表单
        FormBody.Builder builder = new FormBody.Builder();
        for (RequestParam param : mBuilder.mParams) {
            builder.add(param.getKey(), param.getValue() ==null ? "":param.getValue().toString());
        }
        return builder.build();
    }

    public static Builder newBuilder(){

        return new Builder();
    }

    /***************Builder 类******************/
    public static class Builder{

        private String url;
        private String method;
        private boolean isJson;
        private List<RequestParam> mParams;

        private Builder(){
            //默认为get方式
            method = "GET";
        }

        public SimpleHttpClient build(){

            return new SimpleHttpClient(this);
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder get(){

            method = "GET";
            return this;
        }

        /***
         * form 表单形式
         * @return
         */
        public Builder post(){

            method = "POST";
            return this;
        }

        /***
         * json 格式
         * @return
         */
        public Builder json(){
            isJson = true;
            return post();
        }

        public Builder addParam(String key, Object value){
            if (mParams != null){
                mParams = new ArrayList<>();
            }

            mParams.add(new RequestParam(key, value));

            return this;
        }
    }



}
