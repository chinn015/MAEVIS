package com.user.maevis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;
import com.user.maevis.models.ReportVerifiedModel;
import com.user.maevis.session.SessionManager;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabHomeAdapter extends RecyclerView.Adapter<TabHomeAdapter.ViewHolder> {

    private static final String TAG = "TabHomeAdapter";

    private List<ListItem> listItems;
    private List<ListItemVerified> listItemsVerified;
    private Context context;
    //private static ListItem clickedItem = null;
    private static ListItemVerified clickedItemVerified = null;
    private static UserItem clickedUserItem = null;
    static int stat;
    static boolean clickedStatus = false;
    static boolean clickedUserItemStatus = false;

    /*public TabHomeAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }*/

    private TextView yourClassLevelTextView;
    public void setTextView(TextView textViewFromActivity)
    {
        this.yourClassLevelTextView = textViewFromActivity;
    }

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
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

        holder.numStars.setText(""+listItemVerified.getStarCount());
        //Toast.makeText(context, "star count : " + listItemVerified.getStarCount(), Toast.LENGTH_LONG).show();

        holder.cardHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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

        //Picasso.with(context).load(R.drawable.ic_toggle_star_24).into(holder.star);

        // Determine if the current user has liked this post and set UI accordingly
        if (listItemVerified.getStars().containsKey(SessionManager.getUserID())) {
           // holder.star.setImageResource(R.drawable.ic_toggle_star_24);
            Picasso.with(context).load(R.drawable.ic_toggle_star_24).into(holder.star);

        } else {
            //holder.star.setImageResource(R.drawable.ic_toggle_star_outline_24);
            Picasso.with(context).load(R.drawable.ic_toggle_star_outline_24).into(holder.star);

        }

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference globalRef = FirebaseDatabaseManager.FirebaseReportsVerified.child(listItemVerified.getReportID());

                onStarClicked(globalRef);

                if (stat == 0) {
                    // holder.star.setImageResource(R.drawable.ic_toggle_star_24);
                    Picasso.with(context).load(R.drawable.ic_toggle_star_24).into(holder.star);
                    holder.numStars.setText(String.valueOf(listItemVerified.getStarCount()+1));

                } else {
                    //holder.star.setImageResource(R.drawable.ic_toggle_star_outline_24);
                    Picasso.with(context).load(R.drawable.ic_toggle_star_outline_24).into(holder.star);
                    holder.numStars.setText(String.valueOf(listItemVerified.getStarCount()));


                }

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
        public TextView numStars;

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
            numStars = (TextView)itemView.findViewById(R.id.post_num_stars);

        }

    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                //ReportVerifiedModel report = mutableData.getValue(ReportVerifiedModel.class);
                ListItemVerified report = mutableData.getValue(ListItemVerified.class);
                if (report == null) {
                    return Transaction.success(mutableData);
                }

                if (report.stars.containsKey(SessionManager.getUserID())) {
                    // Unstar the post and remove self from stars
                    report.starCount = report.starCount - 1;
                    report.stars.remove(SessionManager.getUserID());
                    stat = 0;
                } else {
                    // Star the post and add self to stars
                    report.starCount = report.starCount + 1;
                    report.stars.put(SessionManager.getUserID(), true);
                    stat = 1;
                }

                // Set value and report transaction success
                mutableData.setValue(report);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]
}
