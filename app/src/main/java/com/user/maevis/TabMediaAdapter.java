package com.user.maevis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import java.util.List;

public class TabMediaAdapter extends RecyclerView.Adapter<TabMediaAdapter.ViewHolder>   {

    private List<ListItem> listItems;
    private static ListItem clickedItem = null;
    private Context context;

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

        Picasso.with(context).load(listItem.getImageURL()).into(holder.imageViewReport);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewReport;
        public RelativeLayout photoReportLayout;

        public ViewHolder (View itemView){
            super(itemView);

            imageViewReport = (ImageView) itemView.findViewById(R.id.photoReport);
            photoReportLayout = (RelativeLayout) itemView.findViewById(R.id.notifReportLayout);

        }
    }

}
