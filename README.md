# stble.jar 文档说明

在AndroidManifest.xml 增加权限
   <uses-permission android:name="android.permission.BLUETOOTH" />
   <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   
导入stble.jar架包，在build.gradle 增加依赖
   implementation files('stble.jar')
   
初始化sdk:
   BleManager.getInstance().init(getApplicationContext());
   
扫描功能：
        1.创建扫描工具
        (1)使用系统扫描实体类
           ScanLocal<ScanResult> scanLocal = new ScanLocal<>(ScanResult.class);
        (2)支持自定义单个设备解析实体类
           ScanLocal<MyBleDevice> scanLocal = new ScanLocal<MyBleDevice>(MyBleDevice.class) {
                @Override
                protected MyBleDevice parser(ScanResult result) {
                    MyBleDevice myBleDevice = new MyBleDevice();
                    myBleDevice.setMac(result.getDevice().getAddress());
                    myBleDevice.setName(result.getDevice().getName());
                    return myBleDevice;
                }
            };

        2.设置系统ble扫描参数，设置扫描时长
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
        BleScanConfig config = new BleScanConfig.Builder().setTime(8000).setScanSettings(settings).build();
        scanLocal.setConfig(config);

        3.监听扫描
        scanLocal.setCallback(new BleScanCallback<MyBleDevice>() {
            @Override
            public void onScanResult(MyBleDevice result) { 
                Log.i(TAG, "onScanResult" + result.getName());
            }

            @Override
            public void onFinished() {
                Log.i(TAG, "onFinished");
            }
        });

        4. 开始扫描
           BleScanManager.getInstance().startScan(scanLocal);
        
        5. 停止扫描
           BleScanManager.getInstance().stopScan();
    
