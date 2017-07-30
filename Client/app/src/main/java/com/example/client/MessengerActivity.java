package com.example.client;

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
import android.view.View;
import android.widget.TextView;

/**
 * 通过Messenger进行进程间通信
 */
public class MessengerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MessengerTest";
//    服务端的Messenger对象，绑定服务时由客户端根据服务端返回的IBinder对象实例化
    private Messenger mRemoteMessenger;
    private boolean isConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        findViewById(R.id.bt_send).setOnClickListener(this);
        connectService();
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            通过服务端传递的IBinder对象实例化一个Messenger对象
//            通过mRemoteMessenger对象可以向服务端发送Message消息
            mRemoteMessenger = new Messenger(service);
            isConnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
        }
    };

    /**
     * 客户端的Handler，mClientMessenger对象包含mClientHandler的引用，
     * 可以不实现，如果不实现则服务端不能向客户端发送消息，只能单向通信
     */
    Handler mClientHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Log.d(TAG,"Client receive message:" + msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

//    客户端实现的Messenger对象，传递给服务端后服务端可以向客户端发送消息
    private Messenger mClientMessenger = new Messenger(mClientHandler);

    private void connectService(){
        Intent intent = new Intent();
        intent.setAction("com.example.service.RemoteMessengerService");
//        Android 5.0以上需要设置包名
        intent.setPackage("com.example.service");
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    private void disConnectService(){
        unbindService(mConnection);
        isConnect = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send:
                sendMessage();
                break;
        }
    }

    private void sendMessage(){
        Log.d(TAG,"Client sendMessage()");
        if (!isConnect) return;
        Message message = Message.obtain();
        message.what = 0;
//                将客户端的Messenger对象传递到服务端，
//                不设置则只能单向通信，服务端无法向客户端传递信息
        message.replyTo = mClientMessenger;
        try {
//                    向服务端发送消息
            mRemoteMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectService();
    }
}
