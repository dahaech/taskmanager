package com.example.dahae.myandroiice.Records;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dahae.myandroiice.R;

/**
 * Created by Dahae on 2015-07-23.
 */
public class RecordItemView extends LinearLayout {

    TextView recordNameTextView;
    TextView recordTimeTextView;


    public RecordItemView(Context context) {
        super(context);

        init(context);
    }

    public RecordItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init (Context context){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.record_item, this, true);

        recordNameTextView = (TextView) findViewById(R.id.recordNameTextView);
        recordTimeTextView = (TextView) findViewById(R.id.recordTimeTextView);
    }


   public void setRecordName(String recordPlanName) {
       recordNameTextView.setText(recordPlanName);
   }

    public void setRecordTime(String recordPlanTime) {
        recordTimeTextView.setText(recordPlanTime);
    }

}
