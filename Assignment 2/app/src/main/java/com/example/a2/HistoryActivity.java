package com.example.a2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class HistoryActivity extends AppCompatActivity {

    ArrayList<History> purchaseList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter<PurchasedProductAdapter.ViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.purchased_Product_List);

        if(getIntent().hasExtra("History")){
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("History");
            purchaseList = (ArrayList<History>) bundle.getSerializable("History_List");
        }

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new PurchasedProductAdapter(purchaseList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        outState.putSerializable("List", purchaseList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        purchaseList = (ArrayList<History>) savedInstanceState.getSerializable("List");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new PurchasedProductAdapter(purchaseList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        super.onRestoreInstanceState(savedInstanceState);
    }
}