package sage.lu6gmail.com.sageupapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {
    public static boolean isRunning = false;
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
