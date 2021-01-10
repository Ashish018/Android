package com.ashish.booklibrary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ashish.booklibrary.Adapters.AllBooksAdapter;
import com.ashish.booklibrary.Models.BooksModelClass;
import com.ashish.booklibrary.R;
import com.ashish.booklibrary.Utilities.UtilityClass;

import java.util.ArrayList;

public class See_All_Books extends AppCompatActivity {
    RecyclerView recyclerView;
    AllBooksAdapter allBooksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see__all__books);

            // When animation added in style then no need to write this and finish method
//        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);  // For animation
        recyclerView=findViewById(R.id.recyclerView);
        allBooksAdapter=new AllBooksAdapter(this,"All_Books");


        allBooksAdapter.setList(UtilityClass.getInstance().getAllBooks());
        recyclerView.setAdapter(allBooksAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    @Override
//    public void finish() { // to add animation when activity exits(on clicking back button)
//        super.finish();
//        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);  // For animation
//
//    }
}