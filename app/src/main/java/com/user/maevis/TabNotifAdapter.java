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

import java.util.List;

public class TabNotifAdapter extends RecyclerView.Adapter<TabNotifAdapter.ViewHolder> {


    private List<ListItem> listItems;
    private static ListItem clickedItem = null;
    private Context context;
    int[] reportIcons = {
            R.mipmap.btn_fire,
            R.mipmap.btn_flood,
            R.mipmap.btn_accident
    };


    public TabNotifAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notif, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        //details to be displayed in Notification Tab
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDateTime.setText(listItem.getDisplayDateTime());

        switch(listItem.getReportType()){
            case "Fire":
                    Picasso.with(context).load(reportIcons[0]).into(holder.imageReportType);
                    break;

            case "Flood":
                    Picasso.with(context).load(reportIcons[1]).into(holder.imageReportType);
                    break;

            case "Vehicular Accident":
                    Picasso.with(context).load(reportIcons[2]).into(holder.imageReportType);
                    break;
        }

        /*holder.textViewDesc.setText(listItem.getDesc());
        Picasso.with(context).load(listItem.getImageURL()).into(holder.imageViewReport);*/

        holder.notifReportLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context, "You clicked : " + listItem.getHead(), Toast.LENGTH_LONG).show();
                setClickedItem(listItem);
                Intent i;
                i = new Intent(context, VerifyReport.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        //public TextView textViewDesc;
        public TextView textViewDateTime;
        public ImageView imageViewReport;
        public RelativeLayout notifReportLayout;
        public ImageView imageReportType;


        public ViewHolder (View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHeadNotif);
            //textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTimeNotif);
            imageViewReport = (ImageView) itemView.findViewById(R.id.viewNotifImage);
            notifReportLayout = (RelativeLayout) itemView.findViewById(R.id.notifReportLayout);
            imageReportType = (ImageView) itemView.findViewById(R.id.reportType);


        }
    }


    //GETTER SETTER
    public static ListItem getClickedItem() {
        return clickedItem;
    }

    public static void setClickedItem(ListItem clickedItem) {
        TabNotifAdapter.clickedItem = clickedItem;
    }
}
