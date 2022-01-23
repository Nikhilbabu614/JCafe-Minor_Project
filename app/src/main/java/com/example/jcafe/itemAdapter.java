package com.example.jcafe;

import static android.content.Context.MODE_PRIVATE;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewholder> {
    Context context;
    ArrayList<ItemClass> list;


    public itemAdapter(Context context, ArrayList<ItemClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public itemAdapter.itemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemViewholder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull itemAdapter.itemViewholder holder, int position) {
        ItemClass itemClass = list.get(position);
        holder.t1.setText(itemClass.getItemName());
        int x = (int) (Integer.parseInt(itemClass.getItemPrice())*(1.4));
        holder.t2.setText(""+x);
        holder.t2.setPaintFlags(holder.t2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.t3.setText(itemClass.getItemPrice());
        Glide.with(context).load(itemClass.getItemImage()).into(holder.imageView);

        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                    if(ordersHelper.getOrderID().equals(sharedPreferences.getString("orderID",""))) {
                        ArrayList<itemcount> kup = ordersHelper.getItemcountArrayList();
                        for (int i = 0; i < kup.size(); i++) {
                            if (kup.get(i).getItemid().equals(itemClass.getItemId())) {
                                holder.count.setText("" + kup.get(i).count);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.addb.setOnClickListener(v->{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                        if(ordersHelper.getOrderID().equals(sharedPreferences.getString("orderID",""))) {
                            ArrayList<itemcount> kup = ordersHelper.getItemcountArrayList();
                            for (int i = 0; i < kup.size(); i++) {
                                if (kup.get(i).getItemid().equals(itemClass.getItemId())) {
                                    kup.get(i).count++;
                                }
                            }
                            ref.child(sharedPreferences.getString("orderID","")).child("itemcountArrayList").setValue(kup);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        holder.subb.setOnClickListener(v->{
           ref.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                       OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                       if(ordersHelper.getOrderID().equals(sharedPreferences.getString("orderID",""))) {
                           ArrayList<itemcount> kup = ordersHelper.getItemcountArrayList();
                           for(int i=0;i<kup.size();i++){
                               if(kup.get(i).getItemid().equals(itemClass.getItemId())){
                                   if(kup.get(i).count>0)kup.get(i).count--;
                               }
                           }
                           ref.child(sharedPreferences.getString("orderID","")).child("itemcountArrayList").setValue(kup);
                       }
                   }
               }
               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class itemViewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3;
        ImageView imageView;

        TextView count;
        ImageButton addb,subb;
        public itemViewholder(@NonNull View itemView) {
            super(itemView);
            t1= itemView.findViewById(R.id.textView6);
            t2= itemView.findViewById(R.id.textView8);
            t3= itemView.findViewById(R.id.textView9);
            imageView=itemView.findViewById(R.id.imageView3);

            addb=itemView.findViewById(R.id.imageButton2);
            subb=itemView.findViewById(R.id.imageButton3);
            count=itemView.findViewById(R.id.textView7);

        }
    }
}

