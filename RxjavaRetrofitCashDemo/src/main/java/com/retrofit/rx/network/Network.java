package com.retrofit.rx.network;

import android.util.Log;

import com.retrofit.rx.UfunApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sunxx on 2016/6/20.
 */
public class Network {
    private static Demo mDemo;

    private static OkHttpClient mOkHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static Demo getDemo() {
        initOkhttp();
        if (mDemo == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl("http://gank.io/api/")
//                    .baseUrl("http://192.168.1.30:8888/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            mDemo = retrofit.create(Demo.class);
        }
        return mDemo;
    }

    private static void initOkhttp() {
        if (mOkHttpClient == null) {
            synchronized (Network.class) {
                if (mOkHttpClient == null) {
                    File mFile = new File(UfunApplication.getContext().getCacheDir().getAbsolutePath(), "chache");
                    String str=mFile.getAbsolutePath().toString();
                    Log.e("Caches",str);
                    Cache mCache = new Cache(mFile, 1024 * 1024 * 200);
                    NetworkInterceptor mNetworkInterceptor = new NetworkInterceptor();
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(mCache)
                            .addInterceptor(mNetworkInterceptor)
                            .addNetworkInterceptor(mNetworkInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }
}
