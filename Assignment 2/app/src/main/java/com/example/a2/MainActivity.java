package com.example.a2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private TextView tv_SelectedQuantity, tv_SelectedProduct, tv_Total;
    private ArrayList<Product> productsArrayList;
    private ArrayList<History> purchaseList;
    private static ProductAdapter adapter;
    ListView listView;
    NumberPicker numberPicker1;
    Boolean itemAdded = false;

    ActivityResultLauncher<Intent> managerResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()  == Activity.RESULT_OK){
                Intent data = result.getData();
                Bundle bundle = data.getBundleExtra("Manager");
                productsArrayList = (ArrayList<Product>) bundle.getSerializable("Updated_Products_List");
                adapter = new ProductAdapter(MainActivity.this, R.layout.products_listview, productsArrayList);
                listView.setAdapter(adapter);
            }
        }
    });

    @Override
    public void onBackPressed() {
        Intent data = getIntent();
        Bundle bundle = data.getBundleExtra("Manager");
        productsArrayList = (ArrayList<Product>) bundle.getSerializable("Updated_Products_List");
        adapter = new ProductAdapter(MainActivity.this, R.layout.products_listview, productsArrayList);
        listView.setAdapter(adapter);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        outState.putString("ProductType", tv_SelectedProduct.getText().toString());
        outState.putString("TotalPrice", tv_Total.getText().toString());
        outState.putString("Quantity", tv_SelectedQuantity.getText().toString());
        outState.putInt("NumberPicker", numberPicker1.getValue());
        outState.putSerializable("List", productsArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        tv_SelectedProduct.setText(savedInstanceState.getString("ProductType"));
        tv_Total.setText(savedInstanceState.getString("TotalPrice"));
        tv_SelectedQuantity.setText(savedInstanceState.getString("Quantity"));
        numberPicker1.setValue(savedInstanceState.getInt("NumberPicker"));
        productsArrayList = (ArrayList<Product>) savedInstanceState.getSerializable("List");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_SelectedQuantity = findViewById(R.id.textView_QuantitySelected);
        tv_SelectedProduct = findViewById(R.id.textView_ProductType);
        tv_Total = findViewById(R.id.textView_TotalAmount);

        numberPicker1 = findViewById(R.id.numberPicker_Quantity);
        listView = findViewById(R.id.listView_ProductList);
        Button btn_Buy = findViewById(R.id.button_Buy);
        Button btn_Manager = findViewById(R.id.button_Manager);

        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(100);
        numberPicker1.setValue(0);
        numberPicker1.setFocusableInTouchMode(false);

        tv_SelectedQuantity.setText(String.valueOf(numberPicker1.getValue()));

        purchaseList = new ArrayList<>();
        productsArrayList = new ArrayList<>();
        productsArrayList.add(new Product("Pante", 20.44, 10));
        productsArrayList.add(new Product("Shoes", 10.44, 100));
        productsArrayList.add(new Product("Hats", 5.9, 30));

        adapter = new ProductAdapter(MainActivity.this, R.layout.products_listview, productsArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            tv_SelectedProduct.setText(productsArrayList.get(position).getName());
            for(int i = 0; i < productsArrayList.size(); i++)
            {
                if(tv_SelectedProduct.getText().toString().equals(productsArrayList.get(i).getName())){
                    double product_Total = Double.parseDouble(tv_SelectedQuantity.getText().toString()) * productsArrayList.get(i).getPrice();
                    if(product_Total == 0)
                        tv_Total.setText("0");
                    else
                        tv_Total.setText(String.format("%.2f",product_Total));
                }
            }
        });

        numberPicker1.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            tv_SelectedQuantity.setText(String.valueOf(newValue));
            if(!tv_SelectedProduct.getText().toString().equals("@string/Product_TextView"))
            {
                for(int i = 0; i < productsArrayList.size(); i++)
                {
                    if(tv_SelectedProduct.getText().toString().equals(productsArrayList.get(i).getName())){
                        double product_Total = Double.parseDouble(tv_SelectedQuantity.getText().toString()) * productsArrayList.get(i).getPrice();
                        if(product_Total == 0)
                            tv_Total.setText("0");
                        else
                            tv_Total.setText(String.format("%.2f",product_Total));
                        if(Integer.parseInt(tv_SelectedQuantity.getText().toString()) > productsArrayList.get(i).getQuantity() || productsArrayList.get(i).getQuantity() == 0)
                        {
                            Toast.makeText(MainActivity.this, "No enough quantity in the stock!!!",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
        });

        btn_Buy.setOnClickListener(view1 -> {
            int i,j = 0;
            if(tv_SelectedProduct.getText().equals("Product Type") || Double.parseDouble(tv_SelectedQuantity.getText().toString()) == 0){
                Toast.makeText(MainActivity.this, "All fields are required!!!", Toast.LENGTH_SHORT).show();
            }else {
                for (i = 0; i < productsArrayList.size(); i++) {
                    if (tv_SelectedProduct.getText().toString().equals(productsArrayList.get(i).getName())) {
                        if (Double.parseDouble(tv_SelectedQuantity.getText().toString()) <= productsArrayList.get(i).getQuantity() && Double.parseDouble(tv_SelectedQuantity.getText().toString()) > 0) {

                            int old_Quantity = productsArrayList.get(i).getQuantity();
                            int selected_Quantity = Integer.parseInt(tv_SelectedQuantity.getText().toString());

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setMessage("Your purchase is " + selected_Quantity + " " + tv_SelectedProduct.getText().toString() + " for " + tv_Total.getText().toString()).setTitle("Thank you for your purchase")
                                    .setCancelable(true)
                                    .create()
                                    .show();

                            String purchaseDate = Calendar.getInstance().getTime().toString();
                            for (j = 0; j < purchaseList.size(); j++){
                                if(purchaseList.get(j).getName().equals(productsArrayList.get(i).getName()))
                                {
                                    int tempQuantity = purchaseList.get(j).getQuantity();
                                    tempQuantity += selected_Quantity;
                                    purchaseList.get(j).setQuantity(tempQuantity);
                                    purchaseList.get(j).setTotalPrice(Double.parseDouble(tv_Total.getText().toString()));
                                    purchaseList.get(j).setPurchaseDate(purchaseDate);
                                    itemAdded = true;
                                }
                            }
                            if(j == purchaseList.size() && !itemAdded){
                                purchaseList.add(new History(productsArrayList.get(i).getName(), Double.parseDouble(tv_Total.getText().toString()), selected_Quantity, purchaseDate));
                            }
                            itemAdded = false;

                            int newQuantity = old_Quantity - selected_Quantity;
                            productsArrayList.get(i).setQuantity(newQuantity);
                        } else {
                            Toast.makeText(MainActivity.this, "No enough quantity in the stock!!!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });

        btn_Manager.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("Purchase_List", (Serializable) purchaseList);
            args.putSerializable("Products_List", (Serializable) productsArrayList);
            intent.putExtra("ManagerBundle",args);
            managerResult.launch(intent);
        });
    }
}