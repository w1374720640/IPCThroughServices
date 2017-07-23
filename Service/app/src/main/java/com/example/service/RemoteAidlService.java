package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class RemoteAidlService extends Service {
    private static final String TAG = "AidlTest";
    private static final int START_ALL_CLIENT = 0;
    private static final int STOP_ALL_CLIENT = 1;
    private List<Person> mPersonList = new ArrayList<>();
    private List<ClientCallback> mCallbackList = new ArrayList<>();
//    通过修改值确定是否在regist后start客户端，默认不启动
    private boolean isAutoStartAfterRegist = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRemoteInterface;
    }

    private RemoteInterface.Stub mRemoteInterface = new RemoteInterface.Stub() {
        @Override
        public Person getPersonById(int id) throws RemoteException {
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
            mCallbackList.add(callback);
            Log.d(TAG, "Service registClientCallback()");
            if (isAutoStartAfterRegist) {
                mHandler.sendEmptyMessageDelayed(START_ALL_CLIENT, 3 * 1000);
            }
        }

        @Override
        public void unRegistClientCallback(ClientCallback callback) throws RemoteException {
            mCallbackList.remove(callback);
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

    public void startAllClient() {
        Log.d(TAG, "Service startAllClient()");
        for (ClientCallback callback : mCallbackList) {
            try {
                callback.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopAllClient() {
        Log.d(TAG, "Service stopAllClient()");
        for (ClientCallback callback : mCallbackList) {
            try {
                callback.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
