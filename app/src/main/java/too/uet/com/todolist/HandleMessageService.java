package too.uet.com.todolist;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import too.uet.com.todolist.models.DeleteToDoMessage;
import too.uet.com.todolist.models.FirstLoginMessage;
import too.uet.com.todolist.models.NewToDoMessage;

/**
 * Created by TooNies1810 on 4/22/16.
 */
public class HandleMessageService extends Service{
    private static final String TAG = "HandleMessageService";

    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "service is onCreate");
//        Toast.makeText(this, "on create", Toast.LENGTH_SHORT).show();

        try{
            mSocket = IO.socket("http://viettel.io:5678");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("first_login", listenerFirstMess);
        mSocket.on("new_todo", listenerMess);
        mSocket.on("delete_todo", listenDeleteMess);
        mSocket.connect();
    }

    Emitter.Listener listenerFirstMess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Log.i(TAG, "first login ok");
            mSocket.off("first_login");
            EventBus.getDefault().post(new FirstLoginMessage(args[0].toString()));
        }
    };

    Emitter.Listener listenerMess = new Emitter.Listener(){
        @Override
        public void call(Object... args) {
//            Log.i(TAG, args[0].toString());
            EventBus.getDefault().postSticky(new NewToDoMessage(args[0].toString()));
        }
    };

    Emitter.Listener listenDeleteMess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            EventBus.getDefault().post(new DeleteToDoMessage(args[0].toString()));
        }
    };

    public void deleteMess(String id){
//        Log.i(TAG, "bam delete");
        if (mSocket.connected()){
            Log.i(TAG, "bam delete connected: " + id);
            mSocket.emit("delete_todo_", id);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "service destroy");

        // disconect and release listener
        mSocket.disconnect();
        mSocket.off("new_todo");
        mSocket.off("delete_todo");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "service onstartcommand");
//        Toast.makeText(this, "on start command", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    private Messenger messenger = new Messenger(new RequestMessageHandler());

    public static final int NEW_TO_DO = 1;
    public static final int DELETE_TO_DO = 2;

    public final class RequestMessageHandler extends Handler{
        @Override
        public void handleMessage(final Message msg) {
            if (msg.arg1 == NEW_TO_DO){
                if (msg.getData().getString("title") == null){
                    Log.i(TAG, "null");
                } else {
                    Log.i(TAG, msg.getData().getString("title"));
                }
                HandleMessageService.this.sendMessage(msg.getData());
            } else if (msg.arg1 == DELETE_TO_DO){
                HandleMessageService.this.deleteMess(msg.getData().getString("id"));
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
//        Log.i(TAG, "onBind");
        return messenger.getBinder();
    }

    public void sendMessage(Bundle bundle) {

        JSONObject json = new JSONObject();
        try {
            json.put("title", bundle.getString("title"));
            json.put("content", bundle.getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("insert_todo", json);
    }
}
