package com.example.jcafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class  cadapter extends RecyclerView.Adapter<cadapter.itemViewholder>{

    Context context;
    ArrayList<OrdersHelper> list;


    public cadapter(Context context, ArrayList<OrdersHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public cadapter.itemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new cadapter.itemViewholder(LayoutInflater.from(context).inflate(R.layout.currentorders,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull cadapter.itemViewholder holder, int position) {
        OrdersHelper itemClass = list.get(position);
        holder.t1.setText("OID: "+itemClass.getOrderID().substring(0,10));
        holder.t2.setText("Name: "+itemClass.getCname());
        holder.t3.setText("Phone: "+itemClass.getSnum());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("items");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                if(ordersHelper.getOrderID().equals(itemClass.getOrderID())) {
                    ArrayList<itemcount> al = ordersHelper.getItemcountArrayList();
                    String solu = "";
                    for (int i = 0; i < al.size(); i++) {
                        if (al.get(i).getCount() > 0) {
                            String ans = al.get(i).getItemid();
                            StringBuffer sb = new StringBuffer(ans);
                            sb.deleteCharAt(sb.length() - 1);
                            solu += sb.substring(0, 1).toUpperCase() + sb.substring(1) + "  -  " + al.get(i).getCount() + "\n";

                            int finalI = i;
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        ItemClass item = dataSnapshot.getValue(ItemClass.class);
                                        if (item.getItemId().equals(al.get(finalI).getItemid())) {
                                            int k = Integer.parseInt(holder.t4.getText().toString());
                                            int m = Integer.parseInt(item.getItemPrice()) * al.get(finalI).getCount();
                                            holder.t4.setText((k + m) + "");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                    holder.lv.setText(solu);
                }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.ac.setOnClickListener(v->{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                        if(ordersHelper.getOrderID().equals(itemClass.getOrderID())){
                            ref.child(ordersHelper.getOrderID()).child("status").setValue("accepted");
                            Intent intent1=new Intent(context,AdminActivity.class);
                            context.startActivity(intent1);
                            ((Activity)context).finish();                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        holder.re.setOnClickListener(v->{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                        if(ordersHelper.getOrderID().equals(itemClass.getOrderID())){
                            ref.child(ordersHelper.getOrderID()).child("status").setValue("rejected");
                            notifyDataSetChanged();
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
        TextView t1,t2,t3,t4;
        Button ac,re;
        TextView lv;
        public itemViewholder(@NonNull View itemView) {
            super(itemView);
            t1= itemView.findViewById(R.id.textView15);
            t2= itemView.findViewById(R.id.textView16);
            t3= itemView.findViewById(R.id.textView17);
            t4= itemView.findViewById(R.id.textView18);
            lv=itemView.findViewById(R.id.listview2jjj);
            ac= itemView.findViewById(R.id.button2);
            re= itemView.findViewById(R.id.button3);
        }
    }
}
