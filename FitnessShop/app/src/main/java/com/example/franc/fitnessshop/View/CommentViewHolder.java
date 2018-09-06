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

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView comment;
    public TextView commenterName;
    public TextView commenterRating;




    private WelcomeClickListener wcl;

    public CommentViewHolder(View itemView) {
        super(itemView);

        comment = (TextView)itemView.findViewById(R.id.comments);
        commenterName = (TextView)itemView.findViewById(R.id.commenterName);
        commenterRating = (TextView)itemView.findViewById(R.id.commenterRating);



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
