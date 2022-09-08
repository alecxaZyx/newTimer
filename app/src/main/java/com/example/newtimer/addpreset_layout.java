package com.example.newtimer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class addpreset_layout extends AppCompatActivity {

    View time_section, delete_layout;
    ImageView deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpreset_layout);

        time_section = findViewById(R.id.section_time);
        delete_layout = findViewById(R.id.delete_imgview);


    }
}