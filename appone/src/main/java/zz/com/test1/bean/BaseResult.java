package zz.com.test1.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by loo on 16-7-18.
 */
public class BaseResult {
    @JSONField(name = "Success")
    private int success;
    @JSONField(name = "ErrMsg")
    private String errMsg;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
