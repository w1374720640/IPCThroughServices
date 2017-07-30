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

/**
 * 服务端，利用Messenger与客户端通信
 */
public class RemoteMessengerService extends Service {
    private static final String TAG = "MessengerTest";

    /**
     * 服务端的Handler，Messenger对象包含Handler的引用，
     * 客户端通过Messenger发送的消息由服务端的Handler处理
     */
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
//                    如果为空则不能由服务端向客户端传递消息，只能单向通信
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

//    利用mRemoteHandler对象创建一个Messenger对象
    Messenger mRemoteMessenger = new Messenger(mRemoteHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        通过Messenger得到一个IBinder对象，通过onBind方法返回给客户端
        return mRemoteMessenger.getBinder();
    }
}
