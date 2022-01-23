package com.example.jcafe;

import static com.example.jcafe.R.color.browser_actions_bg_grey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView t1 = findViewById(R.id.textView2);
        TextView t2 = findViewById(R.id.textView3);
        ImageView imageView = findViewById(R.id.imageView2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(imageView);
        t1.setText(user.getDisplayName());
        t2.setText(user.getEmail());
        findViewById(R.id.button).setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });
    }
}