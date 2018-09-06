package com.example.franc.fitnessshop;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.fitnessshop.Domain.Category;
import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.View.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.smooch.ui.ConversationActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE = 100;
    FirebaseDatabase db;
    DatabaseReference ref;
    Button logout;
    TextView name;
    private FirebaseAuth auth;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> fra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");

        setSupportActionBar(toolbar);


        //Check if user is currently logged in
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //Retrieve user details
        FirebaseUser user = auth.getCurrentUser();


        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Category");

        //Open/Close navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Handle items in navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Display user name in navigation drawer
        View navHeader = navigationView.getHeaderView(0);
        name = (TextView) navHeader.findViewById(R.id.name);
        if (user.getDisplayName() != null) {
            name.setText(user.getDisplayName());
        } else {
            name.setText("Hello");
        }

        //Set recycler view
        menu = (RecyclerView) findViewById(R.id.categories);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        menu.setLayoutManager(lm);

        //Initiate category Recycler View
        initMenu();
    }


    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.welcome_cards, CategoryViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, final int position) {

                //Set name
                viewHolder.categoryName.setText(model.getName());

                //Set image
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Handle click
                final Category clicked = model;
                viewHolder.setItemClickListener(new WelcomeClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {

                        //Create intent to move to Item lists
                        Intent showItems = new Intent(HomeActivity.this, Main2Activity.class);
                        showItems.putExtra("CategoryID", fra.getRef(position).getKey());
                        startActivity(showItems);
                    }
                });
            }
        };
        menu.setAdapter(fra);
    }


    //On back pressed close navigation drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Handles menubar inflation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //Used to select menubar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Retrieve the id of the menubar item selected
        int id = item.getItemId();

        //Navigate to the correct activity depending on what the user has selected
        if (id == R.id.action_cart) {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //Handle Navigation draw items selected
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            // Handle the camera action
        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_referral) {

            //Initiate Google Invites
            onInviteClicked();
        } else if (id == R.id.nav_help) {

            //Initiate RealTime Chat
            ConversationActivity.show(this);
        } else if (id == R.id.nav_nearby) {

            //Initiate Map functionality
            startActivity(new Intent(this, MapsActivity.class));
        } else if (id == R.id.logoutmenu) {

            //Logout current user
            auth.signOut();
            finish();

            //Return to initial screen
            startActivity(new Intent(this, MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void onInviteClicked() {

        //Create AppInvitation
        Intent intent = new AppInviteInvitation.IntentBuilder("Title")
                .setMessage("Hey this is a great app ...")
                .setDeepLink(Uri.parse("http://google.com"))
                //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText("Invite CTA")
                .build();
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            }

        }
    }
}
