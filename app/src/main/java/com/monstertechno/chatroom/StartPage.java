package com.monstertechno.chatroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import static com.monstertechno.chatroom.cords.FirebaseCords.MAIN_CHAT_DATABASE;

public class StartPage extends AppCompatActivity {

    TextView notification_count;


    @Override
    protected void onStart() {
        super.onStart();
        long lastClickTime  = new SaveState(this).getClickTime();
        Query query = MAIN_CHAT_DATABASE.orderBy("timestamp", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("timestamp",lastClickTime);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getDocuments().size()==0){
                    notification_count.setVisibility(View.GONE);
                }else {
                    notification_count.setVisibility(View.VISIBLE);
                }
                notification_count.setText(""+value.getDocuments().size());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Glide.with(this).load(R.drawable.start_page_image).into((ImageView)findViewById(R.id.imageView2));


        notification_count = findViewById(R.id.notification_count);

    }

    public void OpenMainPage(View view) {
        long currentTime  = new Date().getTime();
        new SaveState(this).setClickTime(currentTime);
        notification_count.setVisibility(View.GONE);
        startActivity(new Intent(this,MainActivity.class));
    }
}