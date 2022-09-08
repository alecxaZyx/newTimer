package com.example.newtimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

     CountDownTimer mCountdownTimer;
     TextView addPresetTime, presetBack;
     presetDBHelper DB;
     GridView presetGridView;
     GridAdapter mGridAdapter;
     ArrayList<PresetModel> arrayList = null;
     Button addPretimeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new presetDBHelper(this);
        presetGridView = findViewById(R.id.presetGView);
        arrayList = DB.getPresets();
        mGridAdapter =new GridAdapter(MainActivity.this, arrayList);

        presetGridView.setAdapter(mGridAdapter);
        mGridAdapter.notifyDataSetChanged();

        presetGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in =new Intent(MainActivity.this, MainActivity.class);
                PresetModel presetModel = arrayList.get(position);
                String idVal =String.valueOf(presetModel.getID());
                Cursor cursor = DB.getPresetID(idVal);

                while(cursor.moveToNext()){
                    String hrsVal = cursor.getString(1);
                    String minVal = cursor.getString(2);
                    String secsVal = cursor.getString(3);

                    in.putExtra("PreHrs", hrsVal);
                    in.putExtra("PreMin", minVal);
                    in.putExtra("PreSecs", secsVal);
                    startActivity(in);
                    //finish();
                }
            }
        });

        presetGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in =new Intent(MainActivity.this, MainActivity.class);
                PresetModel presetModel = arrayList.get(position);
                String idVal = String.valueOf(presetModel.getID());

                Boolean checkDelete = DB.deletePreset(idVal);
                if(checkDelete == true){
                    Toast.makeText(MainActivity.this, "Timer Preset Deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Timer Preset Failed to Delete!", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(in);
                overridePendingTransition(0, 0);
                return false;
            }
        });




        /** sets the minimum and max values for number picker **/
        NumberPicker numPickerSec = findViewById(R.id.numPickerSec);
        numPickerSec.setMinValue(0);
        numPickerSec.setMaxValue(59);
        //numPickerSec.setValue(0);
        numPickerSec.setWrapSelectorWheel(true);

        NumberPicker numPickerMins = findViewById(R.id.numPickerMins);
        numPickerMins.setMinValue(0);
        numPickerMins.setMaxValue(59);
        //numPickerMins.setValue(0);
        numPickerMins.setWrapSelectorWheel(true);

        NumberPicker numPickerHrs = findViewById(R.id.numPickerHrs);
        numPickerHrs.setMinValue(0);
        numPickerHrs.setMaxValue(12);
        //numPickerHrs.setValue(0);
        numPickerHrs.setWrapSelectorWheel(true);

        Intent rIn = getIntent();
        if (rIn.hasExtra("PreHrs") || rIn.hasExtra("PreMin") || rIn.hasExtra("PreSecs")) {
            String valueHrs = rIn.getStringExtra("PreHrs");
            String valueMin = rIn.getStringExtra("PreMin");
            String valueSecs = rIn.getStringExtra("PreSecs");

            numPickerSec.setValue(Integer.parseInt(valueSecs));
            numPickerMins.setValue(Integer.parseInt(valueMin));
            numPickerHrs.setValue(Integer.parseInt(valueHrs));
        }



        /** declare buttons -- to be used later **/
        Button StartBtn = findViewById(R.id.StartBtn);
        Button ResetBtn = findViewById(R.id.ResetBtn);
        Button StopBtn = findViewById(R.id.StopBtn);

        /** when start button is clicked **/
        StartBtn.setOnClickListener(view -> {
            /** gets the values from number pickers then convert it to seconds **/
            int totalHrs = numPickerHrs.getValue() * 3600;
            int totalMins = numPickerMins.getValue() * 60;
            int totalSecs = numPickerSec.getValue();
            int Duration = totalHrs + totalMins + totalSecs;
            int totalDuration = Duration * 1000;


            /** makes the main layout invisible and countdown layout visible **/
            View StudyTimerMain = findViewById(R.id.StudyTimerMain);
            StudyTimerMain.setVisibility(View.INVISIBLE);
            View CDViewLayout = findViewById(R.id.CountdownViewLayout);
            CDViewLayout.setVisibility(View.VISIBLE);

            /** set countdown timer **/
            mCountdownTimer = new CountDownTimer(totalDuration, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    /** gets the time left after countdown started **/
                    String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    /** sets text in the countdown textview **/
                    final String[] hourMinSec = time.split(":");
                    TextView CDViewHrs = findViewById(R.id.CDViewHrs);
                    TextView CDViewMins = findViewById(R.id.CDViewMins);
                    TextView CDViewSecs = findViewById(R.id.CDViewSecs);

                    CDViewHrs.setText(hourMinSec[0]);
                    CDViewMins.setText(hourMinSec[1]);
                    CDViewSecs.setText(hourMinSec[2]);
                }

                @Override
                public void onFinish() {
                    /** dialog shows upon ending the timer with the timer name set by the user **/
                    EditText timerNameTxt = (EditText) findViewById(R.id.TimerNameTxt);
                    String notifText = timerNameTxt.getText().toString();

                    AlertDialog.Builder notifDialog = new AlertDialog.Builder(MainActivity.this);
                    notifDialog.setTitle("Timer Ended!");
                    notifDialog.setMessage(notifText);
                    notifDialog.setCancelable(true);

                    notifDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            restartActivity();
                        }
                    });
                    notifDialog.show();

                }



            }.start();
        });
        /** resets the countdown **/
        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountdownTimer.start();
            }
        });
        /** restarts the activity and back to original layout **/
        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountdownTimer.cancel();
                restartActivity();
            }
        });

        presetBack = findViewById(R.id.presetBack);
        presetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.addPreset_layout);
                v.setVisibility(View.GONE);
            }
        });
        addPresetTime = findViewById(R.id.addPresetBtn);
        addPresetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.addPreset_layout);
                v.setVisibility(View.VISIBLE);

                /** set number picker values on add timer preset layout **/
                NumberPicker preSec = findViewById(R.id.prePickerSec);
                preSec.setMinValue(0);
                preSec.setMaxValue(59);
                preSec.setWrapSelectorWheel(true);

                NumberPicker preMins = findViewById(R.id.prePickerMins);
                preMins.setMinValue(0);
                preMins.setMaxValue(59);
                preMins.setWrapSelectorWheel(true);

                NumberPicker preHrs = findViewById(R.id.prePrickerHrs);
                preHrs.setMinValue(0);
                preHrs.setMaxValue(59);
                preHrs.setWrapSelectorWheel(true);



            }
        });

        addPretimeBtn = findViewById(R.id.addPreTimeBtn);
        addPretimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPicker preSec = findViewById(R.id.prePickerSec);
                NumberPicker preMins = findViewById(R.id.prePickerMins);
                NumberPicker preHrs = findViewById(R.id.prePrickerHrs);

                int hrs = preHrs.getValue();
                int mins = preMins.getValue();
                int secs = preSec.getValue();

                Boolean checkAddTime = DB.addPreset(hrs, mins, secs);

                if(checkAddTime == true){
                    Toast.makeText(MainActivity.this, "New Timer Preset Added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "New Timer Preset Addition Failed!", Toast.LENGTH_SHORT).show();
                }

                View v2 = findViewById(R.id.addPreset_layout);
                v2.setVisibility(View.GONE);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0); /** removes restart animation **/
    }
    }
