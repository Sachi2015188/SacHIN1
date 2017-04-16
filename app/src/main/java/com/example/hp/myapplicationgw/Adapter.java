package com.example.hp.myapplicationgw;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thevaki on 4/15/2017.
 */

public class Adapter extends ArrayAdapter<FirebaseDB> {
    public Adapter(Context context, int resource, List<FirebaseDB> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.data, parent, false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView timeOn = (TextView) convertView.findViewById(R.id.onTime);
        TextView timeOff = (TextView) convertView.findViewById(R.id.offTime);

        FirebaseDB data = getItem(position);

        date.setVisibility(View.VISIBLE);
        date.setText(data.getDate());
        timeOn.setText("Time on "+data.getTimeOn().toString()+"      Time off "+data.getTimeOff());
        timeOff.setText("Temp on "+data.getTempOn()+"       Temp off "+data.getTempOff()+'\n');

        return convertView;
    }
}
