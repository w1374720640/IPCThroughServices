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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MessengerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MessengerTest";
    private Messenger mRemoteMessenger;
    private boolean isConnect;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        findViewById(R.id.bt_send).setOnClickListener(this);
        tv_show = (TextView) findViewById(R.id.tv_show);
        connectService();
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            获取服务端的Messenger对象
            mRemoteMessenger = new Messenger(service);
            isConnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
        }
    };

    Handler mClientHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
//                    tv_show.setText(tv_show.getText() + "收到消息：" + msg.arg1 + "\n");
                    Log.d(TAG,"Client receive message:" + msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

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
