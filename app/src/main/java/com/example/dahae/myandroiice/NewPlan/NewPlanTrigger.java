package com.example.dahae.myandroiice.NewPlan;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dahae.myandroiice.R;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dahae on 2015-07-27.
 */

public class NewPlanTrigger extends ListFragment {

    static String TAG= "[ANDROI-ICE]";

    int fragNum;

    Button buttonAnd;
    Button buttonOr;
    Button buttonDone;
    Button buttonTriggerEnd;

    List<String> listTrigger = new ArrayList<String>();

    String triggerName = "";
    public String triggerName_beforeAdding;


    String arr1[] = { "Wi-Fi 켜짐", "Wi-Fi 꺼짐", "화면 켜짐", "화면 꺼짐", "소리모드", "진동모드", // 6
            "무음모드", "데이터네트워크 켜짐", "데이터네트워크 꺼짐", "블루투스 켜짐", "블루투스 꺼짐", // 7-11
            "화면 밝게 했을 때", "화면 어둡게 했을 때", "SMS 수신시", "비행기모드(*)", "전화 발신시", // 12-16
            "충전기 연결시", "충전기 해제시", "이어폰 연결시", "이어폰 연결 해제시", "부팅시" }; // 17-21


    static NewPlanTrigger init(int val) {
        NewPlanTrigger fragment = new NewPlanTrigger();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Retrieving this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fragNum = getArguments() != null ? getArguments().getInt("val") : 1;

    }


    /**
     * The Fragment's UI is a list.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.new_plan_trigger, null);

        buttonOr = (Button) layoutView.findViewById(R.id.buttonOr);
        buttonAnd = (Button) layoutView.findViewById(R.id.buttonAnd);
        buttonDone = (Button) layoutView.findViewById(R.id.buttonDone);
        buttonTriggerEnd = (Button) layoutView.findViewById(R.id.buttonTriggerEnd);

        buttonOr.setOnClickListener(l);
        buttonAnd.setOnClickListener(l);
        buttonDone.setOnClickListener(l);
        buttonTriggerEnd.setOnClickListener(l);

        return layoutView;
    }


    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.buttonOr){
                listTrigger.add(triggerName);
                listTrigger.add("Or");
                triggerName="";
            }
            else if(v.getId()==R.id.buttonAnd){
                listTrigger.add(triggerName);
                listTrigger.add("And");
                triggerName="";
            }
            else if(v.getId()==R.id.buttonDone){
                listTrigger.add(triggerName);
                listTrigger.add("Done");
                triggerName = "";
            }
            else if(v.getId()==R.id.buttonTriggerEnd){
                listTrigger.add(triggerName);
                NewPlanActivity newPlanActivity = (NewPlanActivity) getActivity();
                newPlanActivity.mlistTrigger = listTrigger;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
          if(fragNum == 0) {
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, arr1));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG, "FragmentList  pageNum: " + fragNum + " Item clicked: " + position);

        /**
         * 트리거 설정
         */
        if(fragNum == 0){
            if (position == 0){
                //와이파이 켜짐
                triggerName_beforeAdding = "WifiOn";
            }else if(position == 1){
                //와이파이 꺼짐
                triggerName_beforeAdding = "WifiOff";
            }else if(position == 2){
                //화면 켜짐
                triggerName_beforeAdding = "ScreenOn";
            }else if(position == 3){
                //화면 꺼짐
                triggerName_beforeAdding = "ScreenOff";
            }else if(position == 4){
                //소리모드
                triggerName_beforeAdding = "Sound";
            }else if(position == 5){
                //진동모드
                triggerName_beforeAdding = "Vibration";
            }else if(position == 6){
                //무음모드
                triggerName_beforeAdding = "Silence";
            }else if(position == 7){
                //데이터네트워크 켜짐
                triggerName_beforeAdding = "DataOn";
            }else if(position == 8){
                //데이터네트워크 꺼짐
                triggerName_beforeAdding = "DataOff";
            }else if(position == 9){
                //블루투스 켜짐
                triggerName_beforeAdding = "BluetoothOn";
            }else if(position == 10){
                //블루투스 꺼짐
                triggerName_beforeAdding = "BluetoothOff";
            } else if (position == 11) {
                //화면 밝게
                triggerName_beforeAdding = "BrightnessUp";
            } else if (position == 12) {
                //화면 어둡
                triggerName_beforeAdding = "BrightnessDown";
            } else if (position == 13) {
                //문자 수신시
                triggerName_beforeAdding = "SMSreceiver";
            } else if (position == 14) {
                //비행기모드 바꼈을때
                triggerName_beforeAdding = "AirplaneModeChanged";
            } else if (position == 15) {
                //전화 발신
                triggerName_beforeAdding = "NewOutgoingCall";
            } else if (position == 16) {
                //전원 연결시
                triggerName_beforeAdding = "PowerConnected";
            } else if (position == 17) {
                //전원 해제시
                triggerName_beforeAdding = "PowerDisConnected";
            } else if (position == 18) {
                //이어폰 연결시
                triggerName_beforeAdding = "EarphoneIn";
            } else if (position == 19) {
                //이어폰 해제시
                triggerName_beforeAdding = "EarphoneOut";
            } else if (position == 20) {
                //부팅시
                triggerName_beforeAdding = "Booted";
            } else if (position == 21) {
                triggerName_beforeAdding = "LowBattery";
            } else if (position == 22) {
                triggerName_beforeAdding = "FullBattery";
            }

            triggerName += triggerName_beforeAdding+"/";

        }
    }
}