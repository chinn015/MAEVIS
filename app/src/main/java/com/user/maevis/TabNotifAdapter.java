package com.user.maevis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TabNotifAdapter extends RecyclerView.Adapter<TabNotifAdapter.ViewHolder> {


    private List<ListNotif> listItems;
    private Context context;

    public TabNotifAdapter(List<ListNotif> listItems, Context context) {
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
        ListNotif listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDateTime.setText(listItem.getDateTime());
//        Picasso.with(context).load(listItem.getImageURL()).into(holder.imageViewReport);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewDateTime;
//        public ImageView imageViewReport;

        public ViewHolder (View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHeadNotif);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTimeNotif);
//            imageViewReport = (ImageView) itemView.findViewById(R.id.imageViewReportNotif);
        }
    }
}
