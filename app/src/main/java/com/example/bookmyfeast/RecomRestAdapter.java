package com.example.bookmyfeast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecomRestAdapter extends RecyclerView.Adapter<RecomRestAdapter.ViewHolder> {
    private final Context context;
    private final List<RecomRestModel> restaurantList;
    private final RestaurantClickListener listener;

    public interface RestaurantClickListener {
        void onRestaurantClick(RecomRestModel restaurant);
    }

    public RecomRestAdapter(Context context, List<RecomRestModel> restaurantList, RestaurantClickListener listener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recom_rest_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecomRestModel restaurant = restaurantList.get(position);
        holder.restName.setText(restaurant.getName());
        holder.restLoc.setText(restaurant.getLocality());
        holder.restImgBtn.setImageResource(restaurant.getPictureResId());
        holder.restImgBtn.setOnClickListener(v -> listener.onRestaurantClick(restaurant));
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton restImgBtn;
        TextView restName, restLoc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restImgBtn = itemView.findViewById(R.id.rest_img_btn);
            restName = itemView.findViewById(R.id.rest_name);
            restLoc = itemView.findViewById(R.id.rest_loc);
        }
    }
}
