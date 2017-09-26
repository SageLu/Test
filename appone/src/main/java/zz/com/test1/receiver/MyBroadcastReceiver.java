package zz.com.test1.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import zz.com.test1.MyApplication;
import zz.com.test1.utils.WordstoVoice;
import zz.com.test1.activity.JpushTestActivity;

/**
 * Created by lenovo on 2017/4/2.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消�?");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的通知");

            WordstoVoice wordstoVoice = new WordstoVoice();
            wordstoVoice.toVoice(MyApplication.getGlobalApplication());

            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了�?�知");

            openNotification(context,bundle);

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            //当前的url的�?�是由，极光推�?�的服务传�?�过来的，http://www.saiermeng.com
//          {
//              url:"http://www.saiermeng.com";
//          }
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("url");

        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        Intent mIntent = new Intent(context, JpushTestActivity.class);
        mIntent.putExtra("url", myValue);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }
}