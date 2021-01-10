package com.ashish.musicappadvance.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashish.musicappadvance.Adapters.AlbumAdapter;
import com.ashish.musicappadvance.Adapters.MusicAdapter;
import com.ashish.musicappadvance.R;

import static com.ashish.musicappadvance.Activities.MainActivity.musicFiles;

public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_album, container, false);
        if(musicFiles.size()>=1) { // This musicFiles arraylist is from Main Activity
            recyclerView = view.findViewById(R.id.recyclerView);
            albumAdapter = new AlbumAdapter(getContext(), musicFiles);  // This musicFiles arraylist is from Main Activity
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view;
    }
}