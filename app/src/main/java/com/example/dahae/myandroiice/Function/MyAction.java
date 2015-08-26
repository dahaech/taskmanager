package com.example.dahae.myandroiice.Function;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

public class MyAction extends Service {

    String triggerName;

    private static final String TAG = "[ANDROI-ICE]";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "HI SERVICE");

        triggerName = intent.getStringExtra("triggerName");
        String actionNameFromIntent = intent.getStringExtra("actionName");

        if ( actionNameFromIntent.contains("/")) {
            StringTokenizer st = new StringTokenizer(actionNameFromIntent, "/");
            while (st.hasMoreTokens()) {
                Action(st.nextToken());
            }
        }
        Log.d(TAG, "stopService");
        stopService(intent);

        return super.onStartCommand(intent, flags, startId);
    }


    public void Action(String actionName) {

        Log.d(TAG, "In Action() : " + actionName);

        // Bluetooth - Local Bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Mode - Sound,Vibration,Silence - detecting & setting
        AudioManager aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        try {
            // Wifi, Data detecting
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            // Data-on/off setting
            final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
            connectivityManagerField.setAccessible(true);
            final Object connectivityManager = connectivityManagerField.get(conman);
            final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            /**
             *   Do Action
             */
            if (actionName.toString().equals("WifiOn")) {
                setWiFi(true);

            } else if (actionName.toString().equals("WifiOff")) {
                setWiFi(false);

            } else if (actionName.toString().equals("Sound")) {
                aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            } else if (actionName.toString().equals("Vibration")) {
                Log.d(TAG,"Vibration");
                aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            } else if (actionName.toString().equals("Silence")) {
                aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            } else if (actionName.toString().equals("DataOn")) {
                setMobileDataEnabledMethod.invoke(connectivityManager, true);

            } else if (actionName.toString().equals("DataOff")) {
                setMobileDataEnabledMethod.invoke(connectivityManager, false);

            } else if (actionName.toString().equals("BluetoothOn")) {
                if (mBluetoothAdapter != null) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();     }
                }
            } else if (actionName.toString().equals("BluetoothOff")) {
                if (mBluetoothAdapter != null) {
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();   }
                }
            } else if (actionName.toString().equals("AirplaneModeOn")) {
                Log.d(TAG, "AirplaneModeOn");
                modifyAirplanemode(true);

            } else if (actionName.toString().equals("AirplaneModeOff")) {
                Log.d(TAG, "AirplaneModeOff");
                modifyAirplanemode(false);
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    // Wifi - Setting
    public void  setWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public int getWiFi() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        int result;
        result = wifiManager.getWifiState(); //1 off ; 3 on
        return result;
    }

    public void modifyAirplanemode(boolean mode) {

        Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, mode ? 1 : 0);// Turning ON/OFF Airplane mode.

        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);// creating intent and Specifying action for AIRPLANE mode.
        intent.putExtra("state", !mode);// indicate the "state" of airplane mode is changed to ON/OFF

        sendBroadcast(intent); //안됨?

    }

}
