package too.uet.com.todolist;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import too.uet.com.todolist.models.DeleteToDoMessage;
import too.uet.com.todolist.models.FirstLoginMessage;
import too.uet.com.todolist.models.ItemList;
import too.uet.com.todolist.models.NewToDoMessage;
import too.uet.com.todolist.models.ServerMessage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Messenger sendMessenger;

//    private Messenger receiverMessenger = new Messenger(new MessageHandler());

    private final class MessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            sendMessenger = new Messenger(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "service is dissconected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        // demo
        Intent intent = new Intent();
        intent.setClass(this, NotiMessageService.class);
        startService(intent);

        // start service
        Intent mIntent = new Intent();
        mIntent.setClass(this, HandleMessageService.class);
        startService(mIntent);
        bindService(mIntent, conn, BIND_AUTO_CREATE);
    }

    private ListView lvMain;
    private ToDoListAdapter mAdapter;

    private void initViews() {
        lvMain = (ListView) findViewById(R.id.lv_main);
        mAdapter = new ToDoListAdapter(this);
        lvMain.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final ServerMessage mess) {
        if (mess instanceof NewToDoMessage) {
            try {
                JSONObject json = new JSONObject(((NewToDoMessage) mess).getJsonString());
                final ItemList newItem = new ItemList(json.getString("title"), json.getString("content"));
                newItem.setId(json.getString("_id"));
                mAdapter.addNewItem(newItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mess instanceof DeleteToDoMessage) {
            mAdapter.deleteMessUI(((DeleteToDoMessage) mess).getJsonString());
        } else if (mess instanceof FirstLoginMessage) {
            mAdapter.setItemArr(((FirstLoginMessage) mess).getJsonString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register eventbus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister eventbus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.item_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            Log.i(TAG, "ok");
            NewItemPopUp dialog = new NewItemPopUp(this);
            dialog.setMainActivity(this);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(ItemList item) {
//        Log.i(TAG, "ok hang ve");

        Message msg = new Message();
        msg.arg1 = HandleMessageService.NEW_TO_DO;
        Bundle b = new Bundle();
        b.putString("title", item.getTitle());
        b.putString("content", item.getContent());
        msg.setData(b);

        try {
            sendMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deleteMess(String id) {
        Message msg = new Message();
        msg.arg1 = HandleMessageService.DELETE_TO_DO;
        Bundle b = new Bundle();
        b.putString("id", id);
        msg.setData(b);

        try {
            sendMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
