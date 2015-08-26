package com.example.dahae.myandroiice.Records;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.dahae.myandroiice.ExistingPlans.ActivePlanActivity;
import com.example.dahae.myandroiice.ExistingPlans.SleepingPlanActivity;
import com.example.dahae.myandroiice.ExistingPlans.WholePlanActivity;
import com.example.dahae.myandroiice.Function.MyTrigger;
import com.example.dahae.myandroiice.NewPlan.NewPlanActivity;
import com.example.dahae.myandroiice.R;
import com.example.dahae.myandroiice.Records.RecordItem;
import com.example.dahae.myandroiice.Records.RecordItemView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ListView RecordListView;
    RecordAdapter adapter;

    String[] PlanName = {"학교 도착하면 Wifi 켜기","도서관 도착하면 숙명어플 사용하기",
            "이어폰 연결하면 볼륨 15로","충전기 꽂으면 화면 유지 10분",
            "집 도착시 와이파이 켜기","배터리 30% 이하면 와이파이 끄기"};
    String[] PlanTime = {"2015/7/23 15:37:29","2015/7/23 15:37:29","2015/7/23 15:37:29",
            "2015/7/23 15:37:29","2015/7/23 15:37:29","2015/7/23 15:37:29"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecordListView  = (ListView) findViewById(R.id.recordListView);
        adapter = new RecordAdapter();

        adapter.addItem(new RecordItem(PlanName[0], PlanTime[0]));
        adapter.addItem(new RecordItem(PlanName[1], PlanTime[1]));
        adapter.addItem(new RecordItem(PlanName[2], PlanTime[2]));
        adapter.addItem(new RecordItem(PlanName[3], PlanTime[3]));
        adapter.addItem(new RecordItem(PlanName[4], PlanTime[4]));
        adapter.addItem(new RecordItem(PlanName[5], PlanTime[5]));

        RecordListView.setAdapter(adapter);

    }


    public void onActivePlanLayoutClicked(View v){
        Intent intentActivPlans = new Intent(getApplicationContext(), ActivePlanActivity.class);
        startActivity(intentActivPlans);
    }

    public void onButtonWholePlanClicked(View v){
        Intent intentWholePlans = new Intent(getApplicationContext(), WholePlanActivity.class);
        startActivity(intentWholePlans);
    }

    public void onButtonSleepingPlanClicked(View v){
        Intent intentSleepingPlans = new Intent(getApplicationContext(), SleepingPlanActivity.class);
        startActivity(intentSleepingPlans);
    }


    //최근 기록 보여주는 리스트뷰용 아답터
    class RecordAdapter extends BaseAdapter{

        ArrayList<RecordItem> items  = new ArrayList<RecordItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(RecordItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RecordItemView view = null;

            if(convertView == null) {
                view = new RecordItemView(getApplicationContext());
            } else {
                view = (RecordItemView) convertView;
            }

            RecordItem curItem = items.get(position);

            view.setRecordName(curItem.getRecordName());
            view.setRecordTime(curItem.getRecordTime());

            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        } else if (id == R.id.action_new){    //액션바 "NEW"버튼 (새 플랜 만들기)
            Intent intentPlusButton = new Intent(getApplicationContext(), NewPlanActivity.class);
            startActivity(intentPlusButton);
            return true;

        } else if (id == R.id.action_restart_service) {
            Intent ServiceIntent = new Intent(getApplicationContext(), MyTrigger.class);
            startService(ServiceIntent);
            return true;

        } else if (id == R.id.action_stop_service) {
            Intent ServiceIntent = new Intent(getApplicationContext(), MyTrigger.class);
            stopService(ServiceIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
