package com.example.franc.fitnessshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.franc.fitnessshop.Domain.Category;
import com.example.franc.fitnessshop.Domain.Item;
import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.View.CategoryViewHolder;
import com.example.franc.fitnessshop.View.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView items;
    RecyclerView.LayoutManager lm;

    FirebaseDatabase db;
    DatabaseReference ref;
    String catId = "";

    FirebaseRecyclerAdapter<Item,ItemViewHolder> fra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Items");

        items = (RecyclerView) findViewById(R.id.items);
        items.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        items.setLayoutManager(lm);

        if(getIntent() != null){
            catId = getIntent().getStringExtra("CategoryID");
        }
        if(!catId.isEmpty() && catId != null){
            initItems(catId);
        }


    }

    private void initItems(String catId) {
        fra = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class,R.layout.item_list,ItemViewHolder.class,ref.orderByChild("CategoryId").equalTo(catId)) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, final int position) {
                viewHolder.itemName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.picture);
                viewHolder.price.setText("£" + model.getPrice());
                final Item clicked = model;
                viewHolder.setItemClickListener(new WelcomeClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {
                        Intent detail = new Intent(ItemListActivity.this,ItemDetailActivity.class);
                        detail.putExtra("ItemId",fra.getRef(position).getKey());
                        startActivity(detail);
                    }
                });
            }
        };

        items.setAdapter(fra);
    }
}
