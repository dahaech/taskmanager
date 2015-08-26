package com.example.dahae.myandroiice.NewPlan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dahae.myandroiice.Function.MyTrigger;
import com.example.dahae.myandroiice.R;
import com.example.dahae.myandroiice.Function.DBHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


public class NewPlanActivity extends ActionBarActivity {

    static String TAG= "[ANDROI-ICE]";

    Button buttonTrigger;
    Button buttonAction;
    Button buttonConfig;
    Button buttonCheck;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    public NewPlanTrigger newPlanTrigger;
    public NewPlanAction newPlanAction;
    public NewPlanConfig newPlanConfig;

    String tableName;
    String mPlanName;
    String mActionName;

    Cursor cursor;
    public SQLiteDatabase database;
    public DBHelper databaseHelper;

    int level_id =0;

    List<String> mlistTrigger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        buttonTrigger = (Button) findViewById(R.id.buttonTrigger);
        buttonAction = (Button) findViewById(R.id.buttonAction);
        buttonConfig = (Button) findViewById(R.id.buttonConfig);
        buttonCheck =  (Button) findViewById(R.id.buttonCheck);

        buttonTrigger.setOnClickListener(l);
        buttonAction.setOnClickListener(l);
        buttonConfig.setOnClickListener(l);
        buttonCheck.setOnClickListener(l);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        /**
         *  TriggerDatabase DB 생성
         */
        try {
            databaseHelper = new DBHelper(getApplicationContext());
            database = databaseHelper.getWritableDatabase();
            Toast.makeText(this, "Database TriggerDatabase created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.buttonTrigger){
                mViewPager.setCurrentItem(0); }
            else if(v.getId()==R.id.buttonAction){
                mViewPager.setCurrentItem(1); }
            else if(v.getId()==R.id.buttonConfig){
                mViewPager.setCurrentItem(2); }
            else if(v.getId()==R.id.buttonCheck){
                checkDatabase();
            }
        }
    };


    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //해당하는 page의 fragment 생성
        @Override
        public Fragment getItem(int position) {                                                      Log.d(TAG, "NOW GENERATE FRAGMENTS");
            if (position == 0) {
                newPlanTrigger = NewPlanTrigger.init(position);
                return newPlanTrigger;
            } else if(position == 1){
                newPlanAction = NewPlanAction.init(position);
                return newPlanAction;
            } else {
                newPlanConfig = NewPlanConfig.init(position);
                return newPlanConfig;
            }
        }

        @Override
        public int getCount() {
            return 3;  // 총 3개의 page 사용
        }

    }

    public void saveDatabase(List mlistTrigger, String mActionName, String mPlanName){

        tableName = mPlanName;

        database.execSQL("CREATE TABLE " + tableName + "("
                + "_id integer primary KEY autoincrement,"
                + "triggerName text,"
                + "actionName text,"
                + "level_id integer "
                + ")" );                                                                            Log.d(TAG, "Table " + tableName + " created");

        //트리거 저장
        Iterator iterator = mlistTrigger.iterator();

        while (iterator.hasNext()){

            String trigger = (String)iterator.next();
            Log.d(TAG, trigger + " ;" + level_id);

            if (!trigger.contains("/")) {

                if (trigger.contains("And")) {
                    if (database != null) {
                        database.execSQL("INSERT INTO " + tableName + "(triggerName, level_id) values "
                                + "('And', " + level_id +");" );
                    } else{
                        Log.e(TAG, "first, you should open the table ");
                    }  level_id++;

                } else if (trigger.contains("Or")) {
                    if (database != null) {
                        database.execSQL("INSERT INTO " + tableName + "(triggerName, level_id) VALUES "
                                + "('Or', " + level_id + ");" );
                    } else{
                        Log.e(TAG, "first, you should open the table ");
                    }  level_id++;

                } else if (trigger.contains("Done")) {

                    database.execSQL("INSERT INTO " + tableName + "(triggerName, level_id) values "
                            + "('Done', " + level_id +");" );
                    level_id--;
                }
            }

            else if (trigger.contains("/")) {
                StringTokenizer st = new StringTokenizer(trigger, "/");
                while (st.hasMoreTokens()) {
                    if (database != null) {
                        database.execSQL("INSERT INTO " + tableName + "(triggerName, level_id) VALUES "
                                + "('" + st.nextToken() + "', " + level_id + ");" ); }
                }
            }
        }

        if (database != null) {
            database.execSQL("INSERT INTO " + tableName + "(triggerName, level_id) VALUES "
                    + "('Done', 0);" );
        }

        //트리거의 end인뎅 액션 먼저 넣어도 level_id가 달라서 상관없..
        if (database != null) {
            database.execSQL("INSERT INTO " + tableName + "(triggerName, level_id) VALUES "
                    + "('End', 0);" );
        }

        //액션 저장
        try {
            if (database != null) {
                Log.d(TAG, "I want to add");
                database.execSQL("INSERT INTO " + tableName + "(actionName, level_id) VALUES "
                        + "('" + mActionName + "', -1);" );
                Log.d(TAG, "add action in where level_id = -1 " + mActionName);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        //트리거 리스트,level_id 초기화해쥰당
        mlistTrigger.clear();
        newPlanTrigger.triggerName="";
        level_id =0;
    }


    public void checkDatabase(){
        Log.d(TAG, "hi checkDatabase");

        try {

            if (database != null) {

                // 모든 테이블 목록(플랜 목록) 보여주기
                Cursor cursorT = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

                Log.d(TAG, "THE NUMBER OF TABLE : " + cursorT.getCount());

                    while (cursorT.moveToNext()) {
                        if (!cursorT.getString(0).equals("android_metadata") && !cursorT.getString(0).equals("sqlite_sequence")) {

                        Log.d(TAG, "Table name is " + cursorT.getString(0));
                            //레코드 보여주기
                            cursor = database.rawQuery("SELECT * FROM " + cursorT.getString(0), null);
                            for (int i = 0; i < cursor.getCount(); i++) {
                                if (cursor.moveToNext()) {
                                    int _idDB = cursor.getInt(0);
                                    String triggerNameDB = cursor.getString(1);
                                    String actionNameDB = cursor.getString(2);
                                    int level_idDB = cursor.getInt(3);

                                    Log.d(TAG, "* _idDB : " + _idDB
                                            + " / level_idDB : " + level_idDB
                                            + " / DB trigger : " + triggerNameDB
                                            + " / DB action : " + actionNameDB);
                                }
                            }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "EVERY TABLE WAS SHOWN.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_save){

            mPlanName = newPlanConfig.planName.getText().toString();

            if(mPlanName == null) {
                Toast.makeText(this, "플랜 이름을 정해주세요.", Toast.LENGTH_LONG).show();
            }
            else {
                saveDatabase(mlistTrigger, mActionName, mPlanName);

                //잘 저장됐는지 로그로 확인합니다.
                checkDatabase();
                                                                                                 Log.d(TAG, "EVERY TABLE WAS SHOWN.");
                //저장과 함께 서비스를 시작합니다.
                Intent ServiceIntent = new Intent(getApplicationContext(), MyTrigger.class);
                startService(ServiceIntent);                                                        Log.d(TAG, "SERVICE STARTED");

                finish();
            }


        }

        else if (id == R.id.action_cancel){
            //만들던 플랜 취소! -> 한꺼번에 저장 안하다가 저장 버튼 누르면 저장해도 되고, 누르는대로 저장하다가 X눌렀을때 지우는 방법도???
        }

        return super.onOptionsItemSelected(item);
    }
}
