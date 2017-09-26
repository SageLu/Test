package com.retrofit.rx;

import android.app.Application;
import android.content.Context;

import com.retrofit.rx.utils.CrashHandler;


/**
 * Created by jyh on 2016/5/28.
 */
public class UfunApplication extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
     /*   初始化日志采集器*/
        CrashHandler.getInstance().init(mContext);
    }
    /**全局上下文*/
    public static Context getContext(){
        return  mContext;
    }
}
