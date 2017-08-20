package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端，利用AIDL与客户端通信
 */
public class RemoteAidlService extends Service {
    private static final String TAG = "AidlTest";
    private static final int START_ALL_CLIENT = 0;
    private static final int STOP_ALL_CLIENT = 1;
//    模拟服务端存储客户端传递的数据
    private List<Person> mPersonList = new ArrayList<>();
//    一个服务端可以对应多个客户端，即包含多个ClientCallback对象，
//    使用RemoteCallbackList可以在客户端意外断开连接时移除ClientCallback，防止DeadObjectException
    private RemoteCallbackList<ClientCallback> mCallbackList = new RemoteCallbackList<>();
//    通过修改值确定是否在regist后start客户端，默认不启动
    private boolean isAutoStartAfterRegist = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRemoteInterface;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        服务结束是注意移除所有数据
        mCallbackList.kill();
    }

    /**
     * RemoteInterface.Stub为Android根据aidl文件生成的实现类，
     * 实现了RemoteInterface接口，间接实现了IBinder接口，
     * 客户端绑定时将mRemoteInterface对象返回给客户端，
     * 在服务端定义，在客户端调用
     */
    private RemoteInterface.Stub mRemoteInterface = new RemoteInterface.Stub() {
        @Override
        public Person getPersonById(int id) throws RemoteException {
//            返回固定值
            Person person = new Person();
            person.setId(id);
            person.setName("小红");
            person.setAge(18);
            person.setPhone("120");
            Log.d(TAG, "Service getPersonById()");
            return person;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            mPersonList.add(person);
            Log.d(TAG, "Service addPerson(),person="
                    + (person == null ? null : person.toString()));
        }

        @Override
        public void registClientCallback(ClientCallback callback) throws RemoteException {
//            向服务端注册回调
            mCallbackList.register(callback);
            Log.d(TAG, "Service registClientCallback()");
            if (isAutoStartAfterRegist) {
                mHandler.sendEmptyMessageDelayed(START_ALL_CLIENT, 3 * 1000);
            }
        }

        @Override
        public void unRegistClientCallback(ClientCallback callback) throws RemoteException {
//            服务端取消注册回调
            mCallbackList.unregister(callback);
            Log.d(TAG, "Service unRegistClientCallback()");
        }

    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_ALL_CLIENT:
                    startAllClient();
                    mHandler.sendEmptyMessageDelayed(STOP_ALL_CLIENT, 3 * 1000);
                    break;
                case STOP_ALL_CLIENT:
                    stopAllClient();
                    break;
            }
        }
    };

    /**
     * 调用所有客户端的start()方法
     */
    public void startAllClient() {
        Log.d(TAG, "Service startAllClient()");
//        从列表中取数据时先调用beginBroadcast()方法获取总数，循环取出数据后finishBroadcast()
        int size = mCallbackList.beginBroadcast();
        for (int i = 0;i < size;i++){
            try {
                mCallbackList.getBroadcastItem(i).start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbackList.finishBroadcast();
    }

    /**
     * 调用所有客户端的stop()方法
     */
    public void stopAllClient() {
        Log.d(TAG, "Service stopAllClient()");
        int size = mCallbackList.beginBroadcast();
        for (int i = 0;i < size;i++){
            try {
                mCallbackList.getBroadcastItem(i).stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbackList.finishBroadcast();
    }
}
