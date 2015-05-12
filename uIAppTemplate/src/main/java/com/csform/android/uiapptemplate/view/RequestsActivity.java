package com.csform.android.uiapptemplate.view;

import android.os.Bundle;

import com.csform.android.uiapptemplate.ExpandableListViewActivity;
import com.csform.android.uiapptemplate.R;
import com.firebase.client.Firebase;

/*
 * disregard this mess for now
 */
public class RequestsActivity extends ExpandableListViewActivity {
    // temp location for Firebase init
    private static final String FIREBASE_URL = "https://crackling-torch-5178.firebaseio.com/";
    private Firebase ref;
//    private List <GroupItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_requests);
        Firebase.setAndroidContext(this);
        ref = new Firebase(FIREBASE_URL);
        super.onCreate(savedInstanceState);
//        populateList();
    }

/*    @Override
    private void populateList(){
        Firebase reqRef = ref.child("requests");

        reqRef.addChildEventListener(new ChildEventListener() {
            @Override
            //  A DataSnapshot instance contains data from a Firebase location.
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                if (snapshot.getValue() == null) return;
                // Map<String, Object>
                Map<?, ?> newPost = (Map<?, ?>) snapshot.getValue();
                GroupItem item = new GroupItem();
                item.title = newPost.get("title") + "";
                ChildItem child = new ChildItem();
                child.title = newPost.get("description") + "";
                item.items.add(child);
                listItems.add(item);
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
    }
    protected static class GroupItem {
		String title;
		List<ChildItem> items = new ArrayList<ChildItem>();
	}
    protected static class ChildItem {
		String title;
		//String hint;
	}
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_requests, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
