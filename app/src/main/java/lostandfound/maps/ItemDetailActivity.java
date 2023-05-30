package lostandfound.maps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import lostandfound.maps.data.DatabaseHelper;

public class ItemDetailActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView nameText, descriptionText, phoneText, lostText, dateText, locationText;
    Button removeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        nameText = findViewById(R.id.item_name_details);
        descriptionText = findViewById(R.id.item_description_details);
        phoneText = findViewById(R.id.item_number_details);
        lostText = findViewById(R.id.item_lost_details);
        dateText = findViewById(R.id.item_date_details);
        locationText = findViewById(R.id.item_location_details);
        removeButton = findViewById(R.id.removeButton);

        db = new DatabaseHelper(this);
        // Get item details from intent
        int itemId = getIntent().getIntExtra("item_id", -1);
        String name = getIntent().getStringExtra("item_name");
        String description = getIntent().getStringExtra("item_description");
        String phone = getIntent().getStringExtra("item_phone");
        String lost = getIntent().getStringExtra("item_lost");
        String date = getIntent().getStringExtra("item_date");
        String location = getIntent().getStringExtra("item_location");

        nameText.setText(name);
        descriptionText.setText(description);
        phoneText.setText(phone);
        lostText.setText(lost);
        dateText.setText(date);
        locationText.setText(location);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove item from the database
                db.deleteItem(itemId);
                // Display toast message
                Toast.makeText(ItemDetailActivity.this, "Item removed!", Toast.LENGTH_SHORT).show();
                // Return to the previous activity
                Intent intent = new Intent(ItemDetailActivity.this, ShowItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}