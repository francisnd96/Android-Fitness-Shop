package com.example.franc.fitnessshop;

/**
 * Created by franc on 14/03/2018.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franc.fitnessshop.Domain.Item;
import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.View.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

//Fragment used when the user selects the all Tab
public class All extends Fragment{

    RecyclerView items;
    RecyclerView.LayoutManager lm;
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<Item,ItemViewHolder> fra;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_list, container, false);

        //Retrieve the category ID
        String catId = getArguments().getString("id");

        //Create DB instance
        db = FirebaseDatabase.getInstance();

        //Search DB for all fields under "Items"
        ref = db.getReference("Items");

        //Select the recyclerView
        items = (RecyclerView) rootView.findViewById(R.id.items);
        lm = new LinearLayoutManager(getContext());

        //Give the recycler view the correct layout (all the way to the edges)
        rootView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT));

        //Give the recycler view the correct layout properties
        items.setLayoutManager(lm);

        //Retrieve items from the database
        fra = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class,R.layout.item_list,ItemViewHolder.class,ref.orderByChild("CategoryId").equalTo(catId)) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, final int position) {
                //Set name
                viewHolder.itemName.setText(model.getName());

                //Set image
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Set price
                viewHolder.price.setText("£" + model.getPrice());

                //Set stock alert
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

                //Set rating
                viewHolder.rating.setRating(Float.parseFloat(model.getRating()));

                //Set number of ratings
                viewHolder.numRaters.setText("(" + model.getCount() + ")");
                final Item clicked = model;

                //Handle click
                viewHolder.setItemClickListener(new WelcomeClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {

                        //Create intent to go to ItemDetail page with the correct ItemID information
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
