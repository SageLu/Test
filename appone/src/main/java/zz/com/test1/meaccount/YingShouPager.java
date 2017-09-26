package zz.com.test1.meaccount;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import zz.com.test1.R;

/**
 * Created by lenovo on 2017/4/3.
 */

public class YingShouPager extends BasePager {
    private ChildViewPager vp_Yingshou;
    private TabLayout tabLayout;
    ArrayList<MyFragment> fragments;
    FragmentManager fm;

    ViewPagerAdapter adapter;
    public YingShouPager(Context context,FragmentManager fm) {
        super(context);
        this.fm=fm;
    }

    @Override
    public void initData() {
        super.initData();

        Log.e("sage","营收数据被初始化了..");
        //1.联网请求，得到数据，创建视图
        View view = View.inflate(context, R.layout.yingshou_viewpager,null);
        vp_Yingshou= (ChildViewPager) view.findViewById(R.id.vp_Yingshou);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        //2.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(view);
        //3.绑定数据
        //初始化数据
        fragments = new ArrayList<>();
        fragments.add(new MyFragment("昨日","内容1"));
        fragments.add(new MyFragment("本月","内容2"));
        fragments.add(new MyFragment("上月","内容3"));
        //设置ViewPager的适配器
        adapter = new ViewPagerAdapter(fm, fragments);
        vp_Yingshou.setAdapter(adapter);
        //关联ViewPager
        tabLayout.setupWithViewPager(vp_Yingshou);
        //设置固定的
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}
