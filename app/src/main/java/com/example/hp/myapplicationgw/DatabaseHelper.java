package com.example.hp.myapplicationgw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Thevaki on 4/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="CoolYourCar.db";
    public static final String TABLE_NAME="cool";
    public static final String COL_1="ID";
    public static final String COL_2="DATE";
    public static final String COL_3="ON_TIME";
    public static final String COL_4="ON_TEMP";
    public static final String COL_5="OFF_TIME";
    public static final String COL_6="OFF_TEMP";

    public DatabaseHelper(Context context) { //whenever this constructor is called a database will be created
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY,DATE TEXT,ON_TIME TEXT,ON_TEMP TEXT,OFF_TIME TEXT,OFF_TEMP TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String date,String onTime,String onTemp,String offTime,String offTemp){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("DATE",date);
        contentValues.put("ON_TIME",onTime);
        contentValues.put("ON_TEMP",onTemp);
        contentValues.put("OFF_TIME",offTime);
        contentValues.put("OFF_TEMP",offTemp);
        long result=db.insert(TABLE_NAME,null,contentValues);

        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor viewData(String date){//to provide random read access
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME+" where "+COL_2+" = "+date,null);
        return res;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[] {id});
    }

    public void deleteAllInDate(String date){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_2 + "= '" + date + "'");
    }

    public void deleteAll(String date){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
