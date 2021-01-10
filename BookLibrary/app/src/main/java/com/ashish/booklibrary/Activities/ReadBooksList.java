package com.ashish.booklibrary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ashish.booklibrary.Adapters.AllBooksAdapter;
import com.ashish.booklibrary.R;
import com.ashish.booklibrary.Utilities.UtilityClass;

public class ReadBooksList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_books_list);

        /***
         * We can use the adapter and model class that we made for all books list(AllBooksAdapter and BooksModelClass)
         * just add recycler view in this activity layout
         **/

        RecyclerView recyclerView=findViewById(R.id.recyclerView);

        AllBooksAdapter allBooksAdapter=new AllBooksAdapter(this,"Read_Books");

        recyclerView.setAdapter(allBooksAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allBooksAdapter.setList(UtilityClass.getInstance().getReadBooks());

    }


    // Ovveriding the back button available on the mobile
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
/**
 * Every activity is added to a stack so after pressing the back button it will lead to
 * top most activity (in this case after pressing back button
 * it will go from this activity to the main activity and on clicking back button on main activity it will come back here)

 **/
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // So that this this intent clears the intent stack
        startActivity(intent);

    }
}