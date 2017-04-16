package com.example.hp.myapplicationgw;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private double temp_data = 0;

    private ListView dateView;
    DatabaseHelper db;
    private FirebaseDatabase firebaseDatabase; //entry point to access database
    private DatabaseReference databaseReference; //referces a specific part of the database
    private ChildEventListener childEventListener;
    private Adapter mMessageAdapter;

    SwitchOn s1=new SwitchOn();
    SwitchOff s2=new SwitchOff();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Data");
        dateView = (ListView) findViewById(R.id.dateView);


        final List<FirebaseDB> firebaseDB = new ArrayList<>();
        mMessageAdapter = new Adapter(this, R.layout.data, firebaseDB);
        dateView.setAdapter(mMessageAdapter);

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseDB f=dataSnapshot.getValue(FirebaseDB.class);
                mMessageAdapter.add(f);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

        db = new DatabaseHelper(this);//call the constructor

        Button switchOn = (Button) findViewById(R.id.on);
        Button switchOff = (Button) findViewById(R.id.off);
        if (temp_data > 25) {
            Notification.Builder builder = new Notification.Builder(this).setContentTitle("High Temparature").setContentText(Double.toString(temp_data)).setAutoCancel(true).setSmallIcon(R.drawable.alert);
            Intent intent = new Intent(String.valueOf(MainActivity.this));
            intent.putExtra("msg", Double.toString(temp_data) + "showed");
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 3, intent, 0);
            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notification);
        }
        switchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SwitchOn().execute();
            }
        });
        switchOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SwitchOff().execute();
                addFirebase();
                insertData();
            }
        });

    }

    public class SwitchOn extends AsyncTask<Integer, Void, Void> {
        private final String API_KEY = "50d443f06ac3bbf32ff5677a336a701176db2929";
        private final String VARIABLE_TEMP_ID1 = "587f6d0676254202b4b6dc69";
        private final String VARIABLE_RELAY_ID1 = "587f6d8a7625424ff40f1dd1";

        ApiClient coolUrCar;
        Variable relay;
        TextView setTemp;
        Variable temparature;

        @Override
        protected void onPreExecute() {
        setTemp=(TextView) findViewById(R.id.textView);
        setTemp.setText(Double.toString(temp_data));
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            coolUrCar = new ApiClient(API_KEY);
            temparature = coolUrCar.getVariable(VARIABLE_TEMP_ID1);
            relay = coolUrCar.getVariable(VARIABLE_RELAY_ID1);
            Value[] temp = temparature.getValues();
            temp_data = temp[0].getValue();
            if (temp_data > 25) {
                relay.saveValue(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setTemp = (TextView) findViewById(R.id.textView);
            setTemp.setText(Double.toString(temp_data));
            super.onPostExecute(aVoid);
        }

        String temp1=String.valueOf(temp_data);
        Calendar c = Calendar.getInstance();
        String date = String.valueOf(c.get(Calendar.YEAR)) + " "+String.valueOf(c.get(Calendar.MONTH))+" " + String.valueOf(c.get(Calendar.DATE));
        String timeOn = String.valueOf(c.get(Calendar.HOUR))+"." + String.valueOf(c.get(Calendar.MINUTE));
    }

    public class SwitchOff extends AsyncTask<Integer, Void, Void> {
        private final String API_KEY = "50d443f06ac3bbf32ff5677a336a701176db2929";
        private final String VARIABLE_TEMP_ID1 = "587f6d0676254202b4b6dc69";
        private final String VARIABLE_RELAY_ID1 = "587f6d8a7625424ff40f1dd1";
        private double temp_data = 0;
        ApiClient coolUrCar;
        Variable relay;
        Variable temperature;

        @Override
        protected Void doInBackground(Integer... params) {

            coolUrCar = new ApiClient(API_KEY);
            temperature = coolUrCar.getVariable(VARIABLE_TEMP_ID1);
            relay = coolUrCar.getVariable(VARIABLE_RELAY_ID1);
            Value[] temp = temperature.getValues();
            temp_data = temp[0].getValue();
            if (temp_data <= 25) {
                relay.saveValue(0);

            }
            return null;
        }

        String temp2=String.valueOf(temp_data);
        Calendar c2 = Calendar.getInstance();
        String timeOff = String.valueOf(c2.get(Calendar.HOUR))+"." + String.valueOf(c2.get(Calendar.MINUTE));
    }

    public void addFirebase(){
        FirebaseDB f = new FirebaseDB(s1.date,s1.timeOn,s1.temp1,s2.timeOff,s2.temp2);
        databaseReference.push().setValue(f);
    }

    public void insertData(){
        boolean isInserted=db.insert(s1.date,s1.timeOn,s1.temp1,s2.timeOff,s2.temp2);
        if(isInserted==true){
            Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
        }
    }

}

