package com.example.franc.fitnessshop.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.franc.fitnessshop.Domain.Order;
import com.example.franc.fitnessshop.Interface.WelcomeClickListener;
import com.example.franc.fitnessshop.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by franc on 22/02/2018.
 */

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView orderItem, orderItemPrice;
    public ImageView count;

    private WelcomeClickListener wcl;

    public void setOrderItemName(TextView name){
        orderItem = name;
    }

    public OrdersViewHolder(View itemView) {
        super(itemView);
        orderItem = (TextView)itemView.findViewById(R.id.orderItem);
        orderItemPrice = (TextView)itemView.findViewById(R.id.orderItemPrice);
        count = (ImageView)itemView.findViewById(R.id.count);

    }

    @Override
    public void onClick(View view) {

    }

    public static class OrdersAdapter extends RecyclerView.Adapter<OrdersViewHolder>{

        private List<Order> data = new ArrayList<>();
        private Context context;

        public OrdersAdapter(List<Order> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.orders_adapter,parent,false);
            return new OrdersViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrdersViewHolder holder, int position) {
            TextDrawable draw = TextDrawable.builder().buildRound(""+data.get(position).getQuantity(), Color.MAGENTA);
            holder.count.setImageDrawable(draw);
           // Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.picture);

            int total = (Integer.parseInt(data.get(position).getPrice()))*(Integer.parseInt(data.get(position).getQuantity()));

            Locale l = new Locale("en","GB");
            NumberFormat format = NumberFormat.getCurrencyInstance(l);

            holder.orderItemPrice.setText(format.format(total));
            holder.orderItem.setText(data.get(position).getProductName());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
