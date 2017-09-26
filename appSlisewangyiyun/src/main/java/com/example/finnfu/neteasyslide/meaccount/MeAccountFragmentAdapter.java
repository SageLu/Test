package com.example.finnfu.neteasyslide.meaccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
/**
 * Created by finnfu on 16/5/5.
 */
public class MeAccountFragmentAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragmentArrayList;
    private Fragment mainFragment1;
    private Fragment mainFragment2;
    private Fragment mainFragment3;
    private Fragment mainFragment4;

    public MeAccountFragmentAdapter(FragmentManager fm) {
        super(fm);
        mainFragment1 = new MeAccountFragment1();
        mainFragment2 = new MeAccountFragment2();
        mainFragment3 = new MeAccountFragment3();
        mainFragment4 = new MeAccountFragment4();
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(mainFragment1);
        fragmentArrayList.add(mainFragment2);
        fragmentArrayList.add(mainFragment3);
        fragmentArrayList.add(mainFragment4);
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
            case 3:
                return fragmentArrayList.get(3);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
