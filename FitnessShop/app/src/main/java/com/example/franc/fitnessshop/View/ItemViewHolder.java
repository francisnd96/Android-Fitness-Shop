package com.example.franc.fitnessshop.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.R;

/**
 * Created by franc on 22/11/2017.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemName;
    public ImageView picture;
    public TextView price;

    private WelcomeClickListener wcl;

    public ItemViewHolder(View itemView) {
        super(itemView);

        itemName = (TextView)itemView.findViewById(R.id.item_name);
        picture = (ImageView)itemView.findViewById(R.id.item_image);
        price = (TextView)itemView.findViewById(R.id.item_price);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(WelcomeClickListener wcl){
        this.wcl = wcl;
    }

    @Override
    public void onClick(View v) {
        wcl.onClick(v,getAdapterPosition(),false);
    }
}