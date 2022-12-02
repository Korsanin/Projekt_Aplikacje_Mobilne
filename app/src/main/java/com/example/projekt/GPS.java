package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
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

    private MapView osm;
    private MapController mapController;

    private RelativeLayout swipeRefreshLayout;
    private TextView textNetwork;
    private TextView textGPS;

    private GeoPoint yourLocation;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);


        criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        bp = locationManager.getBestProvider(criteria,true);

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);

            return;
        }

        location = locationManager.getLastKnownLocation(bp);

        bestProvider = findViewById(R.id.bestProvider);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        archivalData = findViewById(R.id.archivalData);


        locationManager.requestLocationUpdates(
                ""+bp,
                500,
                0.5f,
                (LocationListener) this
        );

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textNetwork = findViewById(R.id.textNetwork);
        textGPS = findViewById(R.id.textGPS);

        bestProvider.setText("Best provider: " + bp);
//        longitude.setText("Longitude: "+ location.getLongitude());
//        latitude.setText("Latitude: "+ location.getLatitude());
        archivalData.setText("Measurement readings:\n\n");
//        Log.d(TAG,bp + " " + location.getLongitude() + " " + location.getLatitude());

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
        return networkInfo !=null;
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

            boolean connection = isNetworkAvailable();
            if(connection){
                textNetwork.setText("internet connect");
                textNetwork.setTextColor(Color.GREEN);
            }
            else {
                textNetwork.setText("no internet");
                textNetwork.setTextColor(Color.RED);
            }

            location = locationManager.getLastKnownLocation(bp);
            bestProvider.setText("Best provider: " + bp);
            longitude.setText("Longitude: "+ location.getLongitude());
            latitude.setText("Latitude: "+ location.getLatitude());
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
}