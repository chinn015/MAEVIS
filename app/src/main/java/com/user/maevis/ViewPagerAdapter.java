package com.user.maevis;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.MergedReportsModel;
import com.user.maevis.models.PageNavigationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sanket on 27-Feb-17.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> images1;
    private int [] pics = {R.drawable.help2, R.drawable.help1, R.drawable.help3};
    /*private String [] images = {"https://firebasestorage.googleapis.com/v0/b/maevis-ecd17.appspot.com/o/Photos%2FIMGMAEVIS20180203_165654.jpg?alt=media&token=0c5f5c2e-74ff-4191-a20e-e3c3a643eb27",
            "https://firebasestorage.googleapis.com/v0/b/maevis-ecd17.appspot.com/o/Photos%2FIMGMAEVIS20180203_165729.jpg?alt=media&token=d2867af8-02e4-422f-91e3-1252f84ee215", "https://firebasestorage.googleapis.com/v0/b/maevis-ecd17.appspot.com/o/Photos%2F2175?alt=media&token=f4235f00-0737-4c39-aa8e-496e2363a744"};
*/
    /*private List<String> images = new ArrayList<>();
    private List<String> reportedBy = new ArrayList<>();
    private List<String> description = new ArrayList<>();
    private List<String> dateTime = new ArrayList<>();
    private List<String> dateTime = new ArrayList<>();*/

    private List<MergedReportsModel> mergedReports = new ArrayList<>();

    private TextView viewReportHead;
    private TextView viewReportDesc;
    private TextView viewReportDateTime;
    private ImageView viewReportType;
    private CircleImageView viewUserImage;
    static ImageView imageView;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    /*public int getCount() {
        return images.size();
    }*/

    public int getCount() {
        return mergedReports.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        viewReportHead = (TextView) view.findViewById(R.id.viewReportHead);
        viewReportDesc = (TextView) view.findViewById(R.id.viewReportDesc);
        viewReportDateTime = (TextView) view.findViewById(R.id.viewReportDateTime);
        viewReportType = (ImageView) view.findViewById(R.id.viewReportType);
        viewUserImage = (CircleImageView) view.findViewById(R.id.imgViewProfilePic);
       // imageView.setImageResource(images[position]);

        viewReportHead.setText(mergedReports.get(position).getUserName());
        viewReportDesc.setText(mergedReports.get(position).getDescription());
        viewReportDateTime.setText(mergedReports.get(position).getDateTime());

        Picasso.with(context)
                .load(ListItem.getReportTypeImage(mergedReports.get(position).getReportType()))
                .fit()
                .into(viewReportType);

        Picasso.with(context)
                .load(mergedReports.get(position).getUserPhoto())
                .fit()
                .into(viewUserImage);

        Picasso.with(context)
                .load(mergedReports.get(position).getReportPhoto())
                .fit()
                .into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

    /*public void initializeImages(List<String> imgs) {
        this.images = imgs;
    }*/

    public void initializeMergedReports(List<String> mergedReportsID) {
        for(String reportID : mergedReportsID) {
            MergedReportsModel mrm = new MergedReportsModel();
            ListItem li = FirebaseDatabaseManager.getVerifiedReport(reportID);

            mrm.setDateTime(FirebaseDatabaseManager.formatDate(li.getDateTime()));
            mrm.setDescription(li.getDescription());
            mrm.setUserPhoto(li.getUserPhoto());
            mrm.setReportPhoto(li.getImageURL());
            mrm.setUserName(FirebaseDatabaseManager.getFullName(li.getReportedBy()));
            mrm.setReportType(li.getReportType());

            mergedReports.add(mrm);
        }
    }
}
