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

public class TabProfileTimelineAdapter extends RecyclerView.Adapter<TabProfileTimelineAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private static ListItem clickedItem = null;

    public TabProfileTimelineAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public TabProfileTimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_items, parent, false);
        return new TabProfileTimelineAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TabProfileTimelineAdapter.ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDescription());
        holder.textViewDateTime.setText(listItem.getDisplayDateTime());
        Picasso.with(context).load(listItem.getUserPhoto()).into(holder.userPhoto);
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
    }

    public static ListItem getClickedItem() {
        return clickedItem;
    }

    public static void setClickedItem(ListItem clickedItem) {
        TabProfileTimelineAdapter.clickedItem = clickedItem;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewDateTime;
        public ImageView imageViewReport;
        public RelativeLayout reportLayout;
        public ImageView imageReportType;
        public CircleImageView userPhoto;

        public ViewHolder (View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTime);
            imageViewReport = (ImageView) itemView.findViewById(R.id.imageViewReport);
            reportLayout = (RelativeLayout) itemView.findViewById(R.id.reportLayout);
            imageReportType = (ImageView) itemView.findViewById(R.id.reportHomeType);
            userPhoto = (CircleImageView) itemView.findViewById(R.id.user_photo);
        }
    }
}
