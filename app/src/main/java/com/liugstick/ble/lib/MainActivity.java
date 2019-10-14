package com.liugstick.ble.lib;

import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liugs.stble.BleManager;
import com.liugs.stble.scan.BleScanCallback;
import com.liugs.stble.scan.BleScanManager;
import com.liugs.stble.scan.ScanLocal;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager
                linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CustomAdapter();
        adapter.setOnItemClick(new CustomAdapter.ItemClickCallback() {
            @Override
            public void onClickItem(MyBleDevice device) {
                Intent intent = new Intent(MainActivity.this,ConnectActivity.class);
                intent.putExtra(KeyUtil.KEY_DEVICE,device);
                MainActivity.this.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        BleManager.getInstance().init(getApplicationContext());
    }

    public void startScan(View view) {
        adapter.clear();
        // 1.创建扫描工具,
        // (1)ScanLocal<ScanResult> scanLocal = new ScanLocal<>(ScanResult.class);
        // (2)
        ScanLocal<MyBleDevice> scanLocal = new ScanLocal<MyBleDevice>(MyBleDevice.class) {
            @Override
            protected MyBleDevice parser(ScanResult result) {
                MyBleDevice myBleDevice = new MyBleDevice();
                myBleDevice.setMac(result.getDevice().getAddress());
                myBleDevice.setName(result.getDevice().getName());
                return myBleDevice;
            }
        };

        // 2.设置系统ble扫描参数
        //ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
        //BleScanConfig config = new BleScanConfig.Builder().setTime(8000).setScanSettings(settings).build();
        //scanLocal.setConfig(config);

        // 3.监听扫描
        scanLocal.setCallback(new BleScanCallback<MyBleDevice>() {
            @Override
            public void onScanResult(MyBleDevice result) {
                //Log.i(TAG,"onScanResult" + result.getDevice().getName());
                Toast.makeText(MainActivity.this, "" + result.getName(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onScanResult" + result.getName());
                adapter.add(result);
            }

            @Override
            public void onFinished() {
                Log.i(TAG, "onFinished");
            }
        });

        // 4. 开始扫描
        BleScanManager.getInstance().startScan(scanLocal);
    }

    public void stopScan(View view) {
        BleScanManager.getInstance().stopScan();
    }
}
