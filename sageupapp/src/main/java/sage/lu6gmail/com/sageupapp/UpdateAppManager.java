package sage.lu6gmail.com.sageupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 类名: UpdateAppManager
 * 此类用途: ---
 *
 * @Author: GuXiao
 * @Date: 2017-07-08 14:37
 * @Email: sage.lu6@gmail.com
 * @FileName: sage.lu6gmail.com.sageupapp.UpdateAppManager.java
 */
public class UpdateAppManager {
    final static String INTENT_KEY = "update_dialog_values";
    private String TAG = getClass().getSimpleName().toString();
    private Activity mActivity;
    private HttpManager mHttpManager;
    private String mUpdateUrl;
    private String mTargetPath;

    private UpdateAppBean mUpdateApp;

    private UpdateAppManager(Buidler buidler) {
        mActivity = buidler.getmActivity();
        mHttpManager = buidler.getmHttpManager();
        mUpdateUrl = buidler.getmUpdateUrl();
        mTargetPath = buidler.getmTargetPath();
    }

    /**
     * 跳转到更新页面
     */
    public void showDialog() {
        if (TextUtils.isEmpty(mTargetPath)) {
            Log.e(TAG, "下载路径错误:" + mTargetPath);
            return;
        }
        if (mUpdateApp == null) {
            Log.e(TAG, "mUpdateApp:is--null");
            return;
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            Log.e(TAG, "启动......");

            Intent updateIntent = new Intent(mActivity, DialogActivity.class);
//            mUpdateApp.setTargetPath(mTargetPath);
//            mUpdateApp.setHttpManager(mHttpManager);
//            updateIntent.putExtra(INTENT_KEY, mUpdateApp);
            mActivity.startActivity(updateIntent);

        }
    }

    /**
     * 检测是否有新版本
     *
     * @param callback
     */
    public void checkNewApp(final UpdateCallback callback) {
        if (callback == null) {
            return;
        }
        callback.onBefore();
        if (DialogActivity.isShow || DownloadService.isRunning) {
            callback.onAfter();
            Toast.makeText(mActivity, "app正在更新", Toast.LENGTH_SHORT).show();
            return;
        }
        //下载
        mHttpManager.asyncGet(mUpdateUrl, new HttpManager.Callback() {
            @Override
            public void onResponse(String result) {
                callback.onAfter();
                if (result != null) {
                    processData(result, callback);
                }
            }

            @Override
            public void onError(String error) {
                callback.onAfter();
                callback.noNewApp();
            }
        });

    }

    private void processData(String result, UpdateCallback callback) {
        try {
            mUpdateApp = callback.parseJson(result);
            if (mUpdateApp.isUpdate()) {
                callback.hasNewApp(mUpdateApp, this);

            } else {
                callback.noNewApp();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
            callback.noNewApp();
        }
    }

    public static class Buidler {
        private Activity mActivity;//必须有
        private HttpManager mHttpManager;//必须有
        private String mUpdateUrl;//必须有
        private String mTargetPath; //设置app下载的地址

        public Activity getmActivity() {
            return mActivity;
        }

        public Buidler setmActivity(Activity mActivity) {
            this.mActivity = mActivity;
            return Buidler.this;
        }

        public HttpManager getmHttpManager() {
            return mHttpManager;
        }

        public Buidler setmHttpManager(HttpManager mHttpManager) {
            this.mHttpManager = mHttpManager;
            return Buidler.this;
        }

        public String getmUpdateUrl() {
            return mUpdateUrl;
        }

        public Buidler setmUpdateUrl(String mUpdateUrl) {
            this.mUpdateUrl = mUpdateUrl;
            return Buidler.this;
        }

        public String getmTargetPath() {
            return mTargetPath;
        }

        public Buidler setmTargetPath(String mTargetPath) {
            this.mTargetPath = mTargetPath;
            return Buidler.this;
        }

        public UpdateAppManager build() {
            //校验
            if (getmActivity() == null || getmHttpManager() == null || TextUtils.isEmpty(getmUpdateUrl())) {
                throw new NullPointerException("必要参数不能为空");
            }
            if (TextUtils.isEmpty(getmTargetPath())) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                setmTargetPath(path);
            }
            return new UpdateAppManager(Buidler.this);
        }

    }

}
