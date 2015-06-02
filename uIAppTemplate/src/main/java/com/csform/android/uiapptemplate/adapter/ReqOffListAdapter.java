package com.csform.android.uiapptemplate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.fragment.ReqOffListFragment;
import com.csform.android.uiapptemplate.model.FavorModel;
import com.csform.android.uiapptemplate.util.ImageUtil;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.Collections;
import java.util.List;

public class ReqOffListAdapter extends BaseAdapter implements Swappable, UndoAdapter, OnDismissCallback {


	private Context mContext;
	private LayoutInflater mInflater;
	private List<FavorModel> favorList;
    private ReqOffListFragment.OnFragmentInteractionListener mListener;
	private boolean mShouldShowDragAndDropIcon;

	public ReqOffListAdapter(Context context, List<FavorModel> favorItemsList, boolean shouldShowDragAndDropIcon,
							 ReqOffListFragment.OnFragmentInteractionListener mListener) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		favorList = favorItemsList;
		mShouldShowDragAndDropIcon = shouldShowDragAndDropIcon;
		this.mListener = mListener;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		return favorList.size();
	}

	@Override
	public Object getItem(int position) {
		return favorList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return favorList.get(position).getId();
	}

	public String getItemKey(int position) {
		return favorList.get(position).getKey();
	}

	@Override
	public void swapItems(int positionOne, int positionTwo) {
		Collections.swap(favorList, positionOne, positionTwo);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_default, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);
//            holder.desc = (TextView) convertView.findViewById(R.id.desc);
			//holder.icon = (TextView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final FavorModel fm = favorList.get(position);
		Log.i("IMAGE", fm.getUserImage());
		ImageUtil.displayImage(holder.image, fm.getUserImage(), null);
		holder.text.setText(fm.getTitle());
		/*
		if (mShouldShowDragAndDropIcon) {
			holder.icon.setText(R.string.fontello_drag_and_drop);
		} else {
			holder.icon.setText(fm.getIconRes());
		}
		*/
		 convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        notifyDataSetChanged();
					mListener.onFragmentInteraction(fm);
                    }
            });

		return convertView;
	}

	private static class ViewHolder {
		public ImageView image;
		public /*Roboto*/TextView text;
		public /*Fontello*/TextView icon;
	}

	@Override
	@NonNull
	public View getUndoClickView(@NonNull View view) {
		return view.findViewById(R.id.undo_button);
	}

	@Override
	@NonNull
	public View getUndoView(final int position, final View convertView,
			@NonNull final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_undo_view,
					parent, false);
		}
		return view;
	}

	@Override
	public void onDismiss(@NonNull final ViewGroup listView,
			@NonNull final int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			remove(position);
		}
	}
	public void remove(int position) {
		favorList.remove(position);
	}
}
