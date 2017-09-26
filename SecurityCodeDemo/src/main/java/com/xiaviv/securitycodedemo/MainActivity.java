package com.xiaviv.securitycodedemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SecurityCodeView.InputCompleteListener {

    private SecurityCodeView editText;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListener();
    }

    private void setListener() {
        editText.setInputCompleteListener(this);
    }

    private void findViews() {
        editText = (SecurityCodeView) findViewById(R.id.scv_edittext);
        text = (TextView) findViewById(R.id.tv_text);
    }

    @Override
    public void inputComplete(int length) {

//        Toast.makeText(getApplicationContext(), "订单号是：" + editText.getEditContent(), Toast.LENGTH_LONG).show();
        if (length >= 18) {
            if (!editText.getEditContent().equals("01234567890123456789")) {
                text.setText("订单号输入错误");
                text.setTextColor(Color.RED);

            }
        } else {
            text.setText("输入的订单号不足18位");
            text.setTextColor(Color.RED);
        }

    }

    @Override
    public void inputCompleting() {
        text.setText("请输入18~25位订单号...");
        text.setTextColor(Color.BLACK);
    }

    @Override
    public void deleteContent(boolean isDelete) {
        if (isDelete) {
            text.setText("请输入18~25位订单号...");
            text.setTextColor(Color.BLACK);
        }
    }
}
