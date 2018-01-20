package com.user.maevis;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class TabSearchAdapter extends RecyclerView.Adapter<TabSearchAdapter.ViewHolder> {


    private List<ListItem> listItems;
    private List<ListItemVerified> listItemsVerified;
    private Context context;
    //private static ListItem clickedItem = null;
    private static ListItemVerified clickedItemVerified = null;

    static boolean clickedStatus = false;

    /*public TabHomeAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }*/

    public TabSearchAdapter(List<ListItemVerified> listItemsVerified, Context context) {
        this.listItemsVerified = listItemsVerified;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItemVerified listItemVerified = listItemsVerified.get(position);

        holder.textViewHead.setText(listItemVerified.getHead());
        holder.textViewDesc.setText(listItemVerified.getDescription());
        holder.textViewDateTime.setText(listItemVerified.getDisplayDateTime());
        Picasso.with(context)
                .load(listItemVerified.getImageThumbnailURL())
                .fit()
                .into(holder.imageViewReport);

        Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.imageReportType);

        holder.reportLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context, "You clicked : " + listItemVerified.getHead(), Toast.LENGTH_LONG).show();
                setClickedItemVerified(listItemVerified);

                clickedStatus = true;
                TabNotifAdapterRegUser.clickedStatus = false;

                Intent i;
                i = new Intent(context, ReportPage.class);
                context.startActivity(i);
            }
        });
    }

    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDescription());
        holder.textViewDateTime.setText(listItem.getDisplayDateTime());
        Picasso.with(context)
                .load(listItem.getImageURL())
                .fit()
                .into(holder.imageViewReport);

        Picasso.with(context).load(listItem.getReportTypeImage(listItem.getReportType())).into(holder.imageReportType);

        holder.reportLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context, "You clicked : " + listItem.getHead(), Toast.LENGTH_LONG).show();
                setClickedItem(listItem);
                Intent i;
                i = new Intent(context, ReportPage.class);
                context.startActivity(i);
            }
        });
    }*/

    /*public static ListItem getClickedItem() {
        return clickedItem;
    }

    public static void setClickedItem(ListItem clickedItem) {
        TabHomeAdapter.clickedItem = clickedItem;
    }*/

    public static ListItemVerified getClickedItemVerified() {
        return clickedItemVerified;
    }

    public static void setClickedItemVerified(ListItemVerified clickedItemVerified) {
        TabSearchAdapter.clickedItemVerified = clickedItemVerified;
    }

    @Override
    public int getItemCount() {
        return listItemsVerified.size();
    }

    public int getItemVerifiedCount() {
        return listItemsVerified.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewDateTime;
        public ImageView imageViewReport;
        public RelativeLayout reportLayout;
        public ImageView imageReportType;


        public ViewHolder (View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTime);
            imageViewReport = (ImageView) itemView.findViewById(R.id.imageViewReport);
            reportLayout = (RelativeLayout) itemView.findViewById(R.id.reportLayout);
            imageReportType = (ImageView) itemView.findViewById(R.id.reportHomeType);

        }
    }

    public void setFilter(List<ListItemVerified> listItemVerified){
        listItemsVerified = new ArrayList<>();
        listItemsVerified.addAll(listItemVerified);
        notifyDataSetChanged();
    }
}
