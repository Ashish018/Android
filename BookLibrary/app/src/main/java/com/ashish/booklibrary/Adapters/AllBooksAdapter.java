package com.ashish.booklibrary.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.ashish.booklibrary.Activities.BookDisplay;
import com.ashish.booklibrary.Models.BooksModelClass;
import com.ashish.booklibrary.R;
import com.ashish.booklibrary.Utilities.UtilityClass;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllBooksAdapter extends RecyclerView.Adapter<AllBooksAdapter.ViewHolder> {

    ArrayList<BooksModelClass> list=new ArrayList<>();
    Context context;
    String parent;



    public static String BOOK_ID="BOOK_ID";

    public AllBooksAdapter(Context context, String parent) {
        this.context = context;
        this.parent = parent;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_books,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText(list.get(position).getText());
        Picasso.get().load(list.get(position).getUrl()).into(holder.imageView);

        holder.cardView.setOnClickListener(v-> {
            Intent intent=new Intent(context, BookDisplay.class);
            intent.putExtra(BOOK_ID, list.get(position).getId());
            context.startActivity(intent);
        });
        holder.downArrow.setOnClickListener(v-> {
            TransitionManager.beginDelayedTransition(holder.cardView);
            holder.descLayout.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);
            if(parent.equals("All_Books")) holder.delete.setVisibility(View.GONE);
            else if(parent.equals("Read_Books")) // similarly for all lists
            {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(v1->
                {

                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("You sure you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                            UtilityClass.getInstance().deleteReadList(list.get(position));
                            notifyDataSetChanged(); //refresh the recycler view
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();

                });
            }
            }
        );
        holder.upArrow.setOnClickListener(v-> {
            TransitionManager.beginDelayedTransition(holder.cardView);
            holder.descLayout.setVisibility(View.GONE);
            holder.downArrow.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<BooksModelClass> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView textView;
        ImageView imageView,downArrow,upArrow,delete;
        LinearLayout descLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            textView=itemView.findViewById(R.id.textView);
            imageView=itemView.findViewById(R.id.profile_image);
            downArrow=itemView.findViewById(R.id.downArrow);
            upArrow=itemView.findViewById(R.id.upArrow);
            descLayout=itemView.findViewById(R.id.descLayout);
            delete=itemView.findViewById(R.id.delete);
        }
    }

}
