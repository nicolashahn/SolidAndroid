package com.csform.android.uiapptemplate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpandableListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpandableListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpandableListViewFragment extends Fragment {
	private static Firebase ref;
    private static String url;
	private static Context ctx;
    private AnimatedExpandableListView listView;
	private ExampleAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private List<GroupItem> items = new ArrayList<>();

    public static ExpandableListViewFragment newInstance(String arg_url) {
        ExpandableListViewFragment fragment = new ExpandableListViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        url = arg_url;
        return fragment;
    }

    public ExpandableListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ctx = getActivity().getApplicationContext();
        Firebase.setAndroidContext(ctx);
        ref = new Firebase(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		ref.addChildEventListener(new ChildEventListener() {
			@Override
			//  A DataSnapshot instance contains data from a Firebase location.
			public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
				if (snapshot.getValue() == null) return;
				Map<?, ?> newPost = (Map<?, ?>) snapshot.getValue();
				GroupItem item = new GroupItem();
				ChildItem child = new ChildItem();
				item.title = newPost.get("title") + "";
				child.title = newPost.get("description") + "";
				item.items.add(child);
				items.add(item);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
			}

			@Override
			public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
			}

			@Override
			public void onChildRemoved(DataSnapshot snapshot) {
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
				System.out.println("The read failed: " + firebaseError.getMessage());
			}
		});

        adapter = new ExampleAdapter(ctx);
        adapter.setData(items);
        View V = inflater.inflate(R.layout.fragment_expandable_list_view, container, false);
        listView = (AnimatedExpandableListView)V.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
            int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
        // Set indicator (arrow) to the right
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //Log.v("width", width + "");
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            listView.setIndicatorBounds(width - px, width);
        } else {
            listView.setIndicatorBoundsRelative(width - px, width);
        }
            return V;
        }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Log.i("OnOptions", "trigger");
			getFragmentManager().popBackStack();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private static class GroupItem {
		String title;
		List<ChildItem> items = new ArrayList<ChildItem>();
	}

	private static class ChildItem {
		String title;
		//String hint;
	}

	private static class ChildHolder {
		TextView title;
		//TextView hint;
	}

	private static class GroupHolder {
		TextView title;
	}

	/**
	 * Adapter for our list of {@link GroupItem}s.
	 */
	private class ExampleAdapter extends AnimatedExpandableListAdapter {
		private LayoutInflater inflater;

		private List<GroupItem> items;

		public ExampleAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<GroupItem> items) {
			this.items = items;
		}

		@Override
		public ChildItem getChild(int groupPosition, int childPosition) {
			return items.get(groupPosition).items.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getRealChildView(int groupPosition, int childPosition,
									 boolean isLastChild, View convertView, ViewGroup parent) {
			ChildHolder holder;
			ChildItem item = getChild(groupPosition, childPosition);
			if (convertView == null) {
				holder = new ChildHolder();
				convertView = inflater.inflate(R.layout.list_item, parent,
						false);
				holder.title = (TextView) convertView
						.findViewById(R.id.textTitle);
				/*holder.hint = (TextView) convertView
						.findViewById(R.id.textHint);*/
				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}

			holder.title.setText(item.title);
			//holder.hint.setText(item.hint);

			return convertView;
		}

		@Override
		public int getRealChildrenCount(int groupPosition) {
			return items.get(groupPosition).items.size();
		}

		@Override
		public GroupItem getGroup(int groupPosition) {
			return items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return items.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
								 View convertView, ViewGroup parent) {
			GroupHolder holder;
			GroupItem item = getGroup(groupPosition);
			if (convertView == null) {
				holder = new GroupHolder();
				convertView = inflater.inflate(R.layout.group_item, parent,
						false);
				holder.title = (TextView) convertView
						.findViewById(R.id.textTitle);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}

			holder.title.setText(item.title);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

	}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
