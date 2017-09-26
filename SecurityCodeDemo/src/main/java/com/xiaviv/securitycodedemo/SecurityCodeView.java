package com.xiaviv.securitycodedemo;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * SecurityCodeView自定义输入框
 */

public class SecurityCodeView extends RelativeLayout {
    private Context context;
    private EditText editText;
    private TextView[] TextViews;
    private StringBuffer stringBuffer = new StringBuffer();
    private int count = 25;
    private String inputContent;
    private View btn_ok;

    public SecurityCodeView(Context context) {
        this(context, null);
    }

    public SecurityCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecurityCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TextViews = new TextView[25];
        View.inflate(context, R.layout.view_security_code, this);
        btn_ok = findViewById(R.id.btn_ok);
        editText = (EditText) findViewById(R.id.item_edittext);
        TextViews[0] = (TextView) findViewById(R.id.item_code_iv1);
        TextViews[1] = (TextView) findViewById(R.id.item_code_iv2);
        TextViews[2] = (TextView) findViewById(R.id.item_code_iv3);
        TextViews[3] = (TextView) findViewById(R.id.item_code_iv4);
        TextViews[4] = (TextView) findViewById(R.id.item_code_iv5);
        TextViews[5] = (TextView) findViewById(R.id.item_code_iv6);
        TextViews[6] = (TextView) findViewById(R.id.item_code_iv7);
        TextViews[7] = (TextView) findViewById(R.id.item_code_iv8);
        TextViews[8] = (TextView) findViewById(R.id.item_code_iv9);
        TextViews[9] = (TextView) findViewById(R.id.item_code_iv10);
        TextViews[10] = (TextView) findViewById(R.id.item_code_iv11);
        TextViews[11] = (TextView) findViewById(R.id.item_code_iv12);
        TextViews[12] = (TextView) findViewById(R.id.item_code_iv13);
        TextViews[13] = (TextView) findViewById(R.id.item_code_iv14);
        TextViews[14] = (TextView) findViewById(R.id.item_code_iv15);
        TextViews[15] = (TextView) findViewById(R.id.item_code_iv16);
        TextViews[16] = (TextView) findViewById(R.id.item_code_iv17);
        TextViews[17] = (TextView) findViewById(R.id.item_code_iv18);
        TextViews[18] = (TextView) findViewById(R.id.item_code_iv19);
        TextViews[19] = (TextView) findViewById(R.id.item_code_iv20);
        TextViews[20] = (TextView) findViewById(R.id.item_code_iv21);
        TextViews[21] = (TextView) findViewById(R.id.item_code_iv22);
        TextViews[22] = (TextView) findViewById(R.id.item_code_iv23);
        TextViews[23] = (TextView) findViewById(R.id.item_code_iv24);
        TextViews[24] = (TextView) findViewById(R.id.item_code_iv25);

        editText.setCursorVisible(false);//将光标隐藏
        setListener();
    }

    private void setListener() {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("bianhua","beforeTextChanged"+i+"---"+i1+"---"+i2);
                inputCompleteListener.inputCompleting();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("bianhua","onTextChanged"+i+"---"+i1+"---"+i2);
                inputCompleteListener.inputCompleting();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //重点   如果字符不为""时才进行操作
                if (!editable.toString().equals("")) {
                    if (stringBuffer.length() > 24) {
                        //当文本长度大于3位时edittext置空
                        editText.setText("");
                        return;
                    } else {
                        //将文字添加到StringBuffer中
                        stringBuffer.append(editable);
                        editText.setText("");//添加后将EditText置空  造成没有文字输入的错局
                        //  Log.e("TAG", "afterTextChanged: stringBuffer is " + stringBuffer);
                        count = stringBuffer.length();//记录stringbuffer的长度
                        inputContent = stringBuffer.toString();

//                        if (stringBuffer.length() == 4) {
//                            //文字长度位4  则调用完成输入的监听
//                            if (inputCompleteListener != null) {
//                                inputCompleteListener.inputComplete();
//                            }
//                        }

                    }

                    for (int i = 0; i < stringBuffer.length(); i++) {
                        TextViews[i].setText(String.valueOf(inputContent.charAt(i)));
                        TextViews[i].setBackgroundResource(R.mipmap.bg_verify_press);
                    }

                }
            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onKeyDelete()) return true;
                    return true;
                }
                return false;
            }
        });

        //确定之后再去请求
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringBuffer.length() >= 18) {
                    //文字长度位4  则调用完成输入的监听
                    if (inputCompleteListener != null) {
                        inputCompleteListener.inputComplete(stringBuffer.length());
                    }
                } else {
//                    Toast.makeText(context, "输入的订单号不足18位", Toast.LENGTH_SHORT).show();
                    if (inputCompleteListener != null) {
                        inputCompleteListener.inputComplete(stringBuffer.length());
                    }
                }
            }
        });
    }


    public boolean onKeyDelete() {
        if (count == 0) {
            count = 25;
            return true;
        }
        if (stringBuffer.length() > 0) {
            //删除相应位置的字符
            stringBuffer.delete((count - 1), count);
            count--;
            //   Log.e(TAG, "afterTextChanged: stringBuffer is " + stringBuffer);
            inputContent = stringBuffer.toString();
            TextViews[stringBuffer.length()].setText("");
            TextViews[stringBuffer.length()].setBackgroundResource(R.mipmap.bg_verify);
            if (inputCompleteListener != null)
                inputCompleteListener.deleteContent(true);//有删除就通知manger

        }
        return false;
    }

    /**
     * 清空输入内容
     */
    public void clearEditText() {
        stringBuffer.delete(0, stringBuffer.length());
        inputContent = stringBuffer.toString();
        for (int i = 0; i < TextViews.length; i++) {
            TextViews[i].setText("");
            TextViews[i].setBackgroundResource(R.mipmap.bg_verify);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {
        void inputComplete(int length);
        void inputCompleting();

        void deleteContent(boolean isDelete);
    }

    /**
     * 获取输入文本
     *
     * @return
     */
    public String getEditContent() {
        return inputContent;
    }

}