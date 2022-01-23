package com.example.jcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNewOrdersActivity extends AppCompatActivity {
    Myad mad;
    ArrayList<OrdersHelper> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        RecyclerView rc = findViewById(R.id.recycle34);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        rc.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        mad = new Myad(this,list);
        rc.setAdapter(mad);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OrdersHelper item = dataSnapshot.getValue(OrdersHelper.class);
                    if(item.getStatus().equals("accepted"))list.add(item);
                }
                mad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}