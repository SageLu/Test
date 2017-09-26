package sage.lu6gmail.com.sageupapp;

import java.io.Serializable;

/**
 * 类名: UpdateAppBean
 * 此类用途: ---
 *
 * @Author: GuXiao
 * @Date: 2017-07-08 14:39
 * @Email: sage.lu6@gmail.com
 * @FileName: sage.lu6gmail.com.sageupapp.UpdateAppBean.java
 */
public class UpdateAppBean implements Serializable {

    /**
     * version : 2.4.2.1022
     * url : http://api.b.lbl.aduer.com/app/syt.apk
     */

    private String version;
    private String url;
    private HttpManager httpManager;
    private String targetPath;

    public boolean isUpdate() {
        return true;//暂时写为有更新的包，以后动态改变
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }


    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
