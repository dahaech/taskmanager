package com.example.dahae.myandroiice.NewPlan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dahae.myandroiice.Function.DBHelper;
import com.example.dahae.myandroiice.Function.MyTrigger;
import com.example.dahae.myandroiice.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dahae on 2015-07-27.
 */

public class NewPlanAction extends ListFragment {

    static String TAG= "[ANDROI-ICE]";

    int fragNum;

    Button buttonActionEnd;
//
//    public NewPlanTrigger newPlanTrigger;
//    public NewPlanAction newPlanAction;
//
//    String mTriggerName;
//    String mActionName;
//
//    public SQLiteDatabase database;
//    Cursor cursor;

    String actionName = "";
    public String actionName_beforeAdding;

    String arr2[] = { "Wi-Fi 켜기", "Wi-Fi 끄기", "소리모드로 전환", "진동모드로 전환", // 4
            "무음모드로 전환", "데이터네트워크 켜기", "데이터네트워크 끄기", "블루투스 켜기", // 4-8
            "블루투스 끄기" }; // 9


    static NewPlanAction init(int val) {
        NewPlanAction fragment = new NewPlanAction();
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
        View layoutView = inflater.inflate(R.layout.new_plan_action, null);

        buttonActionEnd = (Button) layoutView.findViewById(R.id.buttonActionEnd);

        buttonActionEnd.setOnClickListener(l);

        return layoutView;
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           if(v.getId()==R.id.buttonActionEnd){
               NewPlanActivity newPlanActivity = (NewPlanActivity) getActivity();
               newPlanActivity.mActionName = actionName;
            }
        }
    };


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            if (fragNum == 1) {
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, arr2));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "pageNum: " + fragNum + " Item clicked: " + position);

            /**
             * 액션 설정
             */
        if (fragNum == 1){
            if (position == 0){
                //와이파이 켜기
                actionName_beforeAdding = "WifiOn";
            } else if(position == 1){
                //와이파이 끄기
                actionName_beforeAdding = "WifiOff";
            } else if(position == 2){
                //화면 켜기
                actionName_beforeAdding = "Sound";
            } else if(position == 3){
                //화면 끄기
                actionName_beforeAdding = "Vibration";
            } else if(position == 4){
                //소리모드로 전환
                actionName_beforeAdding = "Silence";
            } else if(position == 5){
                //진동모드로 전환
                actionName_beforeAdding = "DataOn";
            } else if(position == 6){
                //무음모드로 전환
                actionName_beforeAdding = "DataOff";
            } else if(position == 7){
                //블루투스 켜기
                actionName_beforeAdding = "BluetoothOn";
            } else if(position == 8){
                //블루투스 끄기
                actionName_beforeAdding = "BluetoothOff";
            } else if (position == 11) {
                actionName_beforeAdding = "AirplaneModeOn";
            } else if (position == 12) {
                actionName_beforeAdding = "AirplaneModeOff";
            }

            actionName += actionName_beforeAdding+"/";
        }
    }
}