package com.user.maevis;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.user.maevis.models.PageNavigationManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabSearchAdapter extends RecyclerView.Adapter<TabSearchAdapter.ViewHolder> {


    private List<ListItem> listItems;
    private List<ListItemVerified> listItemsVerified;
    private Context context;
    //private static ListItem clickedItem = null;
    private static ListItemVerified clickedItemVerified = null;
    private static UserItem clickedUserItem = null;

    static boolean clickedStatus = false;
    static boolean clickedUserItemStatus = false;

    public TabSearchAdapter(List<ListItemVerified> listItemsVerified, Context context) {
        this.listItemsVerified = listItemsVerified;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_reports, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItemVerified listItemVerified = listItemsVerified.get(position);
        holder.textViewHead.setText(listItemVerified.getHead());
        holder.textViewDateTime.setText(listItemVerified.getDisplayDateTime());
        Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.reportType);

        if(Tab4_Search.stringContain != null){
            switch (Tab4_Search.stringContain){
                case "reportType" :
                    Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.reportType);

                    Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.imageThumbnail);
                    holder.reportType.setVisibility(View.GONE);
                    break;

                case "reportAddress" :
                    holder.reportType.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.reportType);
                    Picasso.with(context)
                            .load(listItemVerified.getImageThumbnailURL())
                            .fit()
                            .into(holder.imageThumbnail);
                    break;
                case "reportName" :
                    holder.reportType.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.reportType);
                    Picasso.with(context).load(listItemVerified.getUserPhoto()).into(holder.userPhoto);
                    Picasso.with(context)
                            .load(R.drawable.img_user)
                            .fit()
                            .into(holder.imageThumbnail);
                    break;
            }

        }else{
            Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.reportType);
            Picasso.with(context).load(listItemVerified.getReportTypeImage(listItemVerified.getReportType())).into(holder.imageThumbnail);
            holder.reportType.setVisibility(View.GONE);
        }


        holder.reportLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context, "You clicked : " + listItemVerified.getHead(), Toast.LENGTH_LONG).show();

                PageNavigationManager.clickTabHomeListItemVerified(listItemVerified);

                Intent i;
                i = new Intent(context, ReportPage.class);
                context.startActivity(i);
            }
        });
    }

    public static ListItemVerified getClickedItemVerified() {
        return clickedItemVerified;
    }

    public static void setClickedItemVerified(ListItemVerified clickedItemVerified) {
        TabSearchAdapter.clickedItemVerified = clickedItemVerified;
    }

    public static UserItem getClickedUserItem() {
        return clickedUserItem;
    }

    public static void setClickedUserItem(UserItem clickedUserItem) {
        TabSearchAdapter.clickedUserItem = clickedUserItem;
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
        public ImageView imageThumbnail;
        public ImageView reportType;
        public ImageView userPhoto;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            //textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTime);
            //imageViewReport = (ImageView) itemView.findViewById(R.id.imageViewReport);
            reportLayout = (RelativeLayout) itemView.findViewById(R.id.reportLayout);
            imageThumbnail = (ImageView) itemView.findViewById(R.id.imageThumbnail);
            reportType = (ImageView) itemView.findViewById(R.id.reportType);
            userPhoto = (ImageView) itemView.findViewById(R.id.imageThumbnail);

        }
    }

    public void setFilter(List<ListItemVerified> listItemVerified){
        listItemsVerified = new ArrayList<>();
        listItemsVerified.addAll(listItemVerified);
        notifyDataSetChanged();
    }
}
