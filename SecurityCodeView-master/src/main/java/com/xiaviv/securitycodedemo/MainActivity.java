package com.xiaviv.securitycodedemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SecurityCodeView.InputCompleteListener {

    private SecurityCodeView editText;
    private TextView text;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
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
    public void inputComplete() {
        Toast.makeText(getApplicationContext(), "验证码是s：" + securityCodeView.getEditContent(), Toast.LENGTH_LONG).show();
        dialog.hide();
//        if (!editText.getEditContent().equals("123456")) {
//            text.setText("验证码输入错误");
//            text.setTextColor(Color.RED);
//        }
    }

    @Override
    public void deleteContent(boolean isDelete) {
//        if (isDelete) {
//            text.setText("输入验证码表示同意《用户协议》");
//            text.setTextColor(Color.BLACK);
//        }
    }

    public void btnDialog(View view) {
        toDialogActivity();
    }

    Dialog dialog;
    SecurityCodeView securityCodeView;
    private void toDialogActivity() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tuikuan, null);
        // 对话框
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        // 设置宽度为屏幕的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);

//        InputTools.KeyBoard(et_hexiao, "open");
//        InputTools.HideKeyboard(et_hexiao);
        //findview
         securityCodeView = (SecurityCodeView) view.findViewById(R.id.scv_edittext);
        securityCodeView.setInputCompleteListener(this);
    }
}
