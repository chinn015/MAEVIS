package com.user.maevis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.Comment;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;
import com.user.maevis.models.UserModel;
import com.user.maevis.session.SessionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportPage extends AppCompatActivity  implements View.OnClickListener  {


    private CircleImageView viewUserImage;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;


    private static final String TAG = "ReportPage";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private DatabaseReference mImageListReference;

    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;
    private CircleImageView viewUserPhoto;
    private ListItemVerified clickedReport;

    private List<String> imageUrl;

    private ImageView viewReportType;
    private TextView viewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        viewReportType = (ImageView) findViewById(R.id.viewReportType);
        viewLocation = (TextView) findViewById(R.id.viewLocation);

        mCommentField = findViewById(R.id.comment);
        mCommentButton = findViewById(R.id.button_post_comment);
        mCommentsRecycler = findViewById(R.id.recycler_comments);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if(PageNavigationManager.getClickedTabHomeListItemVerified() != null) {
            clickedReport = PageNavigationManager.getClickedTabHomeListItemVerified();
            mPostKey = PageNavigationManager.getClickedTabHomeListItemVerified().getReportID();

            viewLocation.setText(PageNavigationManager.getClickedTabHomeListItemVerified().getLocation());
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportMarkerImage(PageNavigationManager.getClickedTabHomeListItemVerified().getReportType()))
                    .into(viewReportType);


        }else if (PageNavigationManager.getClickedTabLocListItemVerified() != null){
            clickedReport = PageNavigationManager.getClickedTabLocListItemVerified();
            mPostKey = PageNavigationManager.getClickedTabLocListItemVerified().getReportID();

            viewLocation.setText(PageNavigationManager.getClickedTabLocListItemVerified().getLocation());
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportMarkerImage(PageNavigationManager.getClickedTabLocListItemVerified().getReportType()))
                    .into(viewReportType);


        }else if(PageNavigationManager.getClickedTabNotifListItem() != null){
            mPostKey = PageNavigationManager.getClickedTabNotifListItem().getReportID();

            viewLocation.setText(PageNavigationManager.getClickedTabNotifListItem().getLocation());
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportMarkerImage(PageNavigationManager.getClickedTabNotifListItem().getReportType()))
                    .into(viewReportType);

        }else if(PageNavigationManager.getClickedTabNotifRegListItem() != null){
            clickedReport = PageNavigationManager.getClickedTabNotifRegListItem();
            mPostKey = PageNavigationManager.getClickedTabNotifRegListItem().getReportID();

            viewLocation.setText(PageNavigationManager.getClickedTabNotifRegListItem().getLocation());
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportMarkerImage(PageNavigationManager.getClickedTabNotifRegListItem().getReportType()))
                    .into(viewReportType);

        }


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        //viewPagerAdapter.initializeImages(clickedReport.getImageList());
        viewPagerAdapter.initializeMergedReports(clickedReport.getMergedReportsID());

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference().child("Reports").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(mPostKey);

//        viewUserImage.setOnClickListener(this);
        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Listen for comments
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Clean up comments listener
        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        if(v==viewUserImage) {
            if(SessionManager.getUserType().equals("Admin")) {
                Toast.makeText(this, "User Management", Toast.LENGTH_LONG).show();

                if (PageNavigationManager.getClickedTabHomeListItemVerified() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabHomeListItemVerified().getReportedBy(),
                            PageNavigationManager.KEY_TABHOME,
                            PageNavigationManager.getClickedTabHomeListItemVerified());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabNotifListItem() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabNotifListItem().getReportedBy(),
                            PageNavigationManager.KEY_TABNOTIF,
                            PageNavigationManager.getClickedTabNotifListItem());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabLocListItemVerified() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabLocListItemVerified().getReportedBy(),
                            PageNavigationManager.KEY_TABLOCVERIFIED,
                            PageNavigationManager.getClickedTabLocListItemVerified());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                PageNavigationManager.clickReportPageUserItem(FirebaseDatabaseManager.getUserItem(PageNavigationManager.getClickedUserID()));
                if(PageNavigationManager.getClickedReportPageUserItem() != null) {
                    Intent i = new Intent(this, UserManagement.class);
                    startActivity(i);
                }
            }
        }else if(v == mCommentButton){

            if (TextUtils.isEmpty(mCommentField.getText())) {
                Toast.makeText(this, "Please enter comment.", Toast.LENGTH_SHORT).show();
            }else{
                postComment();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home  && TabProfileTimelineAdapter.getClickedItem() != null) {

            startActivity(new Intent(ReportPage.this, SidebarProfile.class));
            finish();

        }else if (id == android.R.id.home  && PageNavigationManager.getClickedTabHomeListItemVerified() != null) {

            startActivity(new Intent(ReportPage.this, Sidebar_HomePage.class));
            finish();

        }else if (id == android.R.id.home  && PageNavigationManager.getClickedTabLocListItemVerified() != null){

            startActivity(new Intent(ReportPage.this, Sidebar_HomePage.class));
            finish();

        }else if (id == android.R.id.home  && TabNotifAdapterRegUser.getClickedItem() != null){

            startActivity(new Intent(ReportPage.this, Sidebar_HomePage.class));
            finish();

        }else{
            finish();

        }

        TabProfileTimelineAdapter.setClickedItem(null);
        PageNavigationManager.setClickedTabHomeListItemVerified(null);
        PageNavigationManager.setClickedTabLocListItemVerified(null);
        TabNotifAdapterRegUser.setClickedItem(null);

        return super.onOptionsItemSelected(item);
    }

    private void postComment() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        String authorName = user.getFirstName() + " " + user.getLastName();

                        // Create new comment object
                        String commentText = mCommentField.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        // Push the comment, it will appear in the list
                        mCommentsReference.push().setValue(comment);

                        // Clear the field
                        mCommentField.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;
        public CircleImageView commentPhoto;

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.comment_author);
            bodyView = itemView.findViewById(R.id.comment_body);
            commentPhoto = itemView.findViewById(R.id.comment_photo);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                    // A new comment has been added, add it to the displayed list
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Replace with the new data
                        mComments.set(commentIndex, newComment);

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex);
                    } else {
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);

            Picasso.with(mContext).load(FirebaseDatabaseManager.getUserPhoto(comment.uid)).into(holder.commentPhoto);

        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
}
