package com.example.franc.fitnessshop;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class SearchActivity extends AppCompatActivity {

    RecyclerView items;
    RecyclerView.LayoutManager lm;
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<Item,ItemViewHolder> fra;
    String s;
    private SearchView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Items");

        items = (RecyclerView) findViewById(R.id.items);
        items.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        items.setLayoutManager(lm);

        sv = (SearchView) findViewById(R.id.search2);

        //Listens for text entered into the search bar
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //When the submit button is clicked
            @Override
            public boolean onQueryTextSubmit(String query) {
                s = query;

                //change the first letter to UpperCase
                if(s.length() != 0)
                s = s.substring(0,1).toUpperCase()+s.substring(1);

                //Initialise RecyclerView
                initItems(s);

                return true;
            }

            //When text is entered
            @Override
            public boolean onQueryTextChange(String newText) {
                s = newText;
                if(s.length() != 0)
                    s = s.substring(0,1).toUpperCase()+s.substring(1);
                initItems(s);
                return true;
            }
        });

    }

    private void initItems(String str) {
        fra = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class,R.layout.item_list,ItemViewHolder.class,ref.orderByChild("Name").startAt(str).endAt(str+"\uf8ff")) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, final int position) {
                //Set name
                viewHolder.itemName.setText(model.getName());

                //Set image
                Picasso.with(SearchActivity.this.getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Set price
                viewHolder.price.setText("£" + model.getPrice());

                //Set stock level detail
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

                //Set number of raters
                viewHolder.numRaters.setText("(" + model.getCount() + ")");

                //Handle click
                final Item clicked = model;
                viewHolder.setItemClickListener(new WelcomeClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {
                        Intent detail = new Intent(SearchActivity.this,ItemDetailActivity.class);
                        detail.putExtra("ItemId",fra.getRef(position).getKey());
                        startActivity(detail);
                    }
                });
            }
        };

        items.setAdapter(fra);
    }

}
