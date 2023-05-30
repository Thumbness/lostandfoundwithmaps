package lostandfound.maps;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import lostandfound.maps.data.DatabaseHelper;
import lostandfound.maps.model.Item;

public class CreateItemActivity extends AppCompatActivity {
    CheckBox lostCheckbox;
    CheckBox foundCheckbox;
    EditText nameText;
    EditText numberText;
    EditText descriptionText;
    EditText dateText;
    EditText locationText;
    Button submitButton, getLocationButton;
    DatabaseHelper db;
    Boolean selectedCurrentPosition;
    LatLng currentLocation;
    private Place selectedPlace;
    String latitude;
    String longitude;
    private FusedLocationProviderClient mFusedLocationClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_advert);
        db = new DatabaseHelper(this);
        lostCheckbox = findViewById(R.id.checkBox);
        foundCheckbox = findViewById(R.id.checkBox2);
        nameText = findViewById(R.id.editTextTextPersonName);
        numberText = findViewById(R.id.editTextPhone);
        descriptionText = findViewById(R.id.editTextTextPersonName2);
        dateText = findViewById(R.id.editTextDate);
        locationText = findViewById(R.id.editTextTextPersonName3);
        submitButton = findViewById(R.id.button);
        getLocationButton = findViewById(R.id.button6);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        selectedCurrentPosition = false;

        //initialize places API for autocomplete
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAeLJ6t4uZkLR_1EcK2biaU1hBxrOCzfl0");
        }

        // Set up the autocomplete fragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace = place;
                locationText.setText(place.getName());
                //flag for using either current location or autocomplete when saving to db
                selectedCurrentPosition = false;
            }

            @Override
            public void onError(Status status) {
                Log.v(TAG, "An error occurred: " + status);
            }
        });

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Check permissions for current location
                if (ActivityCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateItemActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(CreateItemActivity.this, location -> {
                            if (location != null) {
                                // Logic to handle location object
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                currentLocation = new LatLng(latitude, longitude);
                                selectedCurrentPosition = true;
                                Toast.makeText(CreateItemActivity.this, "Successfully saved location", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Store as variables
                Boolean lost = lostCheckbox.isChecked();
                Boolean found = foundCheckbox.isChecked();
                String name = nameText.getText().toString();
                String number = numberText.getText().toString();
                String description = descriptionText.getText().toString();
                String date = dateText.getText().toString();
                String location = locationText.getText().toString();
                if(selectedCurrentPosition == false){
                    latitude = String.valueOf(selectedPlace.getLatLng().latitude);
                    longitude = String.valueOf(selectedPlace.getLatLng().longitude);
                }
                else{
                    latitude = String.valueOf(currentLocation.latitude);
                    longitude = String.valueOf(currentLocation.longitude);
                }
                String status = null;
                if(lost == true && found == false)
                {
                    status = "Lost";
                }
                else if(lost == false && found == true)
                {
                    status = "found";
                }
                else
                {
                    status = "unknown";
                }
                //insert values as item into db.
                long result = db.insertItem(new Item(status, name, description, location, number, date, latitude, longitude));
                if(result > 0)
                {
                    Toast.makeText(CreateItemActivity.this, "Successfully entered item into database", Toast.LENGTH_SHORT).show();
                }
                //return to home page
                Intent intent = new Intent(CreateItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
