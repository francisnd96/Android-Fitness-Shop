package com.example.franc.fitnessshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.franc.fitnessshop.Domain.Item;
import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.View.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by franc on 14/03/2018.
 */

public class Women extends Fragment {

    RecyclerView items;
    RecyclerView.LayoutManager lm;

    FirebaseDatabase db;
    DatabaseReference ref;
    String catId = "";
    FirebaseRecyclerAdapter<Item,ItemViewHolder> fra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_list, container, false);
        String catId = getArguments().getString("id");
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Items");
        items = (RecyclerView) rootView.findViewById(R.id.items);
        // items.setHasFixedSize(true);
        lm = new LinearLayoutManager(getContext());
        rootView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT));
        items.setLayoutManager(lm);

        fra = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class,R.layout.item_list,ItemViewHolder.class,ref.orderByChild("Gender").equalTo(catId+"Women")) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, final int position) {
                viewHolder.itemName.setText(model.getName());
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(viewHolder.picture);
                viewHolder.price.setText("£" + model.getPrice());
                if(Integer.parseInt(model.getStock()) == 0){
                    viewHolder.inStock.setText("Out of stock");
                }else if(Integer.parseInt(model.getStock()) < 5){
                    viewHolder.inStock.setText("Less than 5 left!");
                    viewHolder.inStock.setTextSize(1,12);
                }else if(Integer.parseInt(model.getStock()) < 10){
                    viewHolder.inStock.setText("Less than 10 left!");
                    viewHolder.inStock.setTextSize(1,12);
                }else{
                    viewHolder.inStock.setText("");
                }
                viewHolder.rating.setRating(Float.parseFloat(model.getRating()));
                viewHolder.numRaters.setText("(" + model.getCount() + ")");
                final Item clicked = model;
                viewHolder.setItemClickListener(new WelcomeClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {
                        Intent detail = new Intent(getActivity(),ItemDetailActivity.class);
                        detail.putExtra("ItemId",fra.getRef(position).getKey());
                        startActivity(detail);
                    }
                });
            }
        };

        items.setAdapter(fra);
        return rootView;
    }
}
