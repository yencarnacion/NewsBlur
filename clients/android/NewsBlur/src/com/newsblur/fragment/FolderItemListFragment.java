package com.newsblur.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.newsblur.R;
import com.newsblur.activity.FeedReading;
import com.newsblur.activity.FolderReading;
import com.newsblur.activity.ItemsList;
import com.newsblur.activity.Reading;
import com.newsblur.database.DatabaseConstants;
import com.newsblur.database.MultipleFeedItemsAdapter;
import com.newsblur.util.DefaultFeedView;
import com.newsblur.util.StateFilter;
import com.newsblur.util.StoryOrder;
import com.newsblur.view.FeedItemViewBinder;

public class FolderItemListFragment extends ItemListFragment implements OnItemClickListener {

	private String folderName;
    private ListView itemList;
	
	public static FolderItemListFragment newInstance(String folderName, StateFilter currentState, DefaultFeedView defaultFeedView) {
		FolderItemListFragment feedItemFragment = new FolderItemListFragment();

		Bundle args = new Bundle();
		args.putSerializable("currentState", currentState);
		args.putString("folderName", folderName);
        args.putSerializable("defaultFeedView", defaultFeedView);
		feedItemFragment.setArguments(args);

		return feedItemFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		folderName = getArguments().getString("folderName");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_itemlist, null);
		itemList = (ListView) v.findViewById(R.id.itemlistfragment_list);
        setupBezelSwipeDetector(itemList);
		itemList.setEmptyView(v.findViewById(R.id.empty_view));
		itemList.setOnItemClickListener(this);
		itemList.setOnCreateContextMenuListener(this);
		itemList.setOnScrollListener(this);
        if (adapter != null) {
            // normally the list gets set up when the adapter is created, but sometimes
            // onCreateView gets re-called.
            itemList.setAdapter(adapter);
        }

		getLoaderManager().initLoader(ITEMLIST_LOADER , null, this);

		return v;
	}

    @Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if ((adapter == null) && (cursor != null)) {
            String[] groupFrom = new String[] { DatabaseConstants.STORY_TITLE, DatabaseConstants.STORY_SHORT_CONTENT, DatabaseConstants.FEED_TITLE, DatabaseConstants.STORY_TIMESTAMP, DatabaseConstants.STORY_INTELLIGENCE_AUTHORS, DatabaseConstants.STORY_AUTHORS };
            int[] groupTo = new int[] { R.id.row_item_title, R.id.row_item_content, R.id.row_item_feedtitle, R.id.row_item_date, R.id.row_item_sidebar, R.id.row_item_author };
            adapter = new MultipleFeedItemsAdapter(getActivity(), R.layout.row_folderitem, cursor, groupFrom, groupTo);
            adapter.setViewBinder(new FeedItemViewBinder(getActivity()));
            itemList.setAdapter(adapter);
       }
       super.onLoadFinished(loader, cursor);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getActivity().isFinishing()) return;
		Intent i = new Intent(getActivity(), FolderReading.class);
        i.putExtra(Reading.EXTRA_FEEDSET, getFeedSet());
		i.putExtra(FeedReading.EXTRA_POSITION, position);
		i.putExtra(FeedReading.EXTRA_FOLDERNAME, folderName);
		i.putExtra(ItemsList.EXTRA_STATE, currentState);
        i.putExtra(Reading.EXTRA_DEFAULT_FEED_VIEW, defaultFeedView);
		startActivity(i);
	}

}
