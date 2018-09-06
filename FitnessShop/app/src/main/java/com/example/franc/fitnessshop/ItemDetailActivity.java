package com.example.franc.fitnessshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.fitnessshop.Databases.Database;
import com.example.franc.fitnessshop.Domain.Item;
import com.example.franc.fitnessshop.Domain.Order;
import com.example.franc.fitnessshop.Domain.StarRating;
import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.View.CommentViewHolder;
import com.example.franc.fitnessshop.View.ItemViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;
import java.util.Date;

public class ItemDetailActivity extends AppCompatActivity implements RatingDialogListener {

    TextView price, name, description;
    ImageView image;
    CollapsingToolbarLayout ctl;
    FloatingActionButton btn,rate;
    static String quantity;
    FirebaseAuth auth;
    FirebaseUser user;


    String id = "";

    FirebaseDatabase db;
    DatabaseReference ref,ratingRef;
    Item item;
    TextView stock;
    static String stockNum;
    private FloatingActionButton share;
    FirebaseRecyclerAdapter<StarRating,CommentViewHolder> fra;



    CallbackManager cbm;
    ShareDialog sd;

    //Target object used to share to Facebook
    Target target = new Target(){

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto pic = new SharePhoto.Builder().setBitmap(bitmap).build();
            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(pic).build();
                sd.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    private RatingBar ratingBar;
    private RecyclerView comments;
    private LinearLayoutManager lm;
    private DatabaseReference stockRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        cbm = CallbackManager.Factory.create();
        sd = new ShareDialog(this);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Items");
        stockRef = db.getReference("stock");
        ratingRef = db.getReference("StarRating");

        btn = (FloatingActionButton)findViewById(R.id.trolley);
        description = (TextView) findViewById(R.id.description);
        name = (TextView) findViewById(R.id.itemDetailName);
        price = (TextView) findViewById(R.id.itemPrice);
        image = (ImageView) findViewById(R.id.itemPicture);
        stock = (TextView) findViewById(R.id.stock_num);
        share = (FloatingActionButton) findViewById(R.id.share);
        rate = (FloatingActionButton) findViewById(R.id.rate);
        ratingBar = (RatingBar)findViewById(R.id.ratingbar);
        comments = (RecyclerView) findViewById(R.id.commentsList);
        lm = new LinearLayoutManager(this);
        comments.setLayoutManager(lm);

        //Handle click
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRating();
            }
        });

        final TextView tv = (TextView) findViewById(R.id.tv);
        NumberPicker np = (NumberPicker) findViewById(R.id.np);

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Display the value of the number picker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                quantity = Integer.toString(newVal);
                tv.setText("Quantity: " + newVal);
            }
        });



        ctl = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar);

        //Set Toolbar Appearance on expansion
        ctl.setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Display1);

        //Set Toolbar Appearance on collapse
        ctl.setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Display2);


        //Handle click
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If no quantity has been selected
                if(quantity == null){
                    Toast.makeText(ItemDetailActivity.this, "Please select a quantity",Toast.LENGTH_SHORT).show();

                //If the stock level is insufficient
                }else if(Integer.parseInt(quantity) > Integer.parseInt(stockNum)){
                    Toast.makeText(ItemDetailActivity.this, "Insufficient Stock",Toast.LENGTH_SHORT).show();
                }else {

                //Add to database
                    new Database(getBaseContext()).putInCart(new Order(
                            id, quantity, item.getPrice(), item.getDiscount(), item.getName(),user.getUid()
                    ));

                    Toast.makeText(ItemDetailActivity.this, "Added to your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });




        if(getIntent() != null){
            //Retrieve the Item ID
            id = getIntent().getStringExtra("ItemId");
        }

        //Populate comments
        if(!id.isEmpty() && id != null){
            fra = new FirebaseRecyclerAdapter<StarRating, CommentViewHolder>(StarRating.class,R.layout.comment_card,CommentViewHolder.class,ratingRef.orderByChild("id").equalTo(id)) {
                @Override
                protected void populateViewHolder(CommentViewHolder viewHolder, StarRating model, final int position) {

                    //Set comment
                    viewHolder.comment.setText(model.getComment());

                    //Set Rating
                    viewHolder.commenterRating.setText(model.getRating() + "/5");

                    //Set Commenter Name
                    viewHolder.commenterName.setText(model.getDisplayName());
                }

            };
            comments.setAdapter(fra);
            getRating(id);
            initItems(id);


        }


    }

    //Calculate an items Rating
    private void getRating(final String id) {
        //Locate the item in the database
        Query itemRating = ratingRef.orderByChild("id").equalTo(id);
        itemRating.addValueEventListener(new ValueEventListener() {
            int count = 0;
            int total = 0;

            //Any time a new rating is added
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){

                    //Retrieve information regarding that item and store it in Star Rating object
                    StarRating rating = item.getValue(StarRating.class);

                    //Calculate total of all rating given to item
                    total+=Integer.parseInt(rating.getRating());

                    //Count number of times the item has been rated
                    count++;
                }
                if(count != 0) {

                    //Calculate the average
                    float avg = total / count;

                    //Set the average to the rating bar
                    ratingBar.setRating(avg);

                    //Store information in the Database
                    DatabaseReference storeRating = FirebaseDatabase.getInstance().getReference("Items").child(id).child("Rating");
                    storeRating.setValue(Float.toString(avg));
                    DatabaseReference countRatings = FirebaseDatabase.getInstance().getReference("Items").child(id).child("Count");
                    countRatings.setValue(Integer.toString(count));

                    count = 0;
                    comments.setAdapter(fra);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Initialise the rating dialog box
    private void initRating() {
        new AppRatingDialog.Builder().setPositiveButtonText("Rate").setNegativeButtonText("Cancel").setNoteDescriptions(Arrays.asList("Terrible", "Not Good", "OK", "Very Good", "Fantastic")).setDefaultRating(3).setTitle("Rate this product").setDescription("Please rate this item give your feedback").setHint("Comment here...").setWindowAnimation(R.style.RatingAnimation).create(ItemDetailActivity.this).show();
    }

    //Present the item detail
    private void initItems(final String id){
        Log.e("id",id);



        ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(Item.class);

                //Set Image
                Picasso.with(getBaseContext()).load(item.getImage()).into(image);

                //Set Name
                ctl.setTitle(item.getName());

                //Set Price
                price.setText(item.getPrice());
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext()).load(item.getImage()).into(target);

                    }
                });

                //Set Name
                name.setText(item.getName());

                //Set Description
                description.setText(item.getDescription());

                //Set stock
                stock.setText(item.getStock());
                stockNum = item.getStock();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Close rating dialog box
    @Override
    public void onNegativeButtonClicked() {
    }

    //When rate is pressed
    @Override
    public void onPositiveButtonClicked(final int star, String comment) {

        //Create a star rating object
        final StarRating rate = new StarRating(user.getUid(),id,String.valueOf(star),comment,user.getDisplayName());

        //Save it in the database with its date/time
        ratingRef.child(new Date().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 ratingRef.child(new Date().toString()).setValue(rate);
                Toast.makeText(ItemDetailActivity.this,"Thank you for your feedback!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
