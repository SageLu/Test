package zz.com.test1.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import zz.com.test1.R;

public class JpushTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_test);
        //webView去展示一下
        String url = getIntent().getStringExtra("url");
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
    }
}
