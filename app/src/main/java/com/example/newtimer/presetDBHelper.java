package com.example.newtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class presetDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME ="tbl_Presets";
    public static final String COL1 = "PreHrs";
    public static final String COL2 = "PreMins";
    public static final String COL3 = "PreSecs";

    public presetDBHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 +" INTEGER, "+ COL2 + " INTEGER, "+ COL3 + " INTEGER)";
        DB.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(DB);
    }

    public boolean addPreset(int PreHrs, int PreMins, int PreSecs){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv =new ContentValues();
        cv.put(COL1, PreHrs);
        cv.put(COL2, PreMins);
        cv.put(COL3, PreSecs);

        long result =DB.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public ArrayList<PresetModel> getPresets(){
        ArrayList<PresetModel> arrayList = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor data = DB.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        while(data.moveToNext()){
            int ID = data.getInt(0);
            int prehrs = data.getInt(1);
            int premins = data.getInt(2);
            int presecs = data.getInt(3);
            PresetModel presetModel = new PresetModel(ID, prehrs, premins, presecs);
            arrayList.add(presetModel);
        }
        return arrayList;
    }
    public Cursor getPresetID(String presetID) {
        SQLiteDatabase DB= this.getWritableDatabase();
        Cursor cursor =DB.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE ID = '"+ presetID +"' ", null);
        return cursor;
}
    public boolean deletePreset(String presetID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE ID = ?", new String[]{presetID});
        if(cursor.getCount() > 0){
            long result= DB.delete(TABLE_NAME, "ID = ?", new String[]{presetID});
            if(result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }
}
