package sage.lu6gmail.com.sageupapp;

import android.support.annotation.NonNull;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 类名: UpdateAppHttpUtil
 * 此类用途: ---
 *
 * @Author: GuXiao
 * @Date: 2017-07-08 16:43
 * @Email: sage.lu6@gmail.com
 * @FileName: sage.lu6gmail.com.sageupapp.UpdateAppHttpUtil.java
 */
public class UpdateAppHttpUtil implements HttpManager {
    @Override
    public void asyncGet(@NonNull String url, @NonNull final Callback callBack) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response);
                    }
                });
    }
}
