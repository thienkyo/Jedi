package com.rmc.thienle.jedi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by thienle on 4/17/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "MYDB.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedEntry.COLUMN_NAME_SWITCH_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ISACTIVE + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ENTRY_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_START_HR + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_START_MIN + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_END_HR + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_END_MIN + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_MON + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TUE + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_WEN + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_THU + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_FRI + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_SAT + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_SUN + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_RELAYID + INTEGER_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    //private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertEntry  (String switch_name, int isactive, String entry_name, int start_hr,int start_min,int end_hr,int end_min,
                                 int mon, int tue, int wen, int thu, int fri, int sat, int sun, int date, int relayid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_NAME_SWITCH_NAME, switch_name);
        contentValues.put(FeedEntry.COLUMN_NAME_ISACTIVE, isactive);
        contentValues.put(FeedEntry.COLUMN_NAME_ENTRY_NAME, entry_name);
        contentValues.put(FeedEntry.COLUMN_NAME_START_HR, start_hr);
        contentValues.put(FeedEntry.COLUMN_NAME_START_MIN, start_min);
        contentValues.put(FeedEntry.COLUMN_NAME_END_HR, end_hr);
        contentValues.put(FeedEntry.COLUMN_NAME_END_MIN, end_min);
        contentValues.put(FeedEntry.COLUMN_NAME_MON, mon);
        contentValues.put(FeedEntry.COLUMN_NAME_TUE, tue);
        contentValues.put(FeedEntry.COLUMN_NAME_WEN, wen);
        contentValues.put(FeedEntry.COLUMN_NAME_THU, thu);
        contentValues.put(FeedEntry.COLUMN_NAME_FRI, fri);
        contentValues.put(FeedEntry.COLUMN_NAME_SAT, sat);
        contentValues.put(FeedEntry.COLUMN_NAME_SUN, sun);
        contentValues.put(FeedEntry.COLUMN_NAME_DATE, date);
        contentValues.put(FeedEntry.COLUMN_NAME_RELAYID, relayid);

        return db.insert(FeedEntry.TABLE_NAME, null, contentValues) > 0;
    }

    public Integer deleteEntry (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(id != 0){
        return db.delete(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_NAME_ENTRY_ID + " = ? ", new String[] { Integer.toString(id) });
        }else {
            return db.delete(FeedEntry.TABLE_NAME, null, null);
        }
    }

    public Cursor getEntry(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + FeedEntry.TABLE_NAME + " where " + FeedEntry.COLUMN_NAME_ENTRY_ID + "=" + id, null);
        return res;
    }

    public boolean updateEntry (Integer entry_id, String switch_name, int isactive, String entry_name, int start_hr,int start_min,int end_hr,int end_min,
                                int mon, int tue, int wen, int thu, int fri, int sat, int sun, int date, int relayid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_NAME_SWITCH_NAME, switch_name);
        contentValues.put(FeedEntry.COLUMN_NAME_ISACTIVE, isactive);
        contentValues.put(FeedEntry.COLUMN_NAME_ENTRY_NAME, entry_name);
        contentValues.put(FeedEntry.COLUMN_NAME_START_HR, start_hr);
        contentValues.put(FeedEntry.COLUMN_NAME_START_MIN, start_min);
        contentValues.put(FeedEntry.COLUMN_NAME_END_HR, end_hr);
        contentValues.put(FeedEntry.COLUMN_NAME_END_MIN, end_min);
        contentValues.put(FeedEntry.COLUMN_NAME_MON, mon);
        contentValues.put(FeedEntry.COLUMN_NAME_TUE, tue);
        contentValues.put(FeedEntry.COLUMN_NAME_WEN, wen);
        contentValues.put(FeedEntry.COLUMN_NAME_THU, thu);
        contentValues.put(FeedEntry.COLUMN_NAME_FRI, fri);
        contentValues.put(FeedEntry.COLUMN_NAME_SAT, sat);
        contentValues.put(FeedEntry.COLUMN_NAME_SUN, sun);
        contentValues.put(FeedEntry.COLUMN_NAME_DATE, date);
        contentValues.put(FeedEntry.COLUMN_NAME_RELAYID, relayid);
//        int a = db.update(FeedEntry.TABLE_NAME, contentValues, FeedEntry.COLUMN_NAME_ENTRY_ID + " = ? ", new String[] { Integer.toString(entry_id) });
//        Log.e(TAG, "update a= "+ a);
//        Log.e(TAG, "update  entry name= "+ entry_name);
//        Log.e(TAG, "update  id= "+ entry_id);
//        return  a>0;
        return db.update(FeedEntry.TABLE_NAME, contentValues, FeedEntry.COLUMN_NAME_ENTRY_ID + " = ? ", new String[] { Integer.toString(entry_id) }) > 0;
    }

    public ArrayList<Entry> getAllEntries()
    {
        ArrayList<Entry> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+FeedEntry.TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Entry(res.getString(res.getColumnIndex(FeedEntry.COLUMN_NAME_ENTRY_NAME)),
                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_START_HR)),
                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_START_MIN)),
                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_END_HR)),
                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_END_MIN)),
                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_ENTRY_ID))
                    ));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Entry> getFullEntries()
    {
        ArrayList<Entry> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+FeedEntry.TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new Entry(res.getString(res.getColumnIndex(FeedEntry.COLUMN_NAME_ENTRY_NAME)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_ISACTIVE)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_START_HR)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_START_MIN)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_END_HR)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_END_MIN)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_MON)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_TUE)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_WEN)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_THU)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_FRI)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_SAT)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_SUN)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_DATE)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_RELAYID)),
                                    res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_ENTRY_ID))
            ));
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList<Integer> getAllEntry_id()
    {
        ArrayList<Integer> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+FeedEntry.TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getInt(res.getColumnIndex(FeedEntry.COLUMN_NAME_ENTRY_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
        public static final String COLUMN_NAME_SWITCH_NAME = "switch_name";
        public static final String COLUMN_NAME_ISACTIVE = "isactive";
        public static final String COLUMN_NAME_ENTRY_NAME = "entry_name";
        public static final String COLUMN_NAME_START_HR = "start_hr";
        public static final String COLUMN_NAME_START_MIN = "start_min";
        public static final String COLUMN_NAME_END_HR = "end_hr";
        public static final String COLUMN_NAME_END_MIN = "end_min";
        public static final String COLUMN_NAME_MON = "mon";
        public static final String COLUMN_NAME_TUE = "tue";
        public static final String COLUMN_NAME_WEN = "wen";
        public static final String COLUMN_NAME_THU = "thu";
        public static final String COLUMN_NAME_FRI = "fri";
        public static final String COLUMN_NAME_SAT = "sat";
        public static final String COLUMN_NAME_SUN = "sun";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_RELAYID = "relayid";

    }

}
