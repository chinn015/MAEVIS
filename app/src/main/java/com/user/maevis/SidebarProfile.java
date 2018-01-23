/*
 * Copyright (C) 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.user.maevis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.user.maevis.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SidebarProfile extends AppCompatActivity
	implements AppBarLayout.OnOffsetChangedListener {

	private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
	private boolean mIsAvatarShown = true;
	FloatingActionButton btnAddReport;
	private ImageView mProfileImage;
	private int mMaxScrollSize;
	TextView profileName;
	CircleImageView profilePic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sidebar_profile);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
		ViewPager viewPager  = (ViewPager) findViewById(R.id.tab_viewpager);
		AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
		mProfileImage = (ImageView) findViewById(R.id.imgViewProfilePic);

		profileName=(TextView)findViewById(R.id.txtViewProfileName);
		profileName.setText(SessionManager.getFirstName()+" "+ SessionManager.getLastName());
		profilePic = (CircleImageView) findViewById(R.id.user_photo);
		Picasso.with(this).load(SessionManager.getUserPhoto()).into(profilePic);


		Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				onBackPressed();
			}
		});

		btnAddReport = (FloatingActionButton) findViewById(R.id.btnAddReport);
		btnAddReport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplication(), SelectionPage.class);
				startActivity(i);
			}
		});

		appbarLayout.addOnOffsetChangedListener(this);
		mMaxScrollSize = appbarLayout.getTotalScrollRange();

//		viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
		viewPager = (ViewPager)findViewById(R.id.tab_viewpager);
		if (viewPager != null){
			setupViewPager(viewPager);
		}
		tabLayout.setupWithViewPager(viewPager);
	}


	private void setupViewPager(ViewPager viewPager){
		SidebarProfile.ViewPagerAdapter adapter = new SidebarProfile.ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(new SidebarProfileTimeline(), "Timeline");
		adapter.addFrag(new SidebarProfileMedia(), "Photos");
		//adapter.addFrag(new SidebarProfileMedia(), "Videos");
		viewPager.setAdapter(adapter);
	}

	static class ViewPagerAdapter extends FragmentPagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public ViewPagerAdapter(FragmentManager manager){
			super(manager);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFrag(Fragment fragment, String title){
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public CharSequence getPageTitle(int position){
			return mFragmentTitleList.get(position);
		}
	}


	public static void start(Context c) {
		c.startActivity(new Intent(c, SidebarProfile.class));
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
		if (mMaxScrollSize == 0)
			mMaxScrollSize = appBarLayout.getTotalScrollRange();

		int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

		if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
			mIsAvatarShown = false;

			mProfileImage.animate()
				.scaleY(0).scaleX(0)
				.setDuration(200)
				.start();
		}

		if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
			mIsAvatarShown = true;

			mProfileImage.animate()
				.scaleY(1).scaleX(1)
				.start();
		}
	}

}
