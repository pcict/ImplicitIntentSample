package com.websarva.wings.android.implicitintentsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private double _latitude = 0;
    private double _longitude = 0;

    private FusedLocationProviderClient _fusedLocationClient;
    private LocationRequest _locationRequest;
    private OnUpdateLocation _onUpdateLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //P.368
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        LocationRequest.Builder builder = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000);

        _locationRequest = builder.build();

        _onUpdateLocation = new OnUpdateLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1000);

            return;
        }
        _fusedLocationClient.requestLocationUpdates(_locationRequest, _onUpdateLocation, Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();

        _fusedLocationClient.removeLocationUpdates(_onUpdateLocation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            _fusedLocationClient.requestLocationUpdates(_locationRequest, _onUpdateLocation, Looper.getMainLooper());
        }
    }

    public void onMapSearchButtonClick(View view){
        EditText etSearchWord = findViewById(R.id.etSearchWord);
        String searchWord = etSearchWord.getText().toString();

        try {
            searchWord = URLEncoder.encode(searchWord,"UTF-8");

            String uriStr = "geo:0,0?q=" + searchWord;

            Uri uri = Uri.parse(uriStr);

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            startActivity(intent);
        }
        catch (UnsupportedEncodingException ex){
            Log.e("MainActivity", "検索キーワード変換失敗", ex);
        }

    }

    public void onMapShowCurrentButtonClick(View view){

        String uriStr = "geo:" + _latitude + "," + _latitude;
        Uri uri = Uri.parse(uriStr);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);

    }

    private class OnUpdateLocation extends LocationCallback {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            Location location = locationResult.getLastLocation();
            if(location != null){
                _latitude = location.getLatitude();
                _longitude = location.getLongitude();

                TextView tvLatitude = findViewById(R.id.tvLatitude);
                tvLatitude.setText(Double.toString(_latitude));

                TextView tvLongitude = findViewById(R.id.tvLongitude);
                tvLongitude.setText(Double.toString(_longitude));
            }
        }
    }
}