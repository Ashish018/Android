package com.ashish.booklibrary.Utilities;

import com.ashish.booklibrary.Models.BooksModelClass;

import java.util.ArrayList;

public class UtilityClass {  // It will be a singleton pattern used to store data. But problem is it gets erased once app is closed

    static ArrayList<BooksModelClass>  allBooks,readBooks,wishListBooks,favBooks,currentReadingBook;

    private static UtilityClass instance=null;

    public static UtilityClass getInstance() {
        if(instance==null) instance=new UtilityClass();
        return instance;
    }

    UtilityClass()
    {
        if(allBooks==null)
        {
            allBooks=new ArrayList<>();
            giveData();
        }
        if(readBooks==null) readBooks=new ArrayList<>();
        if(wishListBooks==null) wishListBooks=new ArrayList<>();
        if(favBooks==null) favBooks=new ArrayList<>();
        if(currentReadingBook==null) currentReadingBook=new ArrayList<>();
    }


    private void giveData()
    {
        allBooks.add(new BooksModelClass(1,"Bhagvat Gita","https://cdn.exoticindia.com/details-zoom-mobile/books-2019/nav372.jpg"));
        allBooks.add(new BooksModelClass(2,"Wings of Fire","https://3.imimg.com/data3/VM/WT/MY-12313319/kalam-500x500.jpeg"));
        allBooks.add(new BooksModelClass(3,"Bhagvat Gita","https://cdn.exoticindia.com/details-zoom-mobile/books-2019/nav372.jpg"));
        allBooks.add(new BooksModelClass(4,"Wings of Fire","https://3.imimg.com/data3/VM/WT/MY-12313319/kalam-500x500.jpeg"));
        allBooks.add(new BooksModelClass(5,"Bhagvat Gita","https://cdn.exoticindia.com/details-zoom-mobile/books-2019/nav372.jpg"));
        allBooks.add(new BooksModelClass(6,"Wings of Fire","https://3.imimg.com/data3/VM/WT/MY-12313319/kalam-500x500.jpeg"));
        allBooks.add(new BooksModelClass(7,"Bhagvat Gita","https://cdn.exoticindia.com/details-zoom-mobile/books-2019/nav372.jpg"));
        allBooks.add(new BooksModelClass(8,"Wings of Fire","https://3.imimg.com/data3/VM/WT/MY-12313319/kalam-500x500.jpeg"));
        allBooks.add(new BooksModelClass(9,"Bhagvat Gita","https://cdn.exoticindia.com/details-zoom-mobile/books-2019/nav372.jpg"));
        allBooks.add(new BooksModelClass(10,"Wings of Fire","https://3.imimg.com/data3/VM/WT/MY-12313319/kalam-500x500.jpeg"));

    }

    public static ArrayList<BooksModelClass> getAllBooks() {
        return allBooks;
    }

    public static ArrayList<BooksModelClass> getReadBooks() {
        return readBooks;
    }

    public static ArrayList<BooksModelClass> getWishListBooks() {
        return wishListBooks;
    }

    public static ArrayList<BooksModelClass> getFavBooks() {
        return favBooks;
    }

    public static ArrayList<BooksModelClass> getCurrentReadingBook() {
        return currentReadingBook;
    }

    public BooksModelClass getBook(int id)
    {
        for(BooksModelClass obj:allBooks)
        {
            if(obj.getId()==id) return obj;
        }
        return null;
    }

    public boolean addToReadBooks(BooksModelClass book)
    {
        return readBooks.add(book);
    }
    public boolean addToWishList(BooksModelClass book)
    {
        return wishListBooks.add(book);
    }
    public boolean addToFavBooks(BooksModelClass book)
    {
        return favBooks.add(book);
    }
    public boolean addToCurrentReadingBooks(BooksModelClass book)
    {
        return currentReadingBook.add(book);
    }

    public boolean deleteReadList(BooksModelClass booksModelClass) {
        return readBooks.remove(booksModelClass);
    }
}
