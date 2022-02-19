package com.example.a2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class PurchasedProductAdapter extends RecyclerView.Adapter<PurchasedProductAdapter.ViewHolder>{

    public ArrayList<History> mProductsArrayList;

    public PurchasedProductAdapter(ArrayList<History> mProductsArrayList) {
        this.mProductsArrayList = mProductsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.products_listview,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getName().setText(mProductsArrayList.get(position).getName());
        holder.getTotalPrice().setText(Double.toString(mProductsArrayList.get(position).getTotalPrice()));
        holder.getQuantity().setText(Integer.toString(mProductsArrayList.get(position).getQuantity()));
        holder.date =mProductsArrayList.get(position).getPurchaseDate();
    }

    @Override
    public int getItemCount() {
        return mProductsArrayList.size() ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView totalPrice;
        TextView quantity;
        String date;

        public ViewHolder(@Nullable View view){
            super(view);
            name = view.findViewById(R.id.textView_ProductName);
            totalPrice = view.findViewById(R.id.textView_ProductQuantity);
            quantity = view.findViewById(R.id.textView_ProductPrice);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), HistoryDetailsActivity.class);
                    intent.putExtra("Name", name.getText().toString());
                    intent.putExtra("Price", totalPrice.getText().toString());
                    intent.putExtra("PurchaseDate", date);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public TextView getName() {
            return name;
        }

        public TextView getTotalPrice() {
            return totalPrice;
        }

        public TextView getQuantity() {
            return quantity;
        }
    }
}

