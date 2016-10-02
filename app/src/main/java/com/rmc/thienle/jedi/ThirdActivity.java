package com.rmc.thienle.jedi;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class ThirdActivity extends AppCompatActivity {
    private static final String TAG = ThirdActivity.class.getSimpleName();
    private ToggleButton mon_btn,tue_btn,wen_btn,thu_btn,fri_btn,sat_btn,sun_btn;
    private TextView from_textView, to_textView;
    private EditText task_name_editView;
    private int entry_id;
    DBHelper mydb;

    //SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mon_btn = (ToggleButton) findViewById(R.id.mon);
        tue_btn = (ToggleButton) findViewById(R.id.tue);
        wen_btn = (ToggleButton) findViewById(R.id.wen);
        thu_btn = (ToggleButton) findViewById(R.id.thu);
        fri_btn = (ToggleButton) findViewById(R.id.fri);
        sat_btn = (ToggleButton) findViewById(R.id.sat);
        sun_btn = (ToggleButton) findViewById(R.id.sun);
        from_textView = (TextView) findViewById(R.id.from);
        to_textView = (TextView) findViewById(R.id.to);
        task_name_editView = (EditText) findViewById(R.id.task_name);
        mydb = new DBHelper(this);

        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("MyPackage");
        entry_id = packageFromCaller.getInt("id", 0);
        Log.d(TAG, "load: " + entry_id);
        if (entry_id != 0) {
            loadInfo(entry_id);
        }
        from_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        from_textView.setText((selectedHour > 9 ? selectedHour : "0" + selectedHour) + ":" + (selectedMinute > 9 ? selectedMinute : "0" + selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        to_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ThirdActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        to_textView.setText((selectedHour > 9 ? selectedHour : "0" + selectedHour) + ":" + (selectedMinute > 9 ? selectedMinute : "0" + selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.done) {
            Log.e(TAG, " done= " + entry_id);
            if (manageEntry(entry_id)) {
//                Intent myIntent = new Intent(ThirdActivity.this, MainActivity.class);
//                startActivity(myIntent);
                onBackPressed();
                finish();
            }
            //Toast.makeText(this, "Click done", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadInfo(int p) {
        Cursor res = mydb.getEntry(p);
        res.moveToFirst();
        int start_hr = res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_START_HR));
        int start_min = res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_START_MIN));
        int end_hr = res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_END_HR));
        int end_min = res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_END_MIN));

        task_name_editView.setText(res.getString(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_ENTRY_NAME)));
        from_textView.setText((start_hr > 9 ? start_hr + "" : "0" + start_hr) + ":" + (start_min > 9 ? start_min + "" : "0" + start_min));
        to_textView.setText((end_hr > 9 ? end_hr + "" : "0" + end_hr) + ":" + (end_min > 9 ? end_min + "" : "0" + end_min));
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_MON))==1){
            mon_btn.setChecked(true);
        }else{
            mon_btn.setChecked(false);
        }
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_TUE))==1){
            tue_btn.setChecked(true);
        }else{
            tue_btn.setChecked(false);
        }
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_WEN))==1){
            wen_btn.setChecked(true);
        }else{
            wen_btn.setChecked(false);
        }
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_THU))==1){
            thu_btn.setChecked(true);
        }else{
            thu_btn.setChecked(false);
        }
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_FRI))==1){
            fri_btn.setChecked(true);
        }else{
            fri_btn.setChecked(false);
        }
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_SAT))==1){
            sat_btn.setChecked(true);
        }else{
            sat_btn.setChecked(false);
        }
        if(res.getInt(res.getColumnIndex(DBHelper.FeedEntry.COLUMN_NAME_SUN))==1){
            sun_btn.setChecked(true);
        }else{
            sun_btn.setChecked(false);
        }
    }

    private boolean manageEntry(int p) {
        Log.e(TAG, " update= " + p);
        String entry_name = task_name_editView.getText().toString();
        int isMon = mon_btn.isChecked() ? 1 : 0;
        int isTue = tue_btn.isChecked() ? 1 : 0;
        int isWen = wen_btn.isChecked() ? 1 : 0;
        int isThu = thu_btn.isChecked() ? 1 : 0;
        int isFri = fri_btn.isChecked() ? 1 : 0;
        int isSat = sat_btn.isChecked() ? 1 : 0;
        int isSun = sun_btn.isChecked() ? 1 : 0;
        String[] from = from_textView.getText().toString().split(":");
        String[] to = to_textView.getText().toString().split(":");

        if (p == 0) {
            return mydb.insertEntry("W1", 1, entry_name, Integer.parseInt(from[0]), Integer.parseInt(from[1]),
                    Integer.parseInt(to[0]), Integer.parseInt(to[1]),isMon,isTue,isWen,isThu,isFri,isSat,isSun,25,6);
        } else {
            return mydb.updateEntry(p, "W1", 1, entry_name, Integer.parseInt(from[0]), Integer.parseInt(from[1]),
                    Integer.parseInt(to[0]), Integer.parseInt(to[1]),isMon,isTue,isWen, isThu,isFri,isSat,isSun,25,6);
        }
    }
}
