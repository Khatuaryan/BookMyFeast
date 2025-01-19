package com.example.bookmyfeast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PopCuisineAdapter extends RecyclerView.Adapter<PopCuisineAdapter.CuisineViewHolder> {
    private final List<PopCuisineModel> cuisineList;
    private final Context context;
    private final String phone;

    public PopCuisineAdapter(List<PopCuisineModel> cuisineList, Context context, String phone) {
        this.cuisineList = cuisineList;
        this.context = context;
        this.phone = phone;
    }

    @NonNull
    @Override
    public CuisineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_cuisine_items, parent, false);
        return new CuisineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisineViewHolder holder, int position) {
        PopCuisineModel cuisine = cuisineList.get(position);
        holder.cuisineLabel.setText(cuisine.getName());
        holder.cuisineImgBtn.setImageResource(cuisine.getImageResourceId());
        holder.cuisineImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestaurantListActivity.class);
            intent.putExtra("cuisine_type", cuisine.getName());
            intent.putExtra("phone", phone);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cuisineList.size();
    }

    static class CuisineViewHolder extends RecyclerView.ViewHolder {
        ImageButton cuisineImgBtn;
        TextView cuisineLabel;

        public CuisineViewHolder(@NonNull View itemView) {
            super(itemView);
            cuisineImgBtn = itemView.findViewById(R.id.cuisine_img_btn);
            cuisineLabel = itemView.findViewById(R.id.cuisine_label);
        }
    }
}