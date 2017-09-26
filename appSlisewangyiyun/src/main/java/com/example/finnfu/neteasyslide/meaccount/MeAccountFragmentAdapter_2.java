package com.example.finnfu.neteasyslide.meaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by finnfu on 16/5/5.
 */
public class MeAccountFragmentAdapter_2 extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragmentArrayList;
    private Fragment mainFragment1;
    private Fragment mainFragment2;
    private Fragment mainFragment3;
    private static final String[] TITLE = new String[] { "NB", "SNAKE", "OMG"};
    public MeAccountFragmentAdapter_2(FragmentManager fm) {
        super(fm);
        mainFragment1 = new MeAccount2_Fragment1();
        mainFragment2 = new MeAccount_Fragment2();
        mainFragment3 = new MeAccount2_Fragment3();
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(mainFragment1);
        fragmentArrayList.add(mainFragment2);
        fragmentArrayList.add(mainFragment3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragmentArrayList.get(0);
            case 1:
                return fragmentArrayList.get(1);
            case 2:
                return fragmentArrayList.get(2);
            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
