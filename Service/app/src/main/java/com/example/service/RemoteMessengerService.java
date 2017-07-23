package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;


public class RemoteMessengerService extends Service {
    private static final String TAG = "MessengerTest";
    Handler mRemoteHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"Service receive client message,msg.what=" + msg.what);
            switch (msg.what){
                case 0:
                    Message message = Message.obtain();
                    message.what = 0;
                    Random random = new Random();
                    message.arg1 = random.nextInt(100);
//                    msg的replyTo变量是客户端生成的Messenger对象
                    Messenger mClientMessenger = msg.replyTo;
                    if (mClientMessenger == null) return;
                    try {
//                        向客户端回传信息
                        mClientMessenger.send(message);
                        Log.d(TAG,"Service reply client message,random Num is:" + message.arg1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    Messenger mRemoteMessenger = new Messenger(mRemoteHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        将服务端的Messenger对象传递给客户端
        return mRemoteMessenger.getBinder();
    }
}
