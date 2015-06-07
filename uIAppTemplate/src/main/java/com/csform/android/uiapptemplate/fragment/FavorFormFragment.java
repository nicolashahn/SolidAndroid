package com.csform.android.uiapptemplate.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.csform.android.uiapptemplate.Clock;
import com.csform.android.uiapptemplate.R;
import com.csform.android.uiapptemplate.model.UserModel;
import com.firebase.client.Firebase;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private static Firebase ref;
    private static String list;
    private static String url;
    private static Context ctx;

    private OnFragmentInteractionListener mListener;
    public String LOG_TAG = "FavorFormActivity";
    public String FIREBASE_URL = "https://crackling-torch-5178.firebaseio.com/";

    public static FavorFormFragment newInstance(String url_, String list_) {
        FavorFormFragment fragment = new FavorFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        url = url_;
        list = list_;
        return fragment;
    }

    public FavorFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(super.getActivity().getApplicationContext());
        ctx = getActivity().getApplicationContext();
        Firebase.setAndroidContext(ctx);
        ref = new Firebase(url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout mLinearLayout = (LinearLayout)inflater.inflate(R.layout.fragment_favor_form, container, false);
        // get the button from the layout
        TextView b = (TextView) mLinearLayout.findViewById(R.id.postFavor);
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
        // auto fill the completed by date to be tomorrow
        autofillDate(mLinearLayout);

        TextView userEmail = (TextView) mLinearLayout.findViewById(R.id.userEmailView);
        userEmail.setText(UserModel.getField(getActivity(), "email"));

        return mLinearLayout;
    }

    public void autofillDate(LinearLayout mLinearLayout){
        EditText df = (EditText) mLinearLayout.findViewById(R.id.dateEnd);
        // get tomorrow's date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrowAsString = dateFormat.format(tomorrow);
        df.setText(tomorrowAsString);
//        Log.i(LOG_TAG, "in autofillDate()");
    }

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

        // now we send either an offer or request
        Firebase favorRef = ref.child(list);
        Firebase newFavorRef = favorRef.push();

        // get all the strings from the form
        EditText titleField = (EditText) getActivity().findViewById(R.id.title);
        String title = titleField.getText().toString();

        EditText descField = (EditText) getActivity().findViewById(R.id.description);
        String desc = descField.getText().toString();

        EditText dateField = (EditText) getActivity().findViewById(R.id.dateEnd);
        String date = dateField.getText().toString();

        EditText compField = (EditText) getActivity().findViewById(R.id.compensation);
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