/**
 Important things and steps
 1) Added tab Layout and view pager in main layout (see attributes)
  To slide between fragments we use view pager. For that create a view pager adapter in main activity(for this app)
 For tabs add gradient effect in drawable add new resource file root element will be shape

 2) Add ViewPagerAdapter class and initialise it.
 3) Now make fragments by extending Fragment class(like SongsFragment). Just keep onCreateView method and remove everything else
 4) Ask for read External storage permmision. Add that in manifest file
 5) Create a model class MusicFiles and In getAllMusic method get all the music available using cursor and store it in arraylist of type MusicFiles
 6) Make recyclerView adapter and music_items.xml layout
 7) Now add recycler view to the song fragment. FOr the list needed for adapter make the arraylist available in main activity static and use this list in fragment
 8) To get embedded image of each song use MediaMetadataRetriever class. For that make getAlbumArt method in MusicAdapter
 9) For PlayActivity backgrounds make a drawable file(just like we did it for gradient but root will be solid)
 10) after making layout of PlayActivity do the coding in PlayActivity.java
 11)To add item option make vector asset of more vent in drawable folder for menu item. To add options in menu item go to res file and add Android res directory
 and give directory name as menu and resource type as menu. In that menu directory create new resource file
 12) To have a search item first create search.xml file in menu folder and in style.xml have theme as DarkActionBar. Then in mainActivity ovverride onCreateOptionsMenu
13) For sort first add sort option in search.xml. Ovverride onOptionsItemSelected method
 **/



/**
 TODO: When I play video this app should pause and when I stop the video this app should start again
 **/

package com.ashish.musicappadvance.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.SearchView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.ashish.musicappadvance.Fragments.AlbumFragment;
import com.ashish.musicappadvance.Fragments.SongsFragment;
import com.ashish.musicappadvance.ModelClasses.MusicFiles;
import com.ashish.musicappadvance.ModelClasses.NotificationClass;
import com.ashish.musicappadvance.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int SONGS_READ_REQUEST_CODE = 101;
    public static final String MY_SORT_PREF = "SortOrder";
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    public static ArrayList<MusicFiles> musicFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();
    }

    private void initViewPager() {
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        /**
         Add fragments. As SongsFragment is child of Fragment therefore its object can be passed
         **/
        viewPagerAdapter.addFragent(new SongsFragment(),"Songs");
        viewPagerAdapter.addFragent(new AlbumFragment(),"Album");


        /** Set ViewPagerAdapter for the View pager **/
        viewPager.setAdapter(viewPagerAdapter);
        /**  Set ViewPager for the tab layout **/
        tabLayout.setupWithViewPager(viewPager);
    }

    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SONGS_READ_REQUEST_CODE);
        else {
            musicFiles = getAllMusic(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==SONGS_READ_REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllMusic(this);
                initViewPager();
            }
            /** Keep asking permission  **/
            else ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SONGS_READ_REQUEST_CODE);
        }
    }



    /**
     following method to read all MP3 files from a folder of your device or to read all files of your device:
     **/
    public  ArrayList<MusicFiles> getAllMusic(Context context)
    {
        /**
         MediaStore.Audio.Media-> https://developer.android.com/training/data-storage/shared/media
         Selection and projection-> https://softwareengineering.stackexchange.com/questions/246864/what-is-selection-and-what-is-projection
         https://developer.android.com/guide/topics/providers/content-provider-basics
         **/
        ArrayList<MusicFiles> tempMusicList=new ArrayList<>();
        SharedPreferences preferences=getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE);  // For sorting purpose
        String sortChoice=preferences.getString("sorting","sortByName");// For sorting purpose
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // To get Audio Uri. Can use other thing like Images instead of Audio
        String order=null;// For sorting purpose
        switch (sortChoice)// For sorting purpose
        {
            case "sortByName":
                order= MediaStore.MediaColumns.DISPLAY_NAME+" ASC";
                break;
            case "sortByDate":
                order= MediaStore.MediaColumns.DATE_ADDED+" ASC";
        }
        String[] projections= {  // Columns(Info of particular audio file that I need) that I need. Projection is an array of columns that should be included for each row retrieved.
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, // For path
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };

        /**
         Selecting means choosing some records from a table and leaving others out.
         Projecting means choosing some columns from each record and leaving others out.
         **/
        Cursor cursor=context.getContentResolver().query(uri,projections,null,null,order); // If no sortOrder then instead of order put null
        if(cursor!=null)
        {
            while(cursor.moveToNext()) {
                String album = cursor.getString(0); // i is the column index
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id=cursor.getString(5);

                String p[]=path.split("/");
                if(p[p.length-2].equals("Songs")) {
                    MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration,id);
                tempMusicList.add(musicFiles);}
            }
        }
        return tempMusicList;
    }



    public static class ViewPagerAdapter extends FragmentPagerAdapter
    {

        ArrayList<Fragment> fragmentArrayList;  // Contains all the fragments that will be created afterwards
        ArrayList<String> fragmentTitlesList;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            fragmentArrayList=new ArrayList<>();
            fragmentTitlesList=new ArrayList<>();
        }

        /**
         This method will add all the fragments and their titles to the view pager
        **/
        void addFragent(Fragment fragment,String title)
        {
            fragmentArrayList.add(fragment);
            fragmentTitlesList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitlesList.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { // String s is the name of the file searched
                String searchString=s.toLowerCase();
                ArrayList<MusicFiles> searchFiles=new ArrayList<>();

                for(MusicFiles obj:musicFiles)
                {
                    if(obj.getTitle().toLowerCase().contains(searchString)) searchFiles.add(obj);  // instead of contains we can write anything like equals, startswith etc
                }
                // After getting the searchFiles array we need to show this array in the songs fragment. For that we have to update recycler view array of songs
                // For that make another method of updateList in MusicAdapter and make the MusicAdapter obj in songs fragment static so that we can call updateList method
                // from this activity

                SongsFragment.musicAdapter.updateList(searchFiles);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor choice=getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE).edit();
        switch (item.getItemId())
        {
            case (R.id.nameSort):
                choice.putString("sorting","sortByName");
                choice.apply();
                this.recreate(); // Will reload this activity after destroying
                break;
            case(R.id.dateSort):
                choice.putString("sorting","sortByDate");
                choice.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}