package com.example.finnfu.neteasyslide.meaccount;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finnfu.neteasyslide.R;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by finnfu on 16/5/5.
 */
public class MeAccountFragment2 extends Fragment {
    private ViewPager mainViewPager2;
    private MeAccountFragmentAdapter_2 adapter;
    private TabPageIndicator mIndicator2 ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainfragment2, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewPager2 = (ViewPager) getActivity().findViewById(R.id.mainFragment_pager2);
        mainViewPager2.setCurrentItem(0);
        adapter = new MeAccountFragmentAdapter_2(getChildFragmentManager());
        mainViewPager2.setAdapter(adapter);
        mIndicator2 =  (TabPageIndicator) getActivity().findViewById(R.id.id_indicator2);
        mIndicator2.setViewPager(mainViewPager2,0);
        mIndicator2.setOnPageChangeListener(new MyOnPageChangeListen());
    }



    private class MyOnPageChangeListen implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
