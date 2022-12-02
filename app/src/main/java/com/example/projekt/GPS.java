package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class GPS extends AppCompatActivity implements  LocationListener {
    private static String TAG = "GPS";
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 2;
    private TextView bestProvider;
    private TextView longitude;
    private TextView latitude;
    private TextView archivalData;
    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;
    private String bp;
    private int amount;
    private Button button;
    private Resources resources;

    private MapView osm;
    private MapController mapController;

    private RelativeLayout relativeLayout;
    private TextView textNetwork;
    private TextView textGPS;

    private GeoPoint yourLocation;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        resources = getResources();

        checkGPSPermission();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        bestProvider = findViewById(R.id.bestProvider);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        archivalData = findViewById(R.id.archivalData);

        relativeLayout = findViewById(R.id.relativeLayout);
        textNetwork = findViewById(R.id.textNetwork);
        textGPS = findViewById(R.id.textGPS);

        archivalData.setText(resources.getString(R.string.gps_measurements)+"\n\n");
        isNetworkAvailable();

        if(isGPSAvailable()){
            criteria = new Criteria();
            bp = locationManager.getBestProvider(criteria,true);

            locationManager.requestLocationUpdates(
                    ""+bp,
                    500,
                    0.5f,
                    (LocationListener) this
            );
            bestProvider.setText(resources.getString(R.string.gps_best_provider) + bp);
            location = locationManager.getLastKnownLocation(bp);
        }

        button = findViewById(R.id.refresh);
        button.setOnClickListener(v ->{
            finish();
            startActivity(getIntent());
        });

        osm = findViewById(R.id.osm);
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);

        mapController = (MapController) osm.getController();
        mapController.setZoom(12);

    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        Log.v(TAG,networkInfo+"");

        if(networkInfo!=null){
            textNetwork.setText("Internet: "+resources.getString(R.string.on));
            textNetwork.setTextColor(Color.GREEN);
        }
        else {
            textNetwork.setText("Internet: "+resources.getString(R.string.off));
            textNetwork.setTextColor(Color.RED);
        }

        return networkInfo !=null;
    }

//    @Override
//    public void onProviderEnabled(@NonNull String provider) {
//        isGPSAvailable();
//        finish();
//        startActivity(getIntent());
//        LocationListener.super.onProviderEnabled(provider);
//    }
//
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        finish();
        startActivity(getIntent());
        LocationListener.super.onProviderDisabled(provider);
    }



    public boolean isGPSAvailable(){
        boolean gpsStatus = locationManager.isLocationEnabled();
        if(gpsStatus){
            textGPS.setText("GPS: "+resources.getString(R.string.on));
            textGPS.setTextColor(Color.GREEN);
        }
        else {
            textGPS.setText("GPS: "+resources.getString(R.string.off));
            textGPS.setTextColor(Color.RED);
        }

        return gpsStatus;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ((requestCode)){
            case MY_PERMISSION_ACCESS_FINE_LOCATION:{
                if(permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG,"uprawnienia" + requestCode + " " + permissions[0] + grantResults[0]);
                    Log.d(TAG,"Permissions ACCESS_COARSE_LOCATION was granted");
                    Toast.makeText(this,"Permissions ACCESS_COARSE_LOCATION was granted",Toast.LENGTH_SHORT).show();
                    this.recreate();
                }else{
                    Log.d(TAG,"Permissions ACCESS_COARSE_LOCATION denied");
                    Toast.makeText(this,"Permissions ACCESS_COARSE_LOCATION denied",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: Log.d(TAG,"Another permissions");

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        bp = locationManager.getBestProvider(criteria,true);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED){

            isNetworkAvailable();
            isGPSAvailable();


            location = locationManager.getLastKnownLocation(bp);
            bestProvider.setText(resources.getString(R.string.gps_best_provider) + " " + bp);
            longitude.setText(resources.getString(R.string.gps_longitude)+" "+ location.getLongitude());
            latitude.setText(resources.getString(R.string.gps_latitude)+" "+ location.getLatitude());
            archivalData.setText(archivalData.getText()+" "+location.getLongitude()+" : "+location.getLatitude()+"\n");
            amount++;

            yourLocation = new GeoPoint(location.getLatitude(),location.getLongitude());
            mapController.setCenter(yourLocation);
            mapController.animateTo(yourLocation);
            addMarkerToMap(yourLocation);

            Log.d(TAG,amount+" pomiar: "+bp + " " + location.getLongitude() + " " + location.getLatitude());


        }

    }

    public void addMarkerToMap (GeoPoint center){
        GeoPoint shop = new GeoPoint(53.02545767162853, 18.637661075997386);
        Marker shop_marker = new Marker(osm);
        shop_marker.setPosition(shop);
        shop_marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        shop_marker.setIcon(getResources().getDrawable(R.drawable.marker));
        shop_marker.setTitle("Shop");

        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.your_location));
        marker.setTitle("My position");
        osm.getOverlays().clear();
        osm.getOverlays().add(marker);
        osm.getOverlays().add(shop_marker);
        osm.invalidate();
    }

    public void checkGPSPermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);

            return;
        }
    }
}