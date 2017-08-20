package com.example.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.service.ClientCallback;
import com.example.service.Person;
import com.example.service.RemoteInterface;

/**
 * 通过aidl进行进程间通信
 */
public class AidlActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AidlTest";
    private boolean isConnect;

//    服务端的RemoteInterface对象，绑定服务时创建
    private RemoteInterface mRemoteInterface = null;

//    客户端的ClientCallback对象
//    在服务端注册后服务端可以调用客户端方法
    private ClientCallback.Stub mClientCallback = new ClientCallback.Stub() {
        @Override
        public void start() throws RemoteException {
            Log.d(TAG, "Client start");
        }

        @Override
        public void stop() throws RemoteException {
            Log.d(TAG, "Client stop");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        findViewById(R.id.bt_get).setOnClickListener(this);
        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_regist).setOnClickListener(this);
        findViewById(R.id.bt_unregist).setOnClickListener(this);

        connectService();
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnect = true;
//            绑定服务后从服务端获取RemoteInterface对象
            mRemoteInterface = RemoteInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
//            与服务端意外断开时自动重连
            connectService();
        }
    };


    private void connectService() {
        Intent intent = new Intent();
        intent.setAction("com.example.service.RemoteAidlService");
//        Android 5.0以上需要设置包名
        intent.setPackage("com.example.service");
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private void disConnectService() {
        unbindService(mConnection);
        isConnect = false;
    }

    @Override
    public void onClick(View v) {
        if (!isConnect) return;
        switch (v.getId()) {
            case R.id.bt_get:
                Person person = getPerson(10);
                Log.d(TAG, "Client getPerson return, person=" +
                        (person == null ? null : person.toString()));
                break;
            case R.id.bt_add:
                Person person1 = new Person();
                person1.setId(100);
                person1.setName("小花");
                person1.setAge(16);
                person1.setPhone("110");
                addPerson(person1);
                break;
            case R.id.bt_regist:
                registCallback();
                break;
            case R.id.bt_unregist:
                unRegistCallback();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectService();
    }

    /**
     * 从服务端获取数据
     */
    private Person getPerson(int id) {
        Log.d(TAG,"Client getPerson()");
        if (!isConnect) return null;
        Person person = null;
        try {
            person = mRemoteInterface.getPersonById(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return person;
    }

    /**
     * 向服务端添加数据
     */
    private void addPerson(Person person) {
        Log.d(TAG,"Client addPerson()");
        if (!isConnect) return;
        try {
            mRemoteInterface.addPerson(person);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务端注册回调，注册后服务端才能调用客户端方法
     */
    private void registCallback() {
        Log.d(TAG,"Client registCallback()");
        if (!isConnect) return;
        try {
            mRemoteInterface.registClientCallback(mClientCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消注册
     */
    private void unRegistCallback() {
        Log.d(TAG,"Client unRegistCallback()");
        if (!isConnect) return;
        try {
            mRemoteInterface.unRegistClientCallback(mClientCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
