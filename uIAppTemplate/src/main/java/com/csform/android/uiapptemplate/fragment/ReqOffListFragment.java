package com.csform.android.uiapptemplate.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.adapter.ReqOffListAdapter;
import com.csform.android.uiapptemplate.model.FavorModel;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReqOffListFragment extends Fragment {
	private static Firebase ref;
    private static String url;
	private static Context ctx;
	private ReqOffListAdapter adapter;
    private AnimatedExpandableListView listView;
    private OnFragmentInteractionListener mListener;
    private List<FavorModel> favorList = new ArrayList<>();
	private DynamicListView mDynamicListView;

    public static ReqOffListFragment newInstance(String arg_url) {
        ReqOffListFragment fragment = new ReqOffListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        url = arg_url;
        return fragment;
    }

    public ReqOffListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ctx = getActivity().getApplicationContext();
        Firebase.setAndroidContext(ctx);
        ref = new Firebase(url);
		setHasOptionsMenu(true);
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
//				if (newPost.get("title") == null || newPost.get("description") == null ||
//						newPost.get("completed") != "false") return;
				FavorModel favor = new FavorModel();
				favor.setTitle(newPost.get("title") + "");
				favor.setDesc(newPost.get("description") + "");
                favor.setImageURL(newPost.get("avatar") + "");
				favor.setKey(snapshot.getKey());
				favorList.add(favor);
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
        View V = inflater.inflate(R.layout.fragment_reqoff_list_view, container, false);
		mDynamicListView = (DynamicListView) V.findViewById(R.id.dynamic_listview);
        adapter = new ReqOffListAdapter(ctx, favorList, false, mListener);
		AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        //AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
		animAdapter.setAbsListView(mDynamicListView);
		mDynamicListView.setAdapter(animAdapter);

        return V;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
	@Override
    public void onCreateOptionsMenu(
      Menu menu, MenuInflater inflater) {
//		inflater.inflate(R.menu.menu_expandable_list, menu);
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
        public void onFragmentInteraction(String key);
    }
	public void onListItemClick(String key){
		mListener.onFragmentInteraction(key);
	}
}
