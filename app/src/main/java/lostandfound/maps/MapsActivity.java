package lostandfound.maps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import lostandfound.maps.data.DatabaseHelper;
import lostandfound.maps.model.Item;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Item> itemList = db.getAllItems();

        if(!itemList.isEmpty()){ // if there are items saved in the db:

            double latitude = Double.parseDouble(itemList.get(0).getLatitude());
            double longitude = Double.parseDouble(itemList.get(0).getLongitude());
            //focus camera to first item in list
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            for (Item item : itemList) {
                //add all items to map as markers.
                LatLng location = new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
                googleMap.addMarker(new MarkerOptions().position(location).title(item.getName()));
            }
        }

    }
}