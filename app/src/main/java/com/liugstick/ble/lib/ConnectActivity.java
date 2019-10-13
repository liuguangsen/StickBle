package com.liugstick.ble.lib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liugs.stble.gatt.GattChannelManager;
import com.liugs.stble.gatt.GattConfig;
import com.liugs.stble.gatt.callback.UiGattCallback;

public class ConnectActivity extends AppCompatActivity implements UiGattCallback {

    private String mac;
    private TextView textView;
    private StringBuilder builder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        textView = findViewById(R.id.tv);

        Intent intent = getIntent();
        MyBleDevice device = intent.getParcelableExtra(KeyUtil.KEY_DEVICE);
        mac = device.getMac();
    }

    public void startConnect(View view) {
        GattConfig config = new GattConfig(mac);
        config.setMtu(true,180,500);
        GattChannelManager.getInstance().startGatt(config,this);
    }

    public void closeConnect(View view) {
        GattChannelManager.getInstance().closeGatt(mac);
    }

    @Override
    public void onCreateChannelResult(boolean isSuccess) {
        addTv("建立链路成功 " + isSuccess);
    }

    @Override
    public void onMessage(String msg) {
        addTv(msg);
    }

    @Override
    public void onError(String error) {
        addTv(error);
    }

    private void addTv(String tv){
        builder.append(tv).append("\n");
        textView.setText(builder.toString());
    }
}
