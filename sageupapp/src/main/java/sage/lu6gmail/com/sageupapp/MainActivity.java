package sage.lu6gmail.com.sageupapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void rxjava(View view){


    }


    public void test(View view) {
        new UpdateAppManager.Buidler().setmActivity(this)
                .setmHttpManager(new UpdateAppHttpUtil())
                .setmUpdateUrl("http://test.api.b.lbl.aduer.com/app/version.htm?date=1499414852")
                .build()
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialog();
                    }

                    @Override
                    protected void onAfter() {
                        Toast.makeText(MainActivity.this, "之后", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void noNewApp() {

                    }

                    @Override
                    protected void onBefore() {
                        Toast.makeText(MainActivity.this, "之前", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
