package com.ashish.musicappadvance.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.musicappadvance.Activities.PlayActivity;
import com.ashish.musicappadvance.ModelClasses.MusicFiles;
import com.ashish.musicappadvance.R;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    Context context;
    static public ArrayList<MusicFiles> musicFilesArrayList;

    public MusicAdapter(Context context, ArrayList<MusicFiles> musicFilesArrayList) {
        this.context = context;
        this.musicFilesArrayList = musicFilesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MusicFiles musicFile=musicFilesArrayList.get(position);
        holder.name.setText(musicFile.getTitle());
        byte[] image= getAlbumArt(musicFile.getPath());
        if(image!=null) Glide.with(context).asBitmap().load(image).into(holder.image);
        else Glide.with(context).load(R.drawable.food_logo).into(holder.image);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, PlayActivity.class);
                i.putExtra("position",position);
                context.startActivity(i);
            }
        });

        /**
         To add menu item on each song
         **/
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.delete:
                                deleteSong(position,view);
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }

    private void deleteSong(int position, View view) {

        /**
         For deleting from the memory
         **/
        Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(musicFilesArrayList.get(position).getId())); // It will be of form-> content://
        File file=new File(musicFilesArrayList.get(position).getPath());
        boolean deleted=file.delete();
        if(deleted) {

            /**
             To delete all the content of that file. If we don't do this it will throw an error
             **/
            context.getContentResolver().delete(contentUri,null,null);

            /**
             To delete from the memory. A file may not be deleted because the audio may be in sd card or android level is or above 19
             **/
            MusicFiles musicFile = musicFilesArrayList.get(position);
            musicFilesArrayList.remove(position);
            Snackbar.make(view, "File Deleted. Dou you want to undo?", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicFilesArrayList.add(position, musicFile);
                    notifyDataSetChanged();
                }
            }).show();
            notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(context, "Cant be deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return musicFilesArrayList.size();
    }

    private byte[] getAlbumArt(String path)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever(); //MediaMetadataRetriever class provides a unified interface for retrieving frame and meta data from an input media file.
        retriever.setDataSource(path); //Sets the data source (file pathname) to use.
        byte[] art=retriever.getEmbeddedPicture();  // Get embedded image
        retriever.release();
        return art;
    }

    public void updateList(ArrayList<MusicFiles> updatedList)
    {
        musicFilesArrayList=updatedList;
        notifyDataSetChanged();
    }




    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView image,menu;
        CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.albumSong);
            image=itemView.findViewById(R.id.albumImage);
            parent=itemView.findViewById(R.id.albumParent);
            menu=itemView.findViewById(R.id.menu);
        }
    }



}
