package com.example.franc.fitnessshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.fitnessshop.Databases.Database;
import com.example.franc.fitnessshop.Domain.Delivery;
import com.example.franc.fitnessshop.Domain.Order;
import com.example.franc.fitnessshop.Paypal.PaypalConfig;
import com.example.franc.fitnessshop.View.OrdersViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference req;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    FirebaseAuth auth;
    OrdersViewHolder.OrdersAdapter oAdapter;
    List<Order> orders = new ArrayList<Order>();
    TextView thePrice;
    Button submitOrder;
    private static final int REQ_CODE = 1;
    //using sandbox to test
    private static PayPalConfiguration configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PaypalConfig.CLIENT_ID);
    private Button clearCart;

    //Used to stop the PayPal service if the activity is destroyed
    @Override
    protected void onDestroy() {
        Intent stop = new Intent(CartActivity.this,PayPalService.class);
        stopService(stop);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //START PAYPAL service
        Intent service = new Intent(CartActivity.this,PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        startService(service);

        
        db = FirebaseDatabase.getInstance();
        req = db.getReference("Requests");


        
        rv = (RecyclerView)findViewById(R.id.orderList);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        thePrice = (TextView) findViewById(R.id.totalCost);
        submitOrder = (Button) findViewById(R.id.submitOrder);

        clearCart = (Button) findViewById(R.id.clear_cart);
        //set to final as declared from inner class
        //final FirebaseUser user = auth.getCurrentUser();

        //Handle click
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(CartActivity.this);

                //Call empty cart method
                db.emptyCart(user.getUid());
                finish();

                //Restart activity
                startActivity(getIntent());
            }
        });

        //Handle click
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create a dialog box
                AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);

                //Create a vertical linear layout to use within the dialog
                Context c = getBaseContext();
                LinearLayout linearLayout = new LinearLayout(c);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                //User will enter his address here
                final EditText address = new EditText(CartActivity.this);
                address.setHint("1st line of address");
                linearLayout.addView(address);

                //User will enter his postcode here
                final EditText postcode = new EditText(CartActivity.this);
                postcode.setHint("Postcode");
                linearLayout.addView(postcode);

                //Dialog Title
                dialog.setTitle("Enter details...");

                //Set dialog view
                dialog.setView(linearLayout);

                //On no selected
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Dismiss the dialog box
                        dialogInterface.dismiss();
                    }
                });

                //On yes selected
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Remove the comma from the price
                        String priceNoComma = thePrice.getText().toString().replaceAll(",","");

                        //Add required decimal point to price if needed
                        String withDecimal = priceNoComma.substring(0,priceNoComma.length()-2)+priceNoComma.substring(priceNoComma.length()-2);
                        withDecimal = withDecimal.substring(1);

                        //Create PayPal payment object
                        PayPalPayment payment = new PayPalPayment(new BigDecimal(withDecimal),"GBP","Payment for your order",PayPalPayment.PAYMENT_INTENT_SALE);

                        //Start PayPal payment
                        Intent pay = new Intent(CartActivity.this, PaymentActivity.class);
                        pay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
                        pay.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
                        startActivityForResult(pay,REQ_CODE);

                        //Store delivery information
                        Delivery aDelivery = new Delivery(user.getUid(),address.getText().toString(),postcode.getText().toString(),thePrice.getText().toString(),orders);

                        //Store delivery information in database
                        req.child(new Date().toString()).setValue(aDelivery);
                        Database db = new Database(CartActivity.this);

                        //Empty the cart on successful payment
                        db.emptyCart(user.getUid());


                    }
                });
                //Show the dialog box
                    AlertDialog a1 = dialog.create();
                     a1.show();
            }
        });

        //Show shopping trolley information
        initOrders(user.getUid());
    }

    private void initOrders(String userId) {

        //Retrieve order information
        orders = new Database(this).getShoppingCart(userId);


        oAdapter = new OrdersViewHolder.OrdersAdapter(orders,this);
        rv.setAdapter(oAdapter);

        int totalPrice = 0;

        //Iterate through order items and add up the prices
      for(Order o : orders){
          if(o.getQuantity() == null) {

          }else{
              totalPrice = totalPrice + (Integer.parseInt(o.getPrice())) * (Integer.parseInt(o.getQuantity()));
          }
      }

      //Set the price with the correct locale information
        Locale l = new Locale("en","GB");
        NumberFormat format = NumberFormat.getCurrencyInstance(l);

      thePrice.setText(format.format(totalPrice));
    }

    //Handle the result of the PayPal transaction
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Check if transaction is legitimate
        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(confirm != null){

                //Recieve PayPal transaction details
                JSONObject details = confirm.toJSONObject();

                //Create a dialog showing transaction details
                android.app.AlertDialog.Builder builder;
                builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Success");
                Log.e("y",details.toString());
                try {
                    builder.setMessage("You have sucessfully paid " + thePrice.getText().toString() + "\n\nTransctionID: " + details.getJSONObject("response").getString("id") + "\n\n" +
                            "State: " + details.getJSONObject("response").getString("state") );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                builder.setCancelable(true);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(getIntent());

                    }
                });

                android.app.AlertDialog a1 = builder.create();
                a1.show();

                //Lower stock of purchased items in database
                for(final Order o : orders){
                    DatabaseReference initialStock = FirebaseDatabase.getInstance().getReference("Items").child(o.getProductID()).child("Stock");
                    initialStock.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Retrieve stock level
                            String initStock = dataSnapshot.getValue().toString();

                            //Calculate new stock level
                            int leftover = Integer.parseInt(initStock)-Integer.parseInt(o.getQuantity());

                            //Change value in database
                            DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Items").child(o.getProductID()).child("Stock");
                            stockRef.setValue(Integer.toString(leftover));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
