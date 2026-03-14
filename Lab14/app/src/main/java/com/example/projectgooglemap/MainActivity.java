package com.example.projectgooglemap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView mapSearchView;
    private  SearchView searchFrom, searchTo;
    private String fromLocation ="";
    private String toLocation ="";
    private String searchedLocation = "";
    private boolean isInDirectionMode = false;
    private ImageButton btnSwap;
    private Button btnMyLocation;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        mapSearchView = findViewById(R.id.mapSearch);
        searchFrom = findViewById(R.id.searchFrom);
        searchTo = findViewById(R.id.searchTo);
        btnSwap = findViewById(R.id.btnSwap);
        btnMyLocation = findViewById(R.id.btnMyLocation);

        // button dùng để lấy vị trí hiện tại trong tìm đường
        Button btnUseCurrent = findViewById(R.id.btnUseCurrent);
        btnUseCurrent.setOnClickListener(v -> {
            if(currentLocation != null){
                fromLocation = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                searchFrom.setQuery(fromLocation, false);
                Toast.makeText(MainActivity.this, "Using current location", Toast.LENGTH_SHORT).show();
            }
        });

        // button để về lại vị trí hiện tại
        btnMyLocation.setOnClickListener(v -> {
            if (currentLocation != null && myMap != null) {
                LatLng myLatLng = new LatLng(
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude()
                );
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(myLatLng).title("My Location"));
                myMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(myLatLng, 15)
                );
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Current location not available",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // button đổi 2 vị trí trong tìm đường
        btnSwap.setOnClickListener(v -> {
            // Láº¥y text hiá»‡n táº¡i
            String fromText = searchFrom.getQuery().toString();
            String toText   = searchTo.getQuery().toString();

            // Swap text
            searchFrom.setQuery(toText, false);
            searchTo.setQuery(fromText, false);

            // Náº¿u báº¡n cĂ³ lÆ°u biáº¿n riĂªng thĂ¬ swap luĂ´n
            String temp = fromLocation;
            fromLocation = toLocation;
            toLocation = temp;

        });

        // button tìm đường
        Button btnFindDirection = findViewById(R.id.btnFindDirection);
        btnFindDirection.setOnClickListener(v -> {
            String fromInput = searchFrom.getQuery().toString().trim();
            String toInput = searchTo.getQuery().toString().trim();
            if(fromInput.isEmpty() || toInput.isEmpty()){
                Toast.makeText(MainActivity.this,
                        "Please enter both From and To location",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            openGoogleMapsDirection(fromInput, toInput);
        });

        // khởi tạo lấy lấy vị trí hiện tại từ Google Play Services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        // search map
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (myMap == null) return false;

                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> addressList;

                try {
                    addressList = geocoder.getFromLocationName(query, 1);

                    if (addressList != null && !addressList.isEmpty()) {
                        searchedLocation = query;
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(
                                address.getLatitude(),
                                address.getLongitude()
                        );
                        myMap.clear();
                        myMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(query));
                        myMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(latLng, 15)
                        );
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Location not found",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // xử lý ô nhập form
        searchFrom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fromLocation = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchTo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                toLocation = query;
                if(fromLocation.isEmpty()){
                    Toast.makeText(MainActivity.this,
                            "Please enter From location",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                openGoogleMapsDirection(fromLocation, toLocation);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    searchedLocation ="";
                }
                return false;
            }
        });
    }

    private void getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isEnabled){
            Toast.makeText(this,
                    "Please turn on Location service",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
//        LatLng vietnam = new LatLng(21.0285, 105.8542); // kinh Ä‘á»™, vÄ© Ä‘á»™ cá»§a HĂ  Ná»™i
        LatLng vietnam = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        myMap.addMarker(new MarkerOptions().position(vietnam).title("My Location"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vietnam, 15));

        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Location permission is denied, pls allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.mapNone){
            myMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
        else if(id == R.id.mapNormal){
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        else if(id == R.id.mapSatellite){
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        else if(id == R.id.mapHybrid){
            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        else if(id == R.id.mapTerrain){
            myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        else if (id == R.id.mapDirection){

            LinearLayout layoutDirection = findViewById(R.id.layoutDirection);

            if(!isInDirectionMode){
                layoutDirection.setVisibility(View.VISIBLE);
                mapSearchView.setVisibility(View.GONE);

                String currentToText = searchTo.getQuery().toString();

                if(currentToText.isEmpty() && !searchedLocation.isEmpty()){
                    searchTo.setQuery(searchedLocation, false);
                }
                searchFrom.setIconified(false);
                searchTo.setIconified(false);
                isInDirectionMode = true;

            } else {
                layoutDirection.setVisibility(View.GONE);
                mapSearchView.setVisibility(View.VISIBLE);

                searchFrom.setQuery("", false);
                searchTo.setQuery("", false);

                isInDirectionMode = false;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGoogleMapsDirection(String from, String to){

        Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1"
                + "&origin=" + Uri.encode(from)
                + "&destination=" + Uri.encode(to)
                + "&travelmode=driving");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        startActivity(intent);
    }
}