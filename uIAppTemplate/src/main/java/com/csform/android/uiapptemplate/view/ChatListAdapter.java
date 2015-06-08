package com.csform.android.uiapptemplate.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.csform.android.uiapptemplate.R;
import com.firebase.client.Query;


public class ChatListAdapter extends com.csform.android.uiapptemplate.view.FirebaseListAdapter<com.csform.android.uiapptemplate.view.Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, com.csform.android.uiapptemplate.view.Chat.class, layout, activity);
        this.mUsername = mUsername;
    }

    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview

        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}
