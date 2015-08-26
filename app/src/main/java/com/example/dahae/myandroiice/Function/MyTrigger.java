package com.example.dahae.myandroiice.Function;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class MyTrigger extends Service {

    String TAG = "[ANDROI-ICE]";

    final int FAIL = -1;

    //블루투스 미지원
    public static final int BLUETOOTH_STATE_UNKNOW = -1;

    //본기기 블루트스 활성화
    public static final int BLUETOOTH_STATE_ON = BluetoothAdapter.STATE_ON;
    public static final int BLUETOOTH_STATE_OFF = BluetoothAdapter.STATE_OFF;



    boolean match;

    public MyTrigger() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "hi Tree");
        FirstService();

        //Log.d(TAG, "2");
        /**
         *         브로드캐스트로 받는것
         */
//         SMS수신
        registerReceiver(mybroadcastforComplex, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//         스크린
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_SCREEN_OFF));
//         비행기모드
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
//         전화발신
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
//         충전기
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
//         블루투스
        registerReceiver(mybroadcastforComplex, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(mybroadcastforComplex, new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));
//        와이파이
        registerReceiver(mybroadcastforComplex, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        registerReceiver(mybroadcastforComplex, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        registerReceiver(mybroadcastforComplex, new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION));
        registerReceiver(mybroadcastforComplex, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
//        이어폰
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
//        부팅됐을때
        registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));

        //registerReceiver(mybroadcastforComplex, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    public void FirstService(){

        Intent CheckPlan = new Intent(this, CheckPlan.class);
        CheckPlan.putExtra("i", 0);
        CheckPlan.putExtra("query", "empty");
        CheckPlan.putExtra("Complex", "empty");
        Log.d(TAG, "FirstService()");
        startService(CheckPlan);

        Log.d(TAG, "Out FirstService()");
    }

    BroadcastReceiver mybroadcastforComplex = new BroadcastReceiver() {

        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String action = intent.getAction();

            Intent CheckPlan = new Intent(context, CheckPlan.class);
            CheckPlan.putExtra("i", 0);
            CheckPlan.putExtra("query", "empty");
            CheckPlan.putExtra("Complex", "empty");


            Log.i(TAG, action + " hi mybroadcastforComplex ");


            if (action.equals("android.media.RINGER_MODE_CHANGED")) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                switch (am.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:
                        if (checkInBroadcast("Silence")) {
                            Log.i(TAG, "Silence mode");
                            startService(CheckPlan);
                        }
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        if (checkInBroadcast("Vibration")) {
                            Log.i(TAG, "Vibration mode");
                            startService(CheckPlan);
                        }
                        break;
                    case AudioManager.RINGER_MODE_NORMAL:
                        if (checkInBroadcast("Sound")) {
                            Log.i(TAG, "Sound mode");
                            startService(CheckPlan);
                        }
                        break;
                }
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo niMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                //NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                Log.d(TAG, "Mobile");
                if (niMobile.getState() == NetworkInfo.State.CONNECTED) {
                    Log.d(TAG, "Mobile CONNECTED");
                    if (checkInBroadcast("DataOn")) {
                        Log.d(TAG, "Mobile CONNECTED");
                        startService(CheckPlan);
                    }
                } else if (niMobile.getState() == NetworkInfo.State.CONNECTING) {

                    Log.d(TAG, "Do not matter");
                    //!mobile.isConnectedOrConnecting()
                } else if (niMobile.getState() == NetworkInfo.State.DISCONNECTED) {
                    Log.d(TAG, "Mobile DISCONNECTED");
                    if (checkInBroadcast("DataOff")) {
                        Log.d(TAG, "Mobile DISCONNECTED");
                        startService(CheckPlan);
                    }
                } else if (niMobile.getState() == NetworkInfo.State.DISCONNECTING) {
                    Log.d(TAG, "Donot matter");

                } else if (niMobile.getState() == NetworkInfo.State.SUSPENDED) {
                    Log.d(TAG, "Mobile SUSPENDED");

                } else if (niMobile.getState() == NetworkInfo.State.UNKNOWN) {
                    Log.d(TAG, "Mobile UNKNOWN");

                }

            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                WifiManager m_WifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                switch (m_WifiManager.getWifiState()) {

                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.d(TAG, "Do not matter");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:

                        if (checkInBroadcast("WifiOff")) {
                            Log.i(TAG, "WifiOff");
                            startService(CheckPlan);
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.d(TAG, "Donot matter");

                        break;
                    case WifiManager.WIFI_STATE_ENABLING:

                        if (checkInBroadcast("WifiOn")) {
                            Log.i(TAG, "WifiOn");
                            startService(CheckPlan);
                        }
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.d(TAG, "WIFI_STATE_UNKNOWN");
                        break;
                }
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                if (checkInBroadcast("ScreenOn")) {
                    Log.i(TAG, "Screen ON");
                    startService(CheckPlan);
                }

            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {

                if (checkInBroadcast("ScreenOff")) {
                    Log.i(TAG, "Screen OFF");
                    startService(CheckPlan);
                }

            } else if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                if (checkInBroadcast("SMSreceiver")) {
                    Log.d(TAG, "SMS_RECEIVED");
                    startService(CheckPlan);

                }
            } else if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                boolean enabling = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;

                //비행기 모드 On
                if (checkInBroadcast("AirplaneModeOn")) {
                    if (enabling = true) {
                        Log.d(TAG, "AirplaneModeOn");
                        startService(CheckPlan);
                    }
                }
                //비행기 모드 Off
                if (checkInBroadcast("AirplaneModeOff")) {
                    if (enabling = false) {
                        Log.d(TAG, "AirplaneModeOff");
                        startService(CheckPlan);
                    }
                }

            } else if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

                //전화발신
                if (checkInBroadcast("NewOutgoingCall")) {
                    Log.d(TAG, "ACTION_NEW_OUTGOING_CALL");
                    startService(CheckPlan);
                }
            } else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {

                //배터리충전
                if (checkInBroadcast("PowerConnected")) {
                    Log.d(TAG, "ACTION_POWER_CONNECTED");
                    startService(CheckPlan);
                }
            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {

                //배터리충전끝
                if (checkInBroadcast("PowerDisConnected")) {
                    Log.d(TAG, "ACTION_POWER_DISCONNECTED");
                    startService(CheckPlan);
                }
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                Bundle extras = intent.getExtras();
                switch (extras.getInt("android.bluetooth.adapter.extra.STATE", FAIL)) {

                    case BluetoothAdapter.STATE_OFF:
                        if (checkInBroadcast("BluetoothOff")) {
                            Log.d(TAG, " false BluetoothAdapter STATE_OFF");
                            startService(CheckPlan);
                        }

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if (checkInBroadcast("BluetoothOff")) {
                            Log.d(TAG, " false BluetoothAdapter STATE_TURNING_OFF");
                            startService(CheckPlan);
                        }

                    case BluetoothAdapter.STATE_TURNING_ON:
                        if (checkInBroadcast("BluetoothOn")) {
                            Log.d(TAG, "True BluetoothAdapter STATE_TURNING_ON");//블루투스켜짐
                            startService(CheckPlan);
                        }

                    case BluetoothAdapter.STATE_ON:
                        if (checkInBroadcast("BluetoothOn")) {
                            Log.d(TAG, "True BluetoothAdapter STATE_ON");//블루투스켜짐
                            startService(CheckPlan);
                        }
                }
            }else if (action.equals(Intent.ACTION_BATTERY_CHANGED)){
                Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                int level =  Integer.parseInt(BatteryManager.EXTRA_LEVEL);
                int scale = Integer.parseInt(BatteryManager.EXTRA_SCALE);
                float batteryPct = level / (float)scale;
                int battery = (int)(100 * batteryPct);

                Log.d(TAG,"Battery In Broadcast : " + battery);
                if(battery < 20){ // 나중엔 설정가능하게 만들기
                    if (checkInBroadcast("LowBattery")) {
                        Log.d(TAG, "LowBattery");

                        startService(CheckPlan);
                    }
                }
                if(battery > 70){
                    if (checkInBroadcast("FullBattery")) {
                        Log.d(TAG, "FullBattery");

                        startService(CheckPlan);
                    }
                }
            }
        }
    };



    public boolean checkInBroadcast(String triggerName){


        String tableNameDB;

        DBHelper mHelperCheckBroadcast= new DBHelper(this);
        SQLiteDatabase databaseCB = mHelperCheckBroadcast.getWritableDatabase();
        Cursor cursorT = databaseCB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        boolean result = false;
        if (cursorT.moveToFirst()) {
            while ( !cursorT.isAfterLast() ) {
                tableNameDB = cursorT.getString(0);
                if(!tableNameDB.equals("android_metadata")&& !tableNameDB.equals("sqlite_sequence")) {
                    result = check(triggerName, tableNameDB);
                }
                cursorT.moveToNext();
                return result;
            }
        }
        return result;
    }

    public boolean check(String name, String tableNameDB){

        boolean result = false;
        DBHelper mHelperCheck= new DBHelper(this);
        SQLiteDatabase databaseCheck = mHelperCheck.getWritableDatabase();
        Cursor CursorCheck = databaseCheck.rawQuery("SELECT * FROM " + tableNameDB, null);

        for (int j = 0; j < CursorCheck.getCount(); j++) {
Log.d(TAG,"table name is " + tableNameDB );

            if (CursorCheck.moveToNext()) {
                String triggerNameCheck = CursorCheck.getString(1);

                Log.d(TAG,"triggerNameCheck is " +triggerNameCheck );
                int level_id =  CursorCheck.getInt(3);

                if( level_id != -1){
                    if(triggerNameCheck.equals(name)){
                        result = true;
                    }
                }
            }
        }
        Log.d(TAG, "result : " + result);
        return result;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
