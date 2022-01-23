package com.example.jcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    itemAdapter itemAdap;
    ArrayList<ItemClass> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        findViewById(R.id.ordershistory).setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),OrderHistory.class);
            startActivity(intent);
        });
        findViewById(R.id.profile).setOnClickListener(v->{
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        findViewById(R.id.textView5).setOnClickListener(v -> {
            if(user.getEmail().equals("kendrasuraj@gmail.com")){
                Toast.makeText(getApplicationContext(), "Welocme Admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,AdminActivity.class);
                startActivity(intent);
            }
        });



        String uniqueID = UUID.randomUUID().toString();
        if(sharedPreferences.getString("orderID","").equals("")){
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("orderID",uniqueID);
            myEdit.commit();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders").child(sharedPreferences.getString("orderID",""));
            ArrayList<itemcount> itemcountArrayList=new ArrayList<>();
            itemcountArrayList.add(new itemcount("manchuriya1",0));
            itemcountArrayList.add(new itemcount("vegrice2",0));
            itemcountArrayList.add(new itemcount("vegnoodles3",0));
            itemcountArrayList.add(new itemcount("chickenfried4",0));
            itemcountArrayList.add(new itemcount("chickennoodles5",0));
            itemcountArrayList.add(new itemcount("beverage6",0));
            itemcountArrayList.add(new itemcount("fruit7",0));
            itemcountArrayList.add(new itemcount("frankie8",0));
            itemcountArrayList.add(new itemcount("bottle9",0));
            OrdersHelper helper = new OrdersHelper(sharedPreferences.getString("orderID","")
                    ,user.getDisplayName(),"","","started",itemcountArrayList);
            ref.setValue(helper);
        }

        recyclerView=findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        itemAdap = new itemAdapter(this,list);
        recyclerView.setAdapter(itemAdap);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ItemClass item = dataSnapshot.getValue(ItemClass.class);
                    list.add(item);
                }
                itemAdap.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}