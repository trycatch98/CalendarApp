package com.learn.adnroid.calendarapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class DetailViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private DBHelper mDbHelper;
    private String mAddress;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDbHelper = new DBHelper(this);

        int year = getIntent().getIntExtra("YEAR", -1);
        int month = getIntent().getIntExtra("MONTH", -1);
        int day = getIntent().getIntExtra("DAY", -1);
        int hour = getIntent().getIntExtra("HOUR", -1);
        int endHour = getIntent().getIntExtra("END_HOUR", -1);

        String id = getIntent().getStringExtra("ID");

        String mTitle = getIntent().getStringExtra("TITLE");
        mAddress = getIntent().getStringExtra("ADDRESS");
        String mNote = getIntent().getStringExtra("NOTE");

        TimePicker startTime = findViewById(R.id.start_time);
        TimePicker endTime = findViewById(R.id.end_time);

        if (hour == -1)
            hour = startTime.getHour();
        else {
            startTime.setHour(hour);
        }
        endTime.setHour(startTime.getHour() + 1);

        startTime.setMinute(0);
        endTime.setMinute(0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        EditText address = findViewById(R.id.address);
        Button find = findViewById(R.id.find);
        find.setOnClickListener(view -> {
            moveMap(address.getText().toString());
        });


        EditText title = findViewById(R.id.title);
        title.setText(year + "년 " + month + "월 " + day + "일 " + hour + "시");

        EditText note = findViewById(R.id.note);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            String base = year + " " + month + " " + day;
            String start = base + " " + startTime.getHour();
            String end = base + " " + endTime.getHour();
            String addr = address.getText().toString();
            String nt = note.getText().toString();

            mDbHelper.insertUserByMethod(title.getText().toString(), start, end, addr, nt);
            finish();
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> {
            finish();
        });

        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        mDbHelper.deleteUserByMethod(id);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    })
                    .create()
                    .show();
        });

        if (endHour != -1) {
            endTime.setHour(endHour);
            title.setText(mTitle);
            address.setText(mAddress);
            note.setText(mNote);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        moveMap(mAddress);
    }

    public void moveMap(String address) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                Address address1 = addresses.get(0);
                LatLng hansung = new LatLng(address1.getLatitude(), address1.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(hansung).title(address));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}