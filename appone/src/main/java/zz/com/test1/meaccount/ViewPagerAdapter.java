package zz.com.test1.meaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/25 17:10
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<MyFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<MyFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * 根据位置返回对应的Fragment
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * 得到页面的标题
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
