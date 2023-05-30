package lostandfound.maps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lostandfound.maps.data.DatabaseHelper;
import lostandfound.maps.model.Item;


public class ShowItemsActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button returnHome;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_items);

        db = new DatabaseHelper(this);
        List<Item> items = db.getAllItems();
        returnHome = findViewById(R.id.button4);

        RecyclerView recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter(this, items));
        ItemAdapter adapter = new ItemAdapter(this, items);
        recyclerView.setAdapter(adapter);

        // if clicked, returns to main menu.
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowItemsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

