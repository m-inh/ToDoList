package too.uet.com.todolist.demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import too.uet.com.todolist.R;

/**
 * Created by TooNies1810 on 5/6/16.
 */
public class MainActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btnClick = (Button) findViewById(R.id.btn_click);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messenger == null){
                    return;
                }
                // say something
                Message msg = new Message();
                msg.arg1 = 1;
                try {
                    messenger.send(msg);
                    msg.replyTo = replyMessenger;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        // bind service
        Intent mIntent = new Intent();
        mIntent.setClass(this, HelloMessengerService.class);
        bindService(mIntent, conn, BIND_AUTO_CREATE);
    }

    private Messenger messenger;

    private Messenger replyMessenger = new Messenger(new ReplyClientHandler());

    public final class ReplyClientHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Log.i("MainActivity2", "receive a reply msg from service");
            Toast.makeText(MainActivity2.this,  "receive a reply msg from service", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection  conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);

            Log.i("MainActivity2", "binded");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("MainActivity2", "unbind");
        }
    };

}
