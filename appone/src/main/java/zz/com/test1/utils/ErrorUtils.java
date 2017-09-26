package zz.com.test1.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import zz.com.test1.bean.BaseResult;


/**
 * Created by loo on 16-7-19.
 */
public class ErrorUtils {
    public static boolean hasError(BaseResult result, Context context) {
        if (result == null) {
            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (result.getSuccess() > 0) {
            return false;
        } else {
            if (TextUtils.isEmpty(result.getErrMsg())) {
                Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, result.getErrMsg(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public static boolean hasError(BaseResult result) {
        if (result == null) {
            return true;
        }
        if (result.getSuccess() > 0) {
            return false;
        }
        return true;
    }
}
