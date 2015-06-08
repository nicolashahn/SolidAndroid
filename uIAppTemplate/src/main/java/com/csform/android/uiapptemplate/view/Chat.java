package com.csform.android.uiapptemplate.view;


public class Chat {

    private String message;
    private String author;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String author) {
        this.message = message;
        this.author = author;
        System.out.println("Author is " + author);
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
