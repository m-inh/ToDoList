package too.uet.com.todolist;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by TooNies1810 on 5/6/16.
 */
public class NotiMessageService extends Service {

    private static final String TAG = "NotiMessageService";

    private NotificationManager mNM;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(String id, String title, String content) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.remote_service_started);

        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage("too.uet.com.todolist");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);

        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_mini_todo)  // the status icon
                .setTicker(title)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(title)  // the label of the entry
                .setContentText(content)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(id.hashCode(), notification);
    }

    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();

//        Log.i(TAG, "oncreate");

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        try{
            mSocket = IO.socket("http://viettel.io:5678");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("new_todo", listenerMess);
        mSocket.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "onstartcommand");
        return START_STICKY;
    }

    Emitter.Listener listenerMess = new Emitter.Listener(){
        @Override
        public void call(Object... args) {
//            Log.i(TAG, args[0].toString());

            String jsonString = args[0].toString();
            try {
                JSONObject json = new JSONObject(jsonString);
                String title = json.getString("title");
                String content = json.getString("content");
                String id = json.getString("_id");
                showNotification(id, title,content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i(TAG, "ondestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
