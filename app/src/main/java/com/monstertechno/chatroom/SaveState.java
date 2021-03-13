package com.monstertechno.chatroom;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SaveState {
    SharedPreferences sharedPreferences;

    public SaveState(Context context) {
        sharedPreferences = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    public void setClickTime(long number){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("number",number);
        editor.apply();
    }

    public long getClickTime(){
        long num = sharedPreferences.getLong("number",new Date().getTime());
        return num;
    }
}
