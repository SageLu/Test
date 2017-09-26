package zz.com.test1.meaccount;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2017/4/3.
 */

public class ChildViewPager extends ViewPager {


    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}