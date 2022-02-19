package com.example.a2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ManagerActivity extends AppCompatActivity {

    ArrayList<Product> productsList;
    ArrayList<History> purchaseList;
    ActivityResultLauncher<Intent> restockResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()  == Activity.RESULT_OK){
                Intent data = result.getData();
                Bundle Resultbundle = data.getBundleExtra("Restock");
                purchaseList = (ArrayList<History>) Resultbundle.getSerializable("New_Products_List");
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Updated_Products_List", purchaseList);
                i.putExtra("Manager", bundle);
                setResult(RESULT_OK, i);
                ManagerActivity.super.onBackPressed();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Button button_History, button_Restock;

        button_History = findViewById(R.id.btn_History);
        button_Restock = findViewById(R.id.btn_Restock);

        if(getIntent().hasExtra("ManagerBundle")){
            Intent i = getIntent();
            Bundle bundle = i.getBundleExtra("ManagerBundle");
            productsList = (ArrayList<Product>) bundle.getSerializable("Products_List");
            purchaseList = (ArrayList<History>) bundle.getSerializable("Purchase_List");
        }

        button_History.setOnClickListener(view -> {
            Intent mIntent = new Intent(getApplicationContext(), HistoryActivity.class);
            Bundle bundleHistory = new Bundle();
            bundleHistory.putSerializable("History_List", purchaseList);
            mIntent.putExtra("History", bundleHistory);
            startActivity(mIntent);
        });

        button_Restock.setOnClickListener(view -> {
            Intent mIntent = new Intent(getApplicationContext(), RestockActivity.class);
            Bundle bundleRestock = new Bundle();
            bundleRestock.putSerializable("Restock_List", productsList);
            mIntent.putExtra("Restock", bundleRestock);
            restockResult.launch(mIntent);
        });
    }
}