package sage.lu6gmail.com.sageupapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名: UpdateCallback
 * 此类用途: ---
 *
 * @Author: GuXiao
 * @Date: 2017-07-08 15:40
 * @Email: sage.lu6@gmail.com
 * @FileName: sage.lu6gmail.com.sageupapp.UpdateCallback.java
 */
public abstract class UpdateCallback {

    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    protected UpdateAppBean parseJson(String json) {
        Log.e("json",json);
        UpdateAppBean updateAppBean = new UpdateAppBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            updateAppBean.setVersion(jsonObject.getString("version"));
            updateAppBean.setUrl(jsonObject.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return updateAppBean;
    }



    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    protected abstract void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager);

    /**
     * 网路请求之后
     */
    protected abstract void onAfter();

    /**
     * 没有新版本
     */
    protected abstract void noNewApp();

    /**
     * 网络请求之前
     */
    protected abstract void onBefore();

}
