package com.ashish.musicappadvance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.musicappadvance.ModelClasses.MusicFiles;
import com.ashish.musicappadvance.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumAdapter  extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    Context context;
    ArrayList<MusicFiles> albumFiles;

    public AlbumAdapter(Context context, ArrayList<MusicFiles> albumFiles) {
        this.context = context;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.album_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicFiles musicFile=albumFiles.get(position);
        Glide.with(context).load(R.drawable.food_logo).into(holder.image);
        holder.song.setText(musicFile.getAlbum());

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView parent;
        TextView song;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent=itemView.findViewById(R.id.albumParent);
            song=itemView.findViewById(R.id.albumSong);
            image=itemView.findViewById(R.id.albumImage);
        }
    }
}
