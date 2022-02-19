package com.example.a2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class HistoryDetailsActivity extends AppCompatActivity {

    TextView name, price, purchaseDate;

    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        outState.putString("Name", name.getText().toString());
        outState.putString("Price", price.getText().toString());
        outState.putString("Date", purchaseDate.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        name.setText(savedInstanceState.getString("Name"));
        price.setText(savedInstanceState.getString("Price"));
        purchaseDate.setText(savedInstanceState.getString("Date"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        name = findViewById(R.id.purchased_Product_Name);
        price = findViewById(R.id.purchased_Product_Price);
        purchaseDate = findViewById(R.id.purchased_Product_Date);


        name.setText("Product: " + getIntent().getStringExtra("Name"));
        price.setText("Price: " + getIntent().getStringExtra("Price"));
        purchaseDate.setText("Purchase Date: " + getIntent().getStringExtra("PurchaseDate"));

    }
}