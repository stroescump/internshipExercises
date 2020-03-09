package com.example.internshipexercises;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private int incrementValue=0;
    private TextView labelCounter;
    private Button incrementBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate");
        if(savedInstanceState!=null) {
//            Toast.makeText(this, String.valueOf(savedInstanceState.getInt("value")), Toast.LENGTH_SHORT).show();
            incrementValue = savedInstanceState.getInt("value");
        }
        else incrementValue=0;
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("value",incrementValue);
    }

    //    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putInt("value",incrementValue);
//    }

    private void initViews(){
        labelCounter = findViewById(R.id.labelCounter);
        incrementBtn = findViewById(R.id.incrementBtn);
        labelCounter.setText(String.valueOf(incrementValue));
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue++;
                labelCounter.setText(String.valueOf(incrementValue));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart : The activity exists but cannot be used by USER");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume : Prepping final UI changes");
    }

    @Override
    protected void onPause() {
        super.onPause();
    Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
    Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    Log.d(TAG,"onDestroy");
    }
}
