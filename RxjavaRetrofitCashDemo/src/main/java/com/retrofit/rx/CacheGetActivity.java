package com.retrofit.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.retrofit.rx.adapter.BaseBeanAdapter;
import com.retrofit.rx.bean.baseBean;
import com.retrofit.rx.network.Network;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import butterknife.BindView;
//import butterknife.ButterKnife;

/**
 * Created by sunxx on 2016/6/20.
 */
public class CacheGetActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    BaseBeanAdapter adapter;
    int CurrentInt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initViews();
    }


    private void initViews() {
        adapter = new BaseBeanAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        adapter.setItemListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(CacheGetActivity.this, "onItemClick"+postion, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int postion) {
                Toast.makeText(CacheGetActivity.this, "onItemLongClick"+postion, Toast.LENGTH_LONG).show();
            }
        });
      String str=  Glide.getPhotoCacheDir(CacheGetActivity.this).getAbsolutePath().toString();
        Log.e("Cache",str);
    }


    public void jsonbtn(View view) {
       showJson();
    }

    private void showJson() {
        Network.getDemo().getBase2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        Toast.makeText(CacheGetActivity.this, "hh"+s.toString(), Toast.LENGTH_SHORT).show();
                    }
                },new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(CacheGetActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void next(View view) {
        CurrentInt = CurrentInt + 1;
        show();
    }

    public void previous(View view) {
        if (CurrentInt >= 2) {
            CurrentInt = CurrentInt - 1;
        }
        show();
    }

    public void show() {
        Network.getDemo().getBase(CurrentInt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<baseBean>() {
                    @Override
                    public void call(baseBean mbaseBean) {
                        adapter.setData(mbaseBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(CacheGetActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
