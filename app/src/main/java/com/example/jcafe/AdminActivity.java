package com.example.jcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    cadapter cad;
    ArrayList<OrdersHelper> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ImageButton btn = findViewById(R.id.imageButtonfora);

        btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),AdminNewOrdersActivity.class);
            startActivity(intent);
        });

        RecyclerView recyclerView=findViewById(R.id.recyclerView2);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        cad = new cadapter(this,list);
        recyclerView.setAdapter(cad);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OrdersHelper item = dataSnapshot.getValue(OrdersHelper.class);
                    if(item.getStatus().equals("placed"))list.add(item);
                }
                cad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}