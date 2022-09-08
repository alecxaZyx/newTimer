package com.example.newtimer;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context context;
    ArrayList<PresetModel> arrayList;

    public GridAdapter(Context context, ArrayList<PresetModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convView = inflater.inflate(R.layout.activity_addpreset_layout, null);
        TextView IDtxt = (TextView) convView.findViewById(R.id.presetID);
        TextView prehrsTxt = (TextView)convView.findViewById(R.id.preHrsTxt);
        TextView preminsTxt = (TextView)convView.findViewById(R.id.preMinsTxt);
        TextView presecsTxt = (TextView)convView.findViewById(R.id.preSecsTxt);

        PresetModel presetmodel = arrayList.get(position);
        String idValue = String.valueOf(presetmodel.getID());
        IDtxt.setText(idValue);
        String hrsVal = String.format("%02d", presetmodel.getPreHrs());
        prehrsTxt.setText(hrsVal);
        String minsVal = String.format("%02d", presetmodel.getPreMins());
        preminsTxt.setText(minsVal);
        String secsVal = String.format("%02d", presetmodel.getPreSecs());
        presecsTxt.setText(secsVal);

        return convView;
    }
}
