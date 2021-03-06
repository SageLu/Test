package zz.com.test1.meaccount;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by lenovo on 2017/4/3.
 */

public class KaQuanPager extends BasePager {
    public KaQuanPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("sage","卡券数据被初始化了..");
        //1.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //2.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //3.绑定数据
        textView.setText("卡券内容");
    }
}
