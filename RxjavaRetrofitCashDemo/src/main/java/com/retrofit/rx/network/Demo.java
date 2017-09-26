package com.retrofit.rx.network;

import com.retrofit.rx.bean.baseBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sunxx on 2016/6/20.
 */
public interface Demo {
    @GET("data/福利/10/{page}")
    Observable<baseBean> getBase(@Path("page") int page);
    @GET("http://192.168.1.30:8888/api/home")
    Observable<Object> getBase2();

}
