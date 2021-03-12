package com.monstertechno.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Glide.with(this).load(R.drawable.start_page_image).into((ImageView)findViewById(R.id.imageView2));
    }

    public void OpenMainPage(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }
}