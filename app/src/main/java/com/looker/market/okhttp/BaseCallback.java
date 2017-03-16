package com.looker.market.okhttp;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by looker on 2017/3/15.
 */

public abstract class BaseCallback<T> {

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class){
            throw new RuntimeException("Miss type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public BaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onResponseBefore(Request request);

    public abstract void onFailure(Request request, Exception e);

    public abstract void onSuccess(Response response, T t);

    public abstract void onResponseErroe(Response response, int code, Exception e);


}
