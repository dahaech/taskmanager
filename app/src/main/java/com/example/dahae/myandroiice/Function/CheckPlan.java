package com.example.dahae.myandroiice.Function;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dahae on 2015-08-24.
 */
public class CheckPlan extends Service {


    private static final String TAG = "[ANDROI-ICE]";

    public String tableNameDB;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "hi  CheckPlan ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String query = intent.getStringExtra("query");
        String Complex = intent.getStringExtra("Complex");
        int i = intent.getIntExtra("i", 0);

        checkPlan(i, query, Complex);
        return super.onStartCommand(intent, flags, startId);
    }


    //checking Table name
    public void checkPlan(int i, String query, String Complex){

        DBHelper mHelperInComplexPlan = new DBHelper(this);
        SQLiteDatabase database = mHelperInComplexPlan.getWritableDatabase();
        Cursor cursorT = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        Log.d(TAG, "THE NUMBER OF TABLE : " + cursorT.getCount());
        if (cursorT.moveToFirst()) {
            while ( !cursorT.isAfterLast() ) {
                tableNameDB = cursorT.getString(0);

                query = "SELECT * FROM " + tableNameDB + " where level_id = " + i;
                Log.d(TAG, query);

                if(!tableNameDB.equals("android_metadata")&& !tableNameDB.equals("sqlite_sequence")) {
                    ComplexPlan(i, query, Complex);
                }
                cursorT.moveToNext();
            }
        }
    }


    //checking ComplexPlan or simplePlan
    public boolean ComplexPlan(int i, String query , String Complex) {

        //  List listEnd = new ArrayList();
        List listDone = new ArrayList();

        boolean result = false;
        Log.d(TAG, "hi Complex");
        DBHelper mHelperForComplex = new DBHelper(this);
        SQLiteDatabase dbForComplex = mHelperForComplex.getWritableDatabase();



        Cursor cursorForComplex = dbForComplex.rawQuery(query, null);
        Cursor cursorForComplex2 = dbForComplex.rawQuery(query, null);
        cursorForComplex2.moveToFirst();

        Log.d(TAG, " # cursorforComplex.getCount() ;" + cursorForComplex.getCount());
        Log.d(TAG, " # cursorForComplex2.getCount() ;" + cursorForComplex2.getCount());

        for (int j = 0; j <cursorForComplex.getCount(); j++) {

            if(!cursorForComplex2.moveToNext()){
                cursorForComplex2.moveToLast();
            }

            if (cursorForComplex.moveToNext() ) {
                int idNumberForComplex = cursorForComplex.getInt(0);
                String triggerNameForComplex = cursorForComplex.getString(1);
                int level_idForComplex = cursorForComplex.getInt(3);
                int idNumberForComplex_Next = cursorForComplex2.getInt(0);

                //Log.d(TAG, " # j =>" + j + " ;  cursorForComplex.getCount() ;" +  cursorForComplex.getCount());
                //Log.d(TAG, " # paret_id = " + i + " =>" + idNumberForComplex + " ;" + triggerNameForComplex + " ;" + idNumberForComplex_Next);

                if (triggerNameForComplex.equals("And")) { //1st cursor의 trigger받기

                    Log.d(TAG, "**And IN DB ;" + level_idForComplex); //and에 들어왓다

                    i++; //트리의 다음 depth
                    query = "SELECT * FROM " + tableNameDB + " where _id between " + idNumberForComplex + " and " + idNumberForComplex_Next +" and level_id = " + i ; //두 커서 사이의 levelid가 i인것을 찾아라
                    Log.d(TAG, " # SELECT * FROM " + tableNameDB + " where _id between " + idNumberForComplex + " and " + idNumberForComplex_Next +" and level_id = " + i );

                    Log.d(TAG, " IN AND /  i " + i);
                    Complex = "And";

                    result = ComplexPlan(i, query, Complex); //0과 0사이의 1을 찾아라 -> 1과 1사이의 2를 찾아라 ->
                    i--;
                    listDone.add(result);

                    Log.d(TAG, " OUT result ; " + result + "/ i  " + i);
                    Complex = "And";

                } else if (triggerNameForComplex.equals("Or")) {

                    Log.d(TAG, "**Or IN DB ;" + level_idForComplex);

                    i++;
                    query = "SELECT * FROM " + tableNameDB + " where _id between " + idNumberForComplex + " and " + idNumberForComplex_Next +" and level_id = " + i ;
                    Log.d(TAG, " # SELECT * FROM " + tableNameDB + " where _id between " + idNumberForComplex + " and " + idNumberForComplex_Next +" and level_id = " + i );

                    Log.d(TAG, " IN Or / i " + i);
                    Complex = "Or";

                    result = ComplexPlan(i, query, Complex);
                    i--;
                    listDone.add(result);

                    Log.d(TAG, " OUT result ; " + result+ "/ i " + i);
                    Complex = "Or";

                } else if (triggerNameForComplex.equals("Done")) {

                    Log.d(TAG, "**Done");
                    Iterator iterator = listDone.iterator();
                    if (Complex.equals("And")) {

                        while (iterator.hasNext()) {
                            Boolean and = (Boolean) iterator.next();
                            Log.d(TAG, "@ "+and );
                            if (and) {
                                result = true;
                            } else {
                                result = false;
                                break;
                            }
                        }
                        if(i != 0){
                            return result;
                        }
                    } else if (Complex.equals("Or")) {
                        while (iterator.hasNext()) {
                            Boolean or = (Boolean) iterator.next();
                            Log.d(TAG, "@ "+or );

                            if (or) {
                                result = true;
                                break;
                            } else {
                                result = false;

                            }
                        }
                        if(i != 0){
                            return result;
                        }
                    }else{
                        if(iterator.hasNext()){

                            if((Boolean)iterator.next()){
                                result = true;
                            }
                            else{
                                result = false;
                            }
                        }
                        listDone.add(result);
                    }


                }else if (triggerNameForComplex.equals("End")) {
                    Iterator iterator = listDone.iterator();
                    Log.d(TAG, "**End");

                    if (Complex.toString().equals("And")) {
                        //    Log.d(TAG, "hi and" );
                        while (iterator.hasNext()) {
                            Boolean and = (Boolean) iterator.next();
                            //  Log.d(TAG, "@ "+and );
                            if (and) {
                                result = true;
                            } else {
                                result = false;
                                break;
                            }
                        }

                    } else if (Complex.toString().equals("Or")) {
                        //   Log.d(TAG, "hi or" );

                        while (iterator.hasNext()) {
                            Boolean or = (Boolean) iterator.next();
                            Log.d(TAG, "@ "+ or );
                            if (or) {
                                result = true;
                                break;
                            } else {
                                result = false;

                            }
                        }
                    }

                    listDone.clear();
                    Log.d(TAG, "***FINAL End Result : " + result);

                    if(result == true){

                        //action 시작
                        Log.d(TAG, "**hi Action");
                        query = "SELECT * FROM " + tableNameDB + " where level_id = -1";
                        Cursor cursorForActionIntent = dbForComplex.rawQuery(query, null);
                        cursorForActionIntent.moveToFirst();

                        String actionForIntent = cursorForActionIntent.getString(2);

                        Log.d(TAG, "* actionForIntent / "+  actionForIntent);
                        Intent action = new Intent(this, MyAction.class);
                        action.putExtra("actionName", actionForIntent);
                        startService(action);

                    }
                    return result;

                }else{ //and, or, end, done아닌것들->트리거
                    //여기서 and or 결과값 구해서 리턴하기
                    Log.d(TAG, "** trigger");
                    if (checkTrigger(idNumberForComplex, tableNameDB)){// || resultInBroadcast) {

                        Log.d(TAG, "** true" + triggerNameForComplex + "IN DB ;" + level_idForComplex);
                        result = true;

                    } else {
                        Log.d(TAG, "** false" + triggerNameForComplex + "IN DB ;" + level_idForComplex);
                        result = false;
                    }
                    listDone.add(result);
                }
            }

        }

        return result;
    }

    //Checking Trigger
    public boolean checkTrigger(int num, String tableNameDB){

        boolean result =false;

        Cursor PreviousCursorForComplex;

        String triggerName, actionName;
        int  level_ID;
        float result_Brightness ;

        DBHelper mHelperForComplex = new DBHelper(this);
        SQLiteDatabase dbForComplex = mHelperForComplex.getWritableDatabase();
        PreviousCursorForComplex = dbForComplex.rawQuery("SELECT * FROM " + tableNameDB + " where _id =" + num , null);

        Log.d(TAG, "hi checkComplex");
        for (int j = 0 ; j < PreviousCursorForComplex.getCount(); j++) {

            if (PreviousCursorForComplex.moveToNext()) {
                // int idNumber = PreviousCursorForComplex.getInt(0);
                triggerName =PreviousCursorForComplex.getString(1);
                actionName =PreviousCursorForComplex.getString(2);
                level_ID = PreviousCursorForComplex.getInt(3);

                Log.d(TAG, "checkComplex() * DB list " + triggerName + " / "+ actionName +" / "+ level_ID);

                Intent BroadcastIntent = new Intent(this, MyAction.class);

                // Local Bluetooth adapter
                BluetoothAdapter mBluetoothAdapter =   BluetoothAdapter.getDefaultAdapter();
                //Sound and Vibration and Silence  detecting and setting
                AudioManager aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                try {
                    //wifi and data detecting
                    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    //data on off setting
                    final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    final Class conmanClass = Class.forName(conman.getClass().getName());
                    final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
                    connectivityManagerField.setAccessible(true);
                    final Object connectivityManager = connectivityManagerField.get(conman);
                    final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
                    final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                    setMobileDataEnabledMethod.setAccessible(true);

                    if (triggerName.equals("WifiOn")) {
                        if (wifi.isConnected() == true) {
                            result = true;
                        }
                    } else if (triggerName.equals("WifiOff")) {
                        if (wifi.isConnected() == false) {
                            result = true;
                        }
                    } else if (triggerName.equals("Sound")) {
                        if (aManager.getRingerMode() == 2) {//2 ; Sound
                            result = true;
                        }
                    } else if (triggerName.equals("Vibration")) {
                        if (aManager.getRingerMode() == 1) {//1 ; Vibration
                            result = true;
                        }
                    } else if (triggerName.equals("Silence")) {
                        if (aManager.getRingerMode() == 0) {//0 ; Silence
                            result = true;
                        }
                    } else if (triggerName.equals("DataOn")) {
                        if (mobile.isConnected() == true) {
                            result = true;
                        }
                    } else if (triggerName.equals("DataOff")) {
                        if (mobile.isConnected() == false) {
                            result = true;
                        }
                    } else if (triggerName.equals("BluetoothOn")) {
                        if (mBluetoothAdapter.isEnabled() == true) {
                            result = true;
                        }
                    } else if (triggerName.equals("BluetoothOff")) {
                        if (mBluetoothAdapter.isEnabled() == false) {
                            result = true;
                        }
                    } else if (triggerName.equals("BrightnessUp")) {
                        result_Brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                        if (result_Brightness > 200) {
                            result = true;
                        }
                    } else if (triggerName.equals("BrightnessDown")) {
                        result_Brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                        if (result_Brightness < 30) {
                            result = true;
                        }
                    }else if (triggerName.equals("AirplaneModeOn")) {
                        boolean isEnabled = Settings.System.getInt(this.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                        if (isEnabled == true) {
                            result = true;
                        }
                    }else if (triggerName.equals("AirplaneModeOff")) {
                        boolean isEnabled = Settings.System.getInt(this.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                        if (isEnabled == false){
                            result = true;
                        }
                    }else if (triggerName.equals("LowBattery")) {

                        Intent batteryStatus = this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                        float batteryPct = level / (float)scale;
                        int battery = (int)(100 * batteryPct);
                        Log.d(TAG, "LowBattery  " + batteryPct);
                        if(battery < 20){
                            result = true;
                        }
                    }else if (triggerName.equals("FullBattery")) {

                        Intent batteryStatus = this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                        float batteryPct = level / (float)scale;
                        int battery = (int)(100 * batteryPct);
                        Log.d(TAG, "FullBattery  " + batteryPct);
                        if(battery > 80){
                            result = true;
                        }
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        return result;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}



