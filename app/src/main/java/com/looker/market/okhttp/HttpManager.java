package com.looker.market.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by looker on 2017/3/15.
 */

public class HttpManager {

    private static HttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private HttpManager(){
        //初始化OKHttp
        initOkHttp();

        mHandler = new Handler(Looper.getMainLooper());

        mGson = new Gson();
    }
    /***
     * 单列
     *
     * @return
     */
    public static synchronized HttpManager getInstance(){
        if (mInstance == null){
            mInstance = new HttpManager();
        }
        return mInstance;
    }
    /***
     * 初始化okhttp
     */
    private void initOkHttp() {
        mOkHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(8000, TimeUnit.SECONDS)
                .connectTimeout(8000, TimeUnit.SECONDS)
                .build();
    }

    public void request(SimpleHttpClient client, final BaseCallback callback){

        if (callback == null){
            throw new NullPointerException("callback is null");
        }

        mOkHttpClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendOnFailureMessage(callback, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    String result = response.body().string();
                    if (callback.mType == null || callback.mType == String.class){
                        sendOnSuccessMessage(callback, result);
                    }else {
                        sendOnSuccessMessage(callback, mGson.fromJson(result, callback.mType));
                    }

                }else {
                    sendOnErrorMessage(callback, call, response.code());
                }
            }
        });
    }
    /***
     * 解析失败
     *
     * @param callback
     * @param call
     * @param e
     */
    private void sendOnFailureMessage(final BaseCallback callback, final Call call, final IOException e){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(call, e);
            }
        });
    }
    /***
     * 解析错误
     *
     * @param callback
     * @param call
     * @param code
     */
    private void sendOnErrorMessage(final BaseCallback callback, Call call, final int code){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(code);
            }
        });
    }
    /***
     * 解析成功
     *
     * @param callback
     * @param object
     */
    private void sendOnSuccessMessage(final BaseCallback callback, final Object object){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(object);
            }
        });
    }

}
