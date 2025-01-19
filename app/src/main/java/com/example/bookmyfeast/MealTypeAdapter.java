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

public class MealTypeAdapter extends RecyclerView.Adapter<MealTypeAdapter.ViewHolder> {
    private final List<MealTypeModel> mealTypes;
    private final Context context;
    private final String phone;


    public MealTypeAdapter(List<MealTypeModel> mealTypes, Context context, String phone) {
        this.mealTypes = mealTypes;
        this.context = context;
        this.phone = phone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_type_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealTypeModel mealType = mealTypes.get(position);
        holder.imageButton.setImageResource(mealType.getImageResId());
        holder.label.setText(mealType.getLabel());
        holder.imageButton.setOnClickListener(v -> {
            Intent I = new Intent(context, RestaurantListActivity.class);
            I.putExtra("meal_type", mealType.getLabel());
            I.putExtra("phone", phone);
            context.startActivity(I);
        });
    }

    @Override
    public int getItemCount() {
        return mealTypes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        TextView label;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.item_img_btn);
            label = itemView.findViewById(R.id.item_label);
        }
    }
}