package com.liugstick.ble.lib;

import android.bluetooth.le.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liugs.stble.BleManager;
import com.liugs.stble.scan.BleScanConfig;
import com.liugs.stble.scan.BleScanManager;
import com.liugs.stble.scan.DefaultCallback;
import com.liugs.stble.scan.ScanLocal;

public class MainActivity extends AppCompatActivity {
private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BleManager.getInstance().init(getApplicationContext());
    }

    public void startScan(View view) {
        ScanLocal<ScanResult> scanLocal = new ScanLocal<ScanResult>(){
            @Override
            protected ScanResult parser(ScanResult result) {
                return result;
            }
        };
        scanLocal.setConfig(new BleScanConfig.Builder().setTime(8000).build());
        scanLocal.setCallback(new DefaultCallback<ScanResult>() {
            @Override
            public void onScanResult(ScanResult result) {
                Log.i(TAG,"onScanResult" + result.getDevice().getName());
                Toast.makeText(MainActivity.this,"" + result.getRssi(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                Log.i(TAG,"onFinished");
            }
        });
        BleScanManager.getInstance().startScan(scanLocal);
    }

    public void stopScan(View view) {
        BleScanManager.getInstance().stopScan();
    }
}
