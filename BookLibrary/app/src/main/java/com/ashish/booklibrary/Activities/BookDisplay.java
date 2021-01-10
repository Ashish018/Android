package com.ashish.booklibrary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ashish.booklibrary.Adapters.AllBooksAdapter;
import com.ashish.booklibrary.Models.BooksModelClass;
import com.ashish.booklibrary.R;
import com.ashish.booklibrary.Utilities.UtilityClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookDisplay extends AppCompatActivity {

    ImageView imageView;
    Button readButton,currentButton,favButton,wishListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_display);
        initData();
        Intent intent=getIntent();

        if(intent!=null)
        {
            int id=intent.getIntExtra(AllBooksAdapter.BOOK_ID,-1);
            if(id!=-1)
            {
                BooksModelClass book= UtilityClass.getInstance().getBook(id);
                if(book!=null)
                {
                    // Set data is used to give image url, text (desc, name etc of the clicked book)
                    setData(book);
                    //Handlers are used to enable/ disable button and add books to particular list
                    readBooksHandler(book);
                    wishListHandler(book);
                    favBooksHandler(book);
                    currentlyReadingBooksHandler(book);

                }
            }
        }
    }

    private void currentlyReadingBooksHandler(BooksModelClass book)
    {
        ArrayList<BooksModelClass> readBooks=UtilityClass.getInstance().getCurrentReadingBook();
        boolean alreadyRead=false;
        for(BooksModelClass obj:readBooks)
        {
            if(obj.getId()==book.getId())
            {
                alreadyRead=true;
                break;
            }
        }
        if(alreadyRead)
        {
            currentButton.setEnabled(false);
        }
        else
        {
            currentButton.setOnClickListener(v->{
                if(UtilityClass.getInstance().addToCurrentReadingBooks(book)) Toast.makeText(this,"Book added to Currently reading book",Toast.LENGTH_SHORT).show();
                //TODO: navigate to already read book activity
            });
        }
    }

    private void favBooksHandler(BooksModelClass book)
    {
        ArrayList<BooksModelClass> readBooks=UtilityClass.getInstance().getFavBooks();
        boolean alreadyRead=false;
        for(BooksModelClass obj:readBooks)
        {
            if(obj.getId()==book.getId())
            {
                alreadyRead=true;
                break;
            }
        }
        if(alreadyRead)
        {
            favButton.setEnabled(false);
        }
        else
        {
            favButton.setOnClickListener(v->{
                if(UtilityClass.getInstance().addToFavBooks(book)) Toast.makeText(this,"Book added to Favourite book list",Toast.LENGTH_SHORT).show();
                //TODO: navigate to already Favourite book list activity
            });
        }
    }

    private void wishListHandler(BooksModelClass book)
    {
        ArrayList<BooksModelClass> readBooks=UtilityClass.getInstance().getWishListBooks();
        boolean alreadyRead=false;
        for(BooksModelClass obj:readBooks)
        {
            if(obj.getId()==book.getId())
            {
                alreadyRead=true;
                break;
            }
        }
        if(alreadyRead)
        {
            wishListButton.setEnabled(false);
        }
        else
        {
            wishListButton.setOnClickListener(v->{
                if(UtilityClass.getInstance().addToWishList(book)) Toast.makeText(this,"Book added to wishlist",Toast.LENGTH_SHORT).show();
                //TODO: navigate to already Wishlist activity
            });
        }
    }

    private void readBooksHandler(BooksModelClass book) {
        ArrayList<BooksModelClass> readBooks=UtilityClass.getInstance().getReadBooks();
        boolean alreadyRead=false;
        for(BooksModelClass obj:readBooks)
        {
            if(obj.getId()==book.getId())
            {
                alreadyRead=true;
                break;
            }
        }
        if(alreadyRead)
        {
            readButton.setEnabled(false);
        }
        else
        {
            readButton.setOnClickListener(v->{
                if(UtilityClass.getInstance().addToReadBooks(book)) startActivity(new Intent(this,ReadBooksList.class));
            });
        }
    }

    void initData()
    {
        imageView=findViewById(R.id.imageView2);
        readButton=findViewById(R.id.readButton);
        currentButton=findViewById(R.id.currentButton);
        favButton=findViewById(R.id.favButton);
        wishListButton=findViewById(R.id.wishListButton);
    }

    void setData(BooksModelClass book)
    {

//        Log.d("URLOF", "setData: "+book.getUrl());
        Picasso.get().load(book.getUrl()).into(imageView);
    }
}