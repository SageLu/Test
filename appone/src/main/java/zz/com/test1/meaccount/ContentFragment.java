package zz.com.test1.meaccount;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;

import zz.com.test1.R;

/**
 * Created by lenovo on 2017/4/3.
 */

public class ContentFragment extends BaseFragment {
    private LinearLayout back_ll;
    private RadioGroup rg_top;
    private ViewPager vp_Content;
    FragmentManager fm;
    /**
     * 装四个页面的集合
     */
    private ArrayList<BasePager> basePagers;


    public ContentFragment(FragmentManager fm){
        this.fm=fm;
    }
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_me_account, null);
        back_ll = (LinearLayout) view.findViewById(R.id.ll_back);
        rg_top = (RadioGroup) view.findViewById(R.id.rg_top);
        vp_Content = (ViewPager) view.findViewById(R.id.vp_Content);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //初始化四个页面，并且放入集合中
        basePagers = new ArrayList<>();

        basePagers.add(new YingShouPager(context,getChildFragmentManager()));//营收页面
        basePagers.add(new HuiYuanPager(context));//会员
        basePagers.add(new JiFenPager(context));//积分页面
        basePagers.add(new KaQuanPager(context));//卡券页面


        vp_Content.setAdapter(new ContentFragmentAdapter());

        //设置RadioGroup的选中状态改变的监听
        rg_top.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //监听某个页面被选中，初始对应的页面的数据
        vp_Content.addOnPageChangeListener(new MyOnPageChangeListener());

        rg_top.check(R.id.rb_yingshou);

        basePagers.get(0).initData();

    }
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //调用被选中的页面的initData方法
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_yingshou:
                    vp_Content.setCurrentItem(0);
                break;
                case R.id.rb_huiyuan:
                    vp_Content.setCurrentItem(1);
                break;
                case R.id.rb_jifen:
                    vp_Content.setCurrentItem(2);
                break;
                case R.id.rb_kaquan:
                    vp_Content.setCurrentItem(3);
                break;
            }

        }
    }

    class ContentFragmentAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);
            View rootView = basePager.rootView;
//            basePager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



}
