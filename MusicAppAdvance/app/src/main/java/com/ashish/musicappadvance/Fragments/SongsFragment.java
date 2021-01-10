package com.ashish.musicappadvance.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashish.musicappadvance.Adapters.MusicAdapter;
import com.ashish.musicappadvance.R;

import static com.ashish.musicappadvance.Activities.MainActivity.musicFiles;

public class SongsFragment extends Fragment {

RecyclerView recyclerView;
public static MusicAdapter musicAdapter;  // making it static so that we can use this in main activity and from there we can call updateList in MusicAdapter class

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_songs, container, false);

        if(musicFiles.size()>=1) { // This musicFiles arraylist is from Main Activity
            recyclerView = view.findViewById(R.id.recyclerView);
            musicAdapter = new MusicAdapter(getContext(), musicFiles);  // This musicFiles arraylist is from Main Activity
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return view;
    }
}