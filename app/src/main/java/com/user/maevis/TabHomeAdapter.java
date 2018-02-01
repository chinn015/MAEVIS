package com.user.maevis;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import com.user.maevis.models.PageNavigationManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabHomeAdapter extends RecyclerView.Adapter<TabHomeAdapter.ViewHolder> {


    private List<ListItem> listItems;
    private List<ListItemVerified> listItemsVerified;
    private Context context;
    //private static ListItem clickedItem = null;
    private static ListItemVerified clickedItemVerified = null;
    private static UserItem clickedUserItem = null;

    static boolean clickedStatus = false;
    static boolean clickedUserItemStatus = false;

    /*public TabHomeAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }*/

    public TabHomeAdapter(List<ListItemVerified> listItemsVerified, Context context) {
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
        Picasso.with(context).load(listItemVerified.getUserPhoto()).into(holder.userPhoto);

        holder.cardHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context, "You clicked : " + listItemVerified.getHead(), Toast.LENGTH_LONG).show();
                //setClickedItemVerified(listItemVerified);

                /*for(int x=0; x<FirebaseDatabaseManager.getUserItems().size(); x++) {
                    if (listItemVerified.getReportedBy().equals(FirebaseDatabaseManager.getUserItems().get(x).getUserID())) {
                        clickedUserItem = FirebaseDatabaseManager.getUserItems().get(x);
                        clickedUserItemStatus = true;
                        VerifyReport.clickedUserItemStatus = false;
                    }
                }*/

                PageNavigationManager.clickTabHomeListItemVerified(listItemVerified);

                /*clickedStatus = true;
                clickedUserItemStatus = true;
                TabNotifAdapter.clickedStatus = false;
                TabNotifAdapterRegUser.clickedStatus = false;*/

                Intent i;
                i = new Intent(context, ReportPage.class);
                context.startActivity(i);
            }
        });

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked star", Toast.LENGTH_LONG).show();
            }
        });
    }


    public static ListItemVerified getClickedItemVerified() {
        return clickedItemVerified;
    }

    public static void setClickedItemVerified(ListItemVerified clickedItemVerified) {
        TabHomeAdapter.clickedItemVerified = clickedItemVerified;
    }

    public static UserItem getClickedUserItem() {
        return clickedUserItem;
    }

    public static void setClickedUserItem(UserItem clickedUserItem) {
        TabHomeAdapter.clickedUserItem = clickedUserItem;
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
        public CircleImageView userPhoto;
        public CardView cardHome;
        public ImageView star;

        public ViewHolder (View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewDateTime = (TextView) itemView.findViewById(R.id.textViewDateTime);
            imageViewReport = (ImageView) itemView.findViewById(R.id.imageViewReport);
            reportLayout = (RelativeLayout) itemView.findViewById(R.id.reportBottomLayout);
            imageReportType = (ImageView) itemView.findViewById(R.id.reportHomeType);
            userPhoto = (CircleImageView) itemView.findViewById(R.id.user_photo);
            cardHome = (CardView) itemView.findViewById(R.id.cardHome);
            star = (ImageView) itemView.findViewById(R.id.star);


        }
    }
}
