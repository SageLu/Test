package com.retrofit.rx.network;

import com.retrofit.rx.utils.ToolsUtil;
import com.retrofit.rx.UfunApplication;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sunxx on 2016/6/20.
 */
public class NetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (ToolsUtil.isNetworkAvailable(UfunApplication.getContext())){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        }else{
            request=request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

        }

        Response response =   chain.proceed(request);
        if (ToolsUtil.isNetworkAvailable(UfunApplication.getContext())){
            int maxAge = 0 * 60;
            // 有网络时 设置缓存超时时间0个小时

            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        }else{
            // 无网络时，设置超时为一年
            int maxStale = 60 * 60 * 24 * 365;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
            return response;

    }
}
