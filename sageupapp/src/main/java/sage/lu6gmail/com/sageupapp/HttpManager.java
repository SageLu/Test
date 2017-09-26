package sage.lu6gmail.com.sageupapp;

import android.support.annotation.NonNull;

/**
 * 类名: HttpManager
 * 此类用途: ---
 *
 * @Author: GuXiao
 * @Date: 2017-07-08 14:51
 * @Email: sage.lu6@gmail.com
 * @FileName: sage.lu6gmail.com.sageupapp.HttpManager.java
 */
public interface HttpManager {
    /**
     * 异步get
     */
    void asyncGet(@NonNull String url, @NonNull Callback callBack);

    /**
     * 网络请求回调
     */
    interface Callback {
        void onResponse(String result);

        void onError(String error);
    }
}
