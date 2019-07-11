package com.example.demolocationdetection;

import android.Manifest;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    Button btnGetLastLocation,btnGetLocationUpdate,btnRemoveLocationUpdate;

    FusedLocationProviderClient client;
    LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnGetLastLocation = findViewById(R.id.btnGetLastLocation);
        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = findViewById(R.id.btnDeleteLocationUpdate);

        client = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest mLocationRequest = LocationRequest.create();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();
                    String msg = "New Loc Detected \nLat : " + lat + "Lng : " + lng;
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }
        };

        btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission() == true){
                    LocationRequest mLocationRequest = new LocationRequest();
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    mLocationRequest.setInterval(10000);
                    mLocationRequest.setFastestInterval(5000);
                    mLocationRequest.setSmallestDisplacement(100); //if distance more than 100m, then will trigger update
                    client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                }else{
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
            }
        });
        btnRemoveLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.removeLocationUpdates(mLocationCallback);
            }
        });



        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission() == true) {
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {


                            //get last known location. in some rare situations this can be null.
                            if (location != null) {
                                String msg = "Lat : " + location.getLatitude() + "Lng : " + location.getLongitude();
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                            } else {
                                String msg = "No Last Known Location Found";
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
            }
        });
    }
    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}