package too.uet.com.todolist;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by TooNies1810 on 5/10/16.
 */
public class AppController extends Application {

    private static final String TAG = "AppController";

    @Override
    public void onCreate() {
        super.onCreate();

//        Log.i(TAG, "oncreate");

        // demo
        Intent intent = new Intent();
        intent.setClass(this, NotiMessageService.class);
        startService(intent);
    }
}
