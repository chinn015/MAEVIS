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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SidebarProfileMedia extends Fragment {
	private RecyclerView mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = (RecyclerView) inflater.inflate(R.layout.sidebar_profile_media, container, false);
		return mRootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initRecyclerView();
	}

	private void initRecyclerView() {
		mRootView.setAdapter(new PageAdapter(20));
	}

	public static Fragment newInstance() {
		return new SidebarProfileMedia();
	}


	private static class PageAdapter extends RecyclerView.Adapter<PageVH> {
		private final int numItems;

		PageAdapter(int numItems) {
			this.numItems = numItems;
		}

		@Override
		public PageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
			View itemView = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.list_profile_media, viewGroup, false);

			return new PageVH(itemView);
		}

		@Override
		public void onBindViewHolder(PageVH fakePageVH, int i) {
			// do nothing
		}

		@Override
		public int getItemCount() {
			return numItems;
		}
	}

	private static class PageVH extends RecyclerView.ViewHolder {
		PageVH(View itemView) {
			super(itemView);
		}
	}
}
