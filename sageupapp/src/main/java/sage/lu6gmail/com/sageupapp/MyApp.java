package sage.lu6gmail.com.sageupapp;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 类名: MyApp
 * 此类用途: ---
 *
 * @Author: GuXiao
 * @Date: 2017-07-08 16:41
 * @Email: sage.lu6@gmail.com
 * @FileName: sage.lu6gmail.com.sageupapp.MyApp.java
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
