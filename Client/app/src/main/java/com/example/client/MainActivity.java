package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_messenger).setOnClickListener(this);
        findViewById(R.id.bt_aidl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_messenger:
                Intent messengerIntent = new Intent(this,MessengerActivity.class);
                startActivity(messengerIntent);
                break;
            case R.id.bt_aidl:
                Intent aidlIntent = new Intent(this,AidlActivity.class);
                startActivity(aidlIntent);
                break;
        }
    }
}
