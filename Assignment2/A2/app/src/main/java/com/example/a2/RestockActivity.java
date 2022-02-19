package com.example.a2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RestockActivity extends AppCompatActivity {

    ArrayList<Product> mProductsArrayList;
    ProductAdapter mProductAdapter;
    boolean selectFlag = false;
    int newQuantity, productIndex;
    EditText editText;
    Button btn_CANCEL;

    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        outState.putString("EdtText", editText.getText().toString());
        outState.putSerializable("List", mProductsArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        editText.setText(savedInstanceState.getString("EdtText"));
        mProductsArrayList = (ArrayList<Product>) savedInstanceState.getSerializable("List");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock);

        editText = findViewById(R.id.edtTxt_Quantity);
        Button btn_OK = findViewById(R.id.btn_Ok);
        btn_CANCEL = findViewById(R.id.btn_Cancel);
        ListView listView = findViewById(R.id.listView_RestockProducts);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("Restock");

        mProductsArrayList = new ArrayList<>();
        mProductsArrayList = (ArrayList<Product>) data.getSerializable("Restock_List");

        mProductAdapter = new ProductAdapter(getApplicationContext(), R.layout.activity_restock,mProductsArrayList);
        listView.setAdapter(mProductAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectFlag = true;
                productIndex = position;
                newQuantity = 0;
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("") && selectFlag){
                    int totalQuantity = mProductsArrayList.get(productIndex).getQuantity();
                    newQuantity = Integer.parseInt(editText.getText().toString());
                    totalQuantity +=newQuantity;

                    if(totalQuantity < 0)
                        totalQuantity = 0;

                    mProductsArrayList.get(productIndex).setQuantity(totalQuantity);
                    mProductAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are REQUIRED", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("New_Products_List", mProductsArrayList);
        data.putExtra("Restock", bundle);
        setResult(RESULT_OK, data);
        selectFlag = false;
        super.onBackPressed();
    }
}