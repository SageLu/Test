package zz.com.test1.meaccount;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import zz.com.test1.R;

public class MeAccountActivity extends FragmentActivity {


    private static final String MECOUNT_TAG = "mecount_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_me_account);
//初始化Fragment
        initFragment();

    }

    private void initFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft= fm.beginTransaction();
        //3.替换

        ft.replace(R.id.fl_account,new ContentFragment(fm), MECOUNT_TAG);//主页
        //4.提交
        ft.commit();
    }


}
