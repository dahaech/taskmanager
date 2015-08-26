package com.example.dahae.myandroiice.Records;

/**
 * Created by Dahae on 2015-07-23.
 */
public class RecordItem {

    String RecordName;
    String RecordTime;

    public RecordItem() {
    }

    public RecordItem(String recordName, String recordTime) {
        RecordName = recordName;
        RecordTime = recordTime;
    }

    public String getRecordName() {
        return RecordName;
    }

    public void setRecordName(String recordName) {
        RecordName = recordName;
    }

    public String getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(String recordTime) {
        RecordTime = recordTime;
    }
}


