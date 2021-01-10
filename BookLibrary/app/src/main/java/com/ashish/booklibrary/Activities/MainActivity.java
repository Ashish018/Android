package com.ashish.booklibrary.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ashish.booklibrary.R;
import com.ashish.booklibrary.Utilities.UtilityClass;

public class MainActivity extends AppCompatActivity {


    private Button button1,button2,button3,button4,button5,button6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();


        button1.setOnClickListener(v-> startActivity(new Intent(MainActivity.this,See_All_Books.class)));
        button3.setOnClickListener(v->{
             startActivity(new Intent(this,ReadBooksList.class));
        });

        button6.setOnClickListener(v->{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("This app is created by me. Click Visit button to visit the website").setNegativeButton("Dismiss", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this,WebSiteActivity.class));
                }
            })
                    .setCancelable(false).create().show();
        });

    }

    private void initElements() {
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
    }
}