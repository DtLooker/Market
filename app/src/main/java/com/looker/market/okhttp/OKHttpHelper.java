package com.looker.market.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by looker on 2017/3/15.
 */

public class OKHttpHelper {

    private static OKHttpHelper helper;

    private OkHttpClient client;

    private Gson gson;

    private Handler handler;

    private OKHttpHelper(){
        gson = new Gson();

        handler = new Handler(Looper.getMainLooper());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(8000, TimeUnit.SECONDS);
        builder.writeTimeout(8000, TimeUnit.SECONDS);
        builder.connectTimeout(8000, TimeUnit.SECONDS);

        client = builder.build();
    }

    public static synchronized OKHttpHelper getInstance(){
        if (helper == null){
            helper = new OKHttpHelper();
        }
        return helper;
    }

    public void get(String url, BaseCallback callback){
        Request request = buildRequest(url, null, HttpMethodType.GET);
        doRequest(request, callback);
    }

    public void post(String url, Map<String, String> params, BaseCallback callback){

        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callback);
    }

    public void doRequest(Request request, final BaseCallback callback){

        callback.onResponseBefore(request);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call.request(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){

                    String strResult = response.body().string();
                    Log.d("TAG", " strResult = " +  strResult);
                    if (callback.mType == String.class){
                        callbackSuccess(callback, response, strResult);
                    }else {
                        //解析JSON经常出错，所以try..catch..下
                        try {
                            Object obj = gson.fromJson(strResult, callback.mType);

                            callbackSuccess(callback, response, obj);
                        }catch (JsonParseException e){
                            callbackError(callback, response, response.code(), e);
                        }
                    }

                }else {
                    callbackError(callback, response, response.code(), null);
                }


            }
        });
    }

    public Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType){
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (methodType == HttpMethodType.GET){
            builder.get();
        }else if (methodType == HttpMethodType.POST){

            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();

    }

    public RequestBody buildFormData(Map<String, String> params){

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }
    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final int code, final Exception e){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponseErroe(response, code, e);
            }
        });
    }

    enum HttpMethodType{
        GET,
        POST
    }
}
