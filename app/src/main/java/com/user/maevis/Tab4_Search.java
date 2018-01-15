package com.user.maevis;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.maevis.controllers.cNotification;
import com.user.maevis.models.ReportModel;
import com.user.maevis.models.UserModel;

public class Tab4_Search extends Fragment implements View.OnClickListener {
    private Button btnShowNotif;
    EditText mSearchField;
    Button mBtnSearch;
    RecyclerView mRecyvlerView;
    DatabaseReference mUserDatabase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_search, container, false);
        setHasOptionsMenu(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");

        mSearchField = (EditText) rootView.findViewById(R.id.txtFldSearch);
        mRecyvlerView = (RecyclerView) rootView.findViewById(R.id.list_item_search);
        mRecyvlerView.setHasFixedSize(true);
        mRecyvlerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBtnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        try {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName())
            );

            searchView.setQueryHint("Search Location");
            searchView.setIconified(false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    // do your search

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // do your search on change or save the last string or...
                    return false;
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if(v==mBtnSearch) {
//            cNotification.alertNotification(getContext());
//            cNotification.vibrateNotif(getContext()
            Toast.makeText(getContext(), "Search", Toast.LENGTH_LONG).show();
            firebaseUserSearch();
        }
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String firstname){
            TextView userFirstName = (TextView) mView.findViewById(R.id.textViewHead);

            userFirstName.setText(firstname);

        }
    }

    private void firebaseUserSearch(){
        FirebaseRecyclerAdapter<UserModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserModel, UsersViewHolder>(
            UserModel.class,
            R.layout.list_search,
            UsersViewHolder.class,
                mUserDatabase
            ){
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, UserModel users, int position){
                viewHolder.setDetails(users.getFirstName());
            }
        };

        mRecyvlerView.setAdapter(firebaseRecyclerAdapter);
    }
}
