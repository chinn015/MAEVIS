package com.user.maevis;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 1/8/2018.
 */

public class TabMediaAdapter extends RecyclerView.Adapter<TabMediaAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private static ListItem clickedItem = null;

    public TabMediaAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public TabMediaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile_media, parent, false);
        return new TabMediaAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TabMediaAdapter.ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        Picasso.with(context)
                .load(listItem.getImageURL())
                .fit()
                .into(holder.imageViewReport);
    }

//    public static ListItem getClickedItem() {
//        return clickedItem;
//    }
//
//    public static void setClickedItem(ListItem clickedItem) {
//        TabMediaAdapter.clickedItem = clickedItem;
//    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewReport;

        public ViewHolder (View itemView){
            super(itemView);

            imageViewReport = (ImageView) itemView.findViewById(R.id.photoReport);

        }
    }
}
