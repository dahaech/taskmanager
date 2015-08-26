package com.example.dahae.myandroiice.Function;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JJong on 2015-08-03.
 */
public class DBHelper extends SQLiteOpenHelper {

    static String TAG= "[ANDROI-ICE]";

    public DBHelper(Context context) {
        super(context, "TriggerDatabase", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //changeTable(db);
    }

    public void createTable(SQLiteDatabase db){

        /*try{
            if(db != null){

                db.execSQL("CREATE TABLE if not exists "+ tablename +" ("
                                + "_id integer primary KEY autoincrement,"
                                + "triggerName text,"
                                + "actionName text,"
                                + "level_id integer "
                                + ")" );
                Log.d(TAG, "Table one created");
            }else{

                Log.d(TAG, "table does not exist");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }*/
    }

}
