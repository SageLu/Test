package zz.com.test1;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by lenovo on 2017/4/1.
 */

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化全局上下文对象
        mContext = this;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58e0a32a");
    }

    // 获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
