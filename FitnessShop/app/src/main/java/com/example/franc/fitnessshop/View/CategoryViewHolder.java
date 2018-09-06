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

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView categoryName;
    public ImageView picture;

    private WelcomeClickListener wcl;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        categoryName = (TextView)itemView.findViewById(R.id.cat_name);
        picture = (ImageView)itemView.findViewById(R.id.cat_image);

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
