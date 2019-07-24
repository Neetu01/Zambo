package com.efficientindia.zambopay.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.efficientindia.zambopay.Model.Griditem;
import com.efficientindia.zambopay.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class Recycleradapterrecharge extends RecyclerView.Adapter<Recycleradapterrecharge.Myholder> {

    List<Griditem> list;
    Context context;

    public Recycleradapterrecharge(Context context, List<Griditem> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_customer,null);
        Myholder myholder=new Myholder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        Griditem griditem=list.get(position);
        holder.textView.setText(griditem.getName());
        Picasso.with(context).load(String.valueOf(griditem.getImage())).into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class Myholder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView textView;
        public Myholder(View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.item_image);
            textView=itemView.findViewById(R.id.item_text);
        }
    }
}
