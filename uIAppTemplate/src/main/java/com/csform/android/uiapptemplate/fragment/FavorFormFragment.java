package com.csform.android.uiapptemplate.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.csform.android.uiapptemplate.Clock;
import com.csform.android.uiapptemplate.R;
import com.firebase.client.Firebase;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavorFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavorFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavorFormFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public String LOG_TAG = "FavorFormActivity";

    public String FIREBASE_URL = "https://crackling-torch-5178.firebaseio.com/";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavorFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavorFormFragment newInstance(String param1, String param2) {
        FavorFormFragment fragment = new FavorFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FavorFormFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(super.getActivity().getApplicationContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout mLinearLayout = (FrameLayout)inflater.inflate(R.layout.fragment_favor_form, container, false);
        // get the button from the layout
        Button b = (Button) mLinearLayout.findViewById(R.id.fragPostButton);
        // make its click listener call postButton()
        b.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               postButton();
           }
        });


        // populate spinner with categories
        Spinner spin = (Spinner) mLinearLayout.findViewById(R.id.categorySpinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setPrompt("Choose category");
        spin.setAdapter(adapter);

        return mLinearLayout;
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

    public void postButton(){
        // get server reference, then requests database, then build new request
        Firebase ref = new Firebase(FIREBASE_URL);

        // first get whether we're sending a request or offer
        Switch s = (Switch) getActivity().findViewById(R.id.reqOfferSwitch);
        String type = "requests";
        if(s.isChecked()) {
            type = "offers";
        }

        // now we send either an offer or request
        Firebase favorRef = ref.child(type);
        Firebase newFavorRef = favorRef.push();

        // get all the strings from the form
        EditText titleField = (EditText) getActivity().findViewById(R.id.titleField);
        String title = titleField.getText().toString();

        EditText descField = (EditText) getActivity().findViewById(R.id.descField);
        String desc = descField.getText().toString();

        EditText dateField = (EditText) getActivity().findViewById(R.id.dateField);
        String date = dateField.getText().toString();

        EditText compField = (EditText) getActivity().findViewById(R.id.compField);
        String comp = compField.getText().toString();

        Spinner catSpin = (Spinner) getActivity().findViewById(R.id.categorySpinner);
        String category = catSpin.getSelectedItem().toString();

        ///////////////////////////
        // TODO:
        // checkbox for monetary compensation,
        // currencyField - float?
        //////////////////////////

        // a random number for the favor ID
        SecureRandom random = new SecureRandom();
        String favorId = new BigInteger(130, random).toString(32);


        // build a request object, send it to server
        Map<String, String> f1 = new HashMap<String, String>();
        f1.put("title", title);
        f1.put("description", desc);
        f1.put("completed", "false");
        f1.put("compensation",comp);
        f1.put("dateToBeCompletedBy",date);
        f1.put("datePosted", Clock.getTimeStamp());
        // change this later - use appInfo from hw3?
        f1.put("userPosted","batman");
        f1.put("favorId",favorId);
        f1.put("category",category);
        newFavorRef.setValue(f1);


        // this is to get the id of the object we just sent to the server
        String returnId = newFavorRef.getKey();
        Log.i(LOG_TAG, "key id = " + returnId);

//        Toast t = Toast.makeText(this.getApplicationContext(), "Favor posted!", Toast.LENGTH_SHORT);
//        t.show();

    }

}
