package com.monstertechno.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.monstertechno.chatroom.fcm.SendPushNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.monstertechno.chatroom.cords.FirebaseCords.MAIN_CHAT_DATABASE;
import static com.monstertechno.chatroom.cords.FirebaseCords.mAuth;

public class ImageUploadPreview extends AppCompatActivity {

    ImageView upload_image_container;
    EditText chat_box;
    Uri imageUri;

    ProgressDialog dialog;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_preview);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        imageUri = Uri.parse(getIntent().getStringExtra("image_uri"));
        upload_image_container = findViewById(R.id.upload_image_container);
        chat_box = findViewById(R.id.chat_box);

        upload_image_container.setImageURI(imageUri);

    }

    public void addMessage(View view) {
        dialog.setMessage("Uploading Image...");
        dialog.show();
        /*Generate messageID using the current date. */
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String messageID = format.format(today);

        storageReference = FirebaseStorage.getInstance().getReference().child("chat_image");
        StorageReference imgPath = storageReference.child(messageID+".jpg");
        imgPath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    dialog.setMessage("Finalizing Data...");
                    imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            addMessageToTheDatabase(uri,messageID);
                        }
                    });
                }else {
                    Toast.makeText(ImageUploadPreview.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addMessageToTheDatabase(Uri uri, String messageID) {
        FirebaseUser user = mAuth.getCurrentUser();
        String message = chat_box.getText().toString();
        if(TextUtils.isEmpty(message)){
            message = "\uD83D\uDCF7";
        }

        /*Getting user image from Google account*/
        String user_image_url = "";
        Uri photoUrl = user.getPhotoUrl();
        String originalUrl = "s96-c/photo.jpg";
        String resizeImageUrl = "s400-c/photo.jpg";
        if(photoUrl!=null){
            String photoPath = photoUrl.toString();
            user_image_url = photoPath.replace(originalUrl,resizeImageUrl);
        }

        HashMap<String,Object> messageObj = new HashMap<>();
        messageObj.put("message",message);
        messageObj.put("user_name",user.getDisplayName());
        messageObj.put("timestamp", FieldValue.serverTimestamp());
        messageObj.put("messageID",messageID);
        messageObj.put("chat_image",uri.toString());
        messageObj.put("user_image_url",user_image_url);

        String finalMessage = message;
        MAIN_CHAT_DATABASE.document(messageID).set(messageObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("global_chat");
                    SendPushNotification sendPushNotification = new SendPushNotification(ImageUploadPreview.this);
                    sendPushNotification.startPush(user.getDisplayName(), finalMessage,"global_chat");
                    chat_box.setText("");
                    dialog.dismiss();
                    finish();
                }else {
                    Toast.makeText(ImageUploadPreview.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    public void Finish(View view) {
        finish();
    }
}