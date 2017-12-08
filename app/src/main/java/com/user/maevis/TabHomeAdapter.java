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

public class TabHomeAdapter extends RecyclerView.Adapter<TabHomeAdapter.ViewHolder> {


    private List<ListItem> listItems;
    private Context context;

    public TabHomeAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
        holder.textViewDateTime.setText(listItem.getDateTime());
        Picasso.with(context).load(listItem.getImageURL()).into(holder.imageViewReport);
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

        public ViewHolder (View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTime);
            imageViewReport = (ImageView) itemView.findViewById(R.id.imageViewReport);
        }
    }
}
