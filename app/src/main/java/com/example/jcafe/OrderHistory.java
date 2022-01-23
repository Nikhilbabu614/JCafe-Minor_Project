package com.example.jcafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistory extends AppCompatActivity {
    ListView lv;
    TextView t1,t2,t3;
    EditText e1;
    Button btn;
    ArrayList<String> mylist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        t1=findViewById(R.id.textView10forbtn);
        t2=findViewById(R.id.textView12);
        t3=findViewById(R.id.textView14);
        e1=findViewById(R.id.editTextTextPersonName);
        btn=findViewById(R.id.button2forplaceorder);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrdersHelper hp = dataSnapshot.getValue(OrdersHelper.class);
                    if(hp.getOrderID().equals(sharedPreferences.getString("orderID",""))){
                        if(hp.getStatus().equals("rejected") || hp.getStatus().equals("completed")){
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("orderID","");
                            myEdit.commit();
                            Toast.makeText(getApplicationContext(), "Place New Order", Toast.LENGTH_SHORT).show();
                            Intent refresh = new Intent(getApplicationContext(),MenuActivity.class);
                            startActivity(refresh);//Start the same Activity
                            finish(); //finish Activity.
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> myad = new ArrayAdapter<String>(OrderHistory.this, android.R.layout.simple_list_item_1,mylist);
        lv = findViewById(R.id.listview);
        lv.setAdapter(myad);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        OrdersHelper hp = dataSnapshot.getValue(OrdersHelper.class);
//                        Toast.makeText(getApplicationContext(), "" + snapshot.getValue(OrdersHelper.class).getOrderID(), Toast.LENGTH_SHORT).show();
                    OrdersHelper ordersHelper = snapshot.getValue(OrdersHelper.class);
                    if(ordersHelper.getOrderID().equals(sharedPreferences.getString("orderID",""))) {
                        if (ordersHelper.getStatus().equals("placed")
                        || ordersHelper.getStatus().equals("accepted")){
                            t1.setText("PLACED ORDERS");
                            t2.setText("STATUS: Order Placed \n OID: " + ordersHelper.getOrderID().substring(0, 10));
                            e1.setVisibility(View.GONE);
                            btn.setVisibility(View.GONE);
                        }
                        ArrayList<itemcount> al = ordersHelper.getItemcountArrayList();
                        for (int i = 0; i < al.size(); i++) {
                            if (al.get(i).getCount() > 0) {
                                String ans = al.get(i).getItemid();
                                StringBuffer sb = new StringBuffer(ans);
                                sb.deleteCharAt(sb.length() - 1);
                                mylist.add(sb.substring(0, 1).toUpperCase() + sb.substring(1) + "  -  " + al.get(i).getCount());
                            }
                        }
                    }
//                }
                myad.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myad.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("items");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrdersHelper ordersHelper = dataSnapshot.getValue(OrdersHelper.class);
                    if (ordersHelper.getOrderID().equals(sharedPreferences.getString("orderID", ""))) {
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
                                                int k = Integer.parseInt(t3.getText().toString());
                                                int m = Integer.parseInt(item.getItemPrice()) * al.get(finalI).getCount();
                                                t3.setText((k + m) + "");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener(v->{
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        OrdersHelper hp = dataSnapshot.getValue(OrdersHelper.class);
                        if(hp.getOrderID().equals(sharedPreferences.getString("orderID",""))){
                            ref.child(hp.getOrderID()).child("snum").setValue(e1.getText().toString());
                            ref.child(hp.getOrderID()).child("status").setValue("placed");
                            Intent refresh = new Intent(getApplicationContext(),OrderHistory.class);
                            startActivity(refresh);//Start the same Activity
                            finish(); //finish Activity.
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }
}