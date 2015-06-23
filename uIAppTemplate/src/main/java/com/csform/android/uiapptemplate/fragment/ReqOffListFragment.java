package com.csform.android.uiapptemplate.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.adapter.ReqOffListAdapter;
import com.csform.android.uiapptemplate.model.FavorModel;
import com.csform.android.uiapptemplate.model.FloatingActionButton;
import com.csform.android.uiapptemplate.model.UserModel;
import com.csform.android.uiapptemplate.util.FirebaseUtil;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReqOffListFragment extends Fragment{
    private static Firebase ref;
    private static String list;
    private static String url;
    private static Context ctx;
    private static Boolean singleUser;
    private ReqOffListAdapter adapter;
    private AnimatedExpandableListView listView;
    private OnFragmentInteractionListener mListener;
    private List<FavorModel> favorList = new ArrayList<>();
    private DynamicListView mDynamicListView;
    private String USER_EMAIL;

    public static ReqOffListFragment newInstance(String list_) {
        ReqOffListFragment fragment = new ReqOffListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        list = list_;
        singleUser = false;
        return fragment;
    }

    public static ReqOffListFragment newInstance(String list_, Boolean singleUser_) {
        ReqOffListFragment fragment = new ReqOffListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        list = list_;
        singleUser = singleUser_;
        return fragment;
    }

    public ReqOffListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = FirebaseUtil.getUrl();
        ctx = getActivity().getApplicationContext();
        Firebase.setAndroidContext(ctx);
        ref = new Firebase(url);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(list.equals("")) return inflater.inflate(R.layout.fragment_reqoff_list_view, container, false);
        ref.child(list).addChildEventListener(new ChildEventListener() {
            @Override
            //  A DataSnapshot instance contains data from a Firebase location.
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                if (!snapshot.exists()) return;
                Map<?, ?> favorMap = (Map<?, ?>) snapshot.getValue();
                final FavorModel favor = new FavorModel();
                favor.setUser(favorMap.get("userPosted") + "");
                String user_key =  emailToKey(UserModel.getField(getActivity(), "email"));
                if (singleUser) {
                    if (!favor.getUser().equals(user_key)) {
                        return;
                    }
                }
                favor.setTitle(favorMap.get("title") + "");
                favor.setDesc(favorMap.get("description") + "");
                favor.setDatePosted(favorMap.get("datePosted") + "");
                favor.setDateDoneBy(favorMap.get("dateToBeCompletedBy") + "");
                favor.setCompensation(favorMap.get("compensation") + "");
                ref.child("users").child(emailToKey(favorMap.get("userPosted") + "")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            return;
                        }
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                        }
                        Map<?, ?> userMap = (Map<?, ?>) dataSnapshot.getValue();
                        favor.setUserImage(userMap.get("avatar") + "");
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                favor.setKey(snapshot.getKey());
                favorList.add(0, favor); // display list from newest to oldest
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
        // initial list population
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        View V = inflater.inflate(R.layout.fragment_reqoff_list_view, container, false);
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) V.findViewById(R.id.reqoff_list_swipe_refresh_layout);
        mDynamicListView = (DynamicListView) V.findViewById(R.id.dynamic_listview);
        adapter = new ReqOffListAdapter(ctx, favorList, false, mListener, singleUser);
        AnimationAdapter animAdapter = new AlphaInAnimationAdapter(adapter);
        //AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        animAdapter.setAbsListView(mDynamicListView);
        mDynamicListView.setAdapter(animAdapter);

        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                }, 750);
            }
        });

        FloatingActionButton fab = new FloatingActionButton.Builder(this.getActivity(), (FrameLayout)V.findViewById(R.id.frameLayout))
                .withDrawable(getResources().getDrawable(R.drawable.ic_add))
                .withButtonColor(getResources().getColor(R.color.material_blue_800))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 5, 5)
                .create();
/*
        fab.setOnClickListener(V.OnClickListener(){

            @Override

                    public void onClick(View V){
                //some code to launch the form
            }
        }*/

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
        void onFragmentInteraction(FavorModel fm);
    }

    public String emailToKey(String emailAddress) {
        return emailAddress.replace('.', ',');
    }
}