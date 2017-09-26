package com.example.finnfu.neteasyslide.meaccount;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.finnfu.neteasyslide.R;

public class MeAccountActivity extends AppCompatActivity {

    //声明相关变量
    private Toolbar toolbar;
//    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton btn_back;
    private ImageButton toolBarBtn1;
    private ImageButton toolBarBtn2;
    private ImageButton toolBarBtn3;
    private ImageButton toolBarBtn4;
//    private ListView lvLeftMenu;
//    private String[] lvs = {"签到","我的消息", "积分商城", "付费音乐包", "在线听歌免流量","听歌识曲","主题换肤","夜间模式","定时停止播放","音乐闹钟","我的音乐云盘"};
//    private ArrayAdapter arrayAdapter;

    private ViewPager mainViewPager;
    private MeAccountFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaccount);

        findViews(); //获取控件
//        toolbar.setTitle("");//设置Toolbar标题
//        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
//        };
//        mDrawerToggle.syncState();
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
//        lvLeftMenu.setAdapter(arrayAdapter);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
//        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        toolBarBtn1 = (ImageButton) findViewById(R.id.toolbar_btn1);
        toolBarBtn2 = (ImageButton) findViewById(R.id.toolbar_btn2);
        toolBarBtn3 = (ImageButton) findViewById(R.id.toolbar_btn3);
        toolBarBtn4 = (ImageButton) findViewById(R.id.toolbar_btn4);
        toolBarBtn1.setOnClickListener(onClickListener);
        toolBarBtn2.setOnClickListener(onClickListener);
        toolBarBtn3.setOnClickListener(onClickListener);
        toolBarBtn4.setOnClickListener(onClickListener);

        mainViewPager = (ViewPager) findViewById(R.id.mainPager);
        adapter = new MeAccountFragmentAdapter(this.getSupportFragmentManager());
        mainViewPager.setAdapter(adapter);
        mainViewPager.setCurrentItem(0);
        updateToolBarUi(0);
        mainViewPager.setOnPageChangeListener(new MyOnPageChangeListen());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.toolbar_btn1:
                    updateToolBarUi(0);
                    mainViewPager.setCurrentItem(0);
                    break;
                case R.id.toolbar_btn2:
                    updateToolBarUi(1);
                    mainViewPager.setCurrentItem(1);
                    break;
                case R.id.toolbar_btn3:
                    updateToolBarUi(2);
                    mainViewPager.setCurrentItem(2);
                    break;
                case R.id.toolbar_btn4:
                    updateToolBarUi(3);
                    mainViewPager.setCurrentItem(3);
                    break;
                default:
                    break;
            }
        }
    };

    private class MyOnPageChangeListen implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            updateToolBarUi(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void updateToolBarUi(int index){
        switch (index){
            case 0:
                toolBarBtn1.setBackgroundResource(R.drawable.toolbar_discover_selected);
                toolBarBtn2.setBackgroundResource(R.drawable.toolbar_music_normal);
                toolBarBtn3.setBackgroundResource(R.drawable.toolbar_friends_normal);
                toolBarBtn4.setBackgroundResource(R.drawable.toolbar_friends_normal);
                break;
            case 1:
                toolBarBtn1.setBackgroundResource(R.drawable.toolbar_discover_normal);
                toolBarBtn2.setBackgroundResource(R.drawable.toolbar_music_selected);
                toolBarBtn3.setBackgroundResource(R.drawable.toolbar_friends_normal);
                toolBarBtn4.setBackgroundResource(R.drawable.toolbar_friends_normal);
                break;
            case 2:
                toolBarBtn1.setBackgroundResource(R.drawable.toolbar_discover_normal);
                toolBarBtn2.setBackgroundResource(R.drawable.toolbar_music_normal);
                toolBarBtn3.setBackgroundResource(R.drawable.toolbar_friends_selected);
                toolBarBtn4.setBackgroundResource(R.drawable.toolbar_friends_normal);
                break;
            case 3:
                toolBarBtn1.setBackgroundResource(R.drawable.toolbar_discover_normal);
                toolBarBtn2.setBackgroundResource(R.drawable.toolbar_music_normal);
                toolBarBtn3.setBackgroundResource(R.drawable.toolbar_friends_normal);
                toolBarBtn4.setBackgroundResource(R.drawable.toolbar_friends_selected);
                break;
            default:
                break;
        }

    }
}
