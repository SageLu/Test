package sage.lu6gmail.com.sageupapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DialogActivity extends AppCompatActivity {
    public static boolean isShow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setContentView(R.layout.activity_dialog);
    }

    @Override
    protected void onDestroy() {
        isShow = false;
        super.onDestroy();
    }
}
