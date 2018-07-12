package redsail.readr;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;



import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MapActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final LatLng USER1 = new LatLng(38.888484, 121.543655);

    private static final LatLng USER2 = new LatLng(38.886093, 121.533808);

    private static final LatLng USER3 = new LatLng(38.895793, 121.524364);


    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    public GoogleMap bookMap;

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseUser currentUser = mAuth.getCurrentUser();

    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public FusedLocationProviderClient mFusedLocationClient;
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        addListener();

        buildGoogleApiClient();

    }

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            bookMap.moveCamera(CameraUpdateFactory
                                    .newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            requestLocationUpdates();
        } else {
            buildGoogleApiClient();
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        bookMap = map;

        bookMap.setOnMyLocationButtonClickListener(this);
        bookMap.setOnMyLocationClickListener(this);
        bookMap.setOnInfoWindowClickListener(this);

        enableMyLocation();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (bookMap != null) {
            // Access to the location has been granted to the app.
            bookMap.setMyLocationEnabled(true);
        }
    }

    public void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Unimplemented: Context switch to personal profile", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                Log.i("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                writeLocation(location);
            }
        }

        ;
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Unimplemented: Context switch to specific profile:" + marker.getTitle(),
                Toast.LENGTH_SHORT).show();
    }

    private void writeLocation(Location location) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/locations/" + currentUser.getUid() + "/lat/", location.getLatitude());
        childUpdates.put("/locations/" + currentUser.getUid() + "/long/", location.getLongitude());

        mDatabase.updateChildren(childUpdates);
    }

    public void addListener() {
        FirebaseDatabase.getInstance().getReference().child("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap<String, HashMap> dataMap = (HashMap) dataSnapshot.getValue();

                if (bookMap != null) {
                    bookMap.clear();
                }

                for (String key : dataMap.keySet()) {
                    String lat = dataMap.get(key).get("lat").toString();
                    String lng = dataMap.get(key).get("long").toString();
                    setMarker(key, lat, lng);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("MapActivity", "loadPost:Shat itself", databaseError.toException());
                // ...
            }
        });
    }

    public void setMarker(String userId, final String lat, final String lng) {

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap<String, String> dataMap = (HashMap) dataSnapshot.getValue();

                String username = dataMap.get("username").toString();
                LatLng userLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                MarkerOptions uMarkOpts = new MarkerOptions()
                        .position(userLocation)
                        .title(username)
                        .snippet("Empty Booklist");

                switch(username){
                    case "Daniel":
                        BitmapDrawable bitmapdraw1=(BitmapDrawable)getResources().getDrawable(R.drawable.daniel);
                        Bitmap b1=bitmapdraw1.getBitmap();
                        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1, 84, 84, false);
                        uMarkOpts.icon(BitmapDescriptorFactory.fromBitmap(smallMarker1));
                        break;
                    case "Frances":
                        BitmapDrawable bitmapdraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.frances);
                        Bitmap b2=bitmapdraw2.getBitmap();
                        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, 84, 84, false);
                        uMarkOpts.icon(BitmapDescriptorFactory.fromBitmap(smallMarker2));
                        break;
                    case "Jack":
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.jack);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 84, 84, false);
                        uMarkOpts.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        break;
                    default:
                        break;
                }
                if (uMarkOpts.getIcon() != null) {
                    bookMap.addMarker(uMarkOpts);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("MapActivity", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

}