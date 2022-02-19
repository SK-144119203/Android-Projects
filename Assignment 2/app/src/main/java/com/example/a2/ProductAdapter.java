package com.example.a2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, int resourceId, ArrayList<Product> products){
        super(context,resourceId, products);
    }

    public static class ViewHolder{
        TextView name;
        TextView price;
        TextView quantity;

        public ViewHolder(View view){
            name = view.findViewById(R.id.textView_ProductName);
            price = view.findViewById(R.id.textView_ProductPrice);
            quantity = view.findViewById(R.id.textView_ProductQuantity);
        }
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.products_listview,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(getItem(position).getName());
        viewHolder.price.setText(Double.toString(getItem(position).getPrice()));
        viewHolder.quantity.setText(Integer.toString(getItem(position).getQuantity()));

        return convertView;
    }
}
