package com.mysocial.hackfest;


import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mysocial.hackfest.classes.MapData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    // variables for adding location layer
    private MapView mapView;
    private static final String ID_ICON_AIRPORT = "airport";
    private MapboxMap mapboxMap;
    private Symbol symbol;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private double lat,lng;
    private String stringImage;
    private LocationComponent locationComponent;
    private  ArrayList<MapData> arrList=new ArrayList<MapData>();
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button,btn2;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        stringImage="android.graphics.Bitmap@48f4edb";
        mapView = findViewById(R.id.mapView);
        btn2=findViewById(R.id.gotoButton);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        // You can add any new location marker here
        // Steps:
        // Make A Map Data Object With the Location Name,Location Latitude and Location longitude
        // Ex- MapData <custom_variable_name>=new MapData(<name>,<latitiude>,<Longitude>);
        // Now add this MapData to arraylist like -> arrlist.add(<custom_variable_name>);
        // Done
        MapData StudentsActivityCenter=new MapData("Students Activity Center",23.817602991767018, 86.43754362173524);
        MapData HeritageBuilding=new MapData("Heritage Building",23.81440325360859, 86.44115923927656);
        MapData CentralLibrary=new MapData("Central Library",23.816593005598243,86.44243350117469);
        MapData ElectricSubstation=new MapData("Electric Substation",23.814384533800013, 86.43874276877608);
        MapData oldLibrary=new MapData("Old Library",23.81554269568486, 86.44127475919854);
        MapData nationalCadetCorps=new MapData("National Cadet Corps",23.812020476116697, 86.43907138157573);
        MapData chwOffice=new MapData("CHW Office",23.81591567568406, 86.44075977505234);
        MapData ismUpperGround=new MapData("Ism Upper Ground",23.813402947637208, 86.4428197117042);
        MapData newLectureComplex=new MapData("New Lecture Complex",23.816462746604728, 86.43997638408928);
        MapData jasperHostel=new MapData("Jasper Hostel",23.816709534948973, 86.44084657883755);
        MapData adminBlock=new MapData("Admin Block",23.81425755245915, 86.44238682361436);
        MapData ismSportsOffice=new MapData("IsmSports Office",23.81322030671901, 86.43993656695105);
        arrList.add(StudentsActivityCenter);
        arrList.add(CentralLibrary);
        arrList.add(ElectricSubstation);
        arrList.add(oldLibrary);
        arrList.add(nationalCadetCorps);
        arrList.add(chwOffice);
        arrList.add(ismUpperGround);
        arrList.add(HeritageBuilding);
        arrList.add(newLectureComplex);
        arrList.add(jasperHostel);
        arrList.add(adminBlock);
        arrList.add(ismSportsOffice);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MapActivity.this,GoToActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                for(MapData val:arrList ){
                    addAirplaneImageToStyle(style);
                    AddPoint(style,val.getLat(),val.getLng(), val.getName());
                }
                enableLocationComponent(style);
                addDestinationIconSymbolLayer(style);
                mapboxMap.addOnMapClickListener(MapActivity.this);
                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean simulateRoute = false;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();
                        NavigationLauncher.startNavigation(MapActivity.this, options);
                    }
                });
            }
        });
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }
        getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        return true;
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.updateRouteVisibilityTo(false);
                            navigationMapRoute.updateRouteArrowVisibilityTo(false);
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        SharedPreferences sh = getSharedPreferences("MapShared", MODE_PRIVATE);
        String latlong=sh.getString("position", "null");
        if(!latlong.equalsIgnoreCase("null")){
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("position","null");
            editor.apply();
            k=1;
            lat=Double.parseDouble(latlong.substring(0,16));
            lng=Double.parseDouble(latlong.substring(18,34));
            addCustomPoint(lat,lng);
        }
        if(k==1){
            addCustomPoint(lat,lng);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    public void addCustomPoint(double latitude,double longitude){
        Point destinationPoint = Point.fromLngLat(longitude, latitude);
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());
        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }
        getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
    }
    void AddPoint(Style style,double lat,double lng,String nae){
        SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.addLongClickListener(symbol -> {
            LayoutInflater factory = LayoutInflater.from(this);
            final View deleteDialogView = factory.inflate(R.layout.custom_dialog, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
            deleteDialog.setView(deleteDialogView);
            deleteDialogView.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //your business logic
                    deleteDialog.dismiss();
                }
            });
            TextView tvi=(TextView) deleteDialogView.findViewById(R.id.tv_location);
            tvi.setText(nae);
            TextView tvi2=(TextView) deleteDialogView.findViewById(R.id.tv_location_details);
            tvi2.setText(nae);
            ImageView ivr=(ImageView)deleteDialogView.findViewById(R.id.iv_location_image);
            ivr.setImageResource(R.drawable.diamondhost);
            switch (nae){
                case "Central Library":
                    tvi2.setText(R.string.central_library);
                    ivr.setImageResource(R.drawable.centerl_library_copy);
                    break;
                case "Electric Substation":
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
                    break;
                case "Old Library":
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
                    break;
                case "National Cadet Corps":
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
                    break;
                case "CHW Office":
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
                    break;
                case "Ism Upper Ground":
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
                    break;
                case "New Lecture Complex":
                    tvi2.setText(R.string.new_lecture_complex);
                    ivr.setImageResource(R.drawable.new_lecture_complex);
                    break;
                case "Jasper Hostel":
                    tvi2.setText(R.string.jasper_hostel);
                    ivr.setImageResource(R.drawable.jasper_hostel);
                    break;
                case "Admin Block":
                    tvi2.setText(R.string.admin_block);
                    ivr.setImageResource(R.drawable.admin_block);
                    break;
                case "IsmSports Office":
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
                    break;
                case "Heritage Building":
                    tvi2.setText(R.string.heritage_building);
                    ivr.setImageResource(R.drawable.heritage_building);
                    break;
                case "Students Activity Center":
                    tvi2.setText(R.string.students_activity_centers);
                    ivr.setImageResource(R.drawable.student_activity_center);
                    break;
                default:
                    tvi2.setText(R.string.def_place);
                    ivr.setImageResource(R.drawable.def_image_copy);
            }

            deleteDialog.show();
        });
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
        SymbolOptions symbolOptions = new SymbolOptions()
                .withLatLng(new LatLng(lat, lng))
                .withIconImage(ID_ICON_AIRPORT)
                .withIconSize(0.97f);
        symbolManager.create(symbolOptions);
    }
    private void addAirplaneImageToStyle(Style style) {
        style.addImage(ID_ICON_AIRPORT,
                BitmapUtils.getBitmapFromDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mapbox_marker_icon_default, null)),
                true);
    }
}