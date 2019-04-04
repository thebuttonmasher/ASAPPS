package com.codestew.circles;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class AppClass extends Application {
    //Todo: add feature: infinite feed
    //Todo: add feature: create circles
    //Todo: add feature: comments
    //Todo: add feature: post scores

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        // Required initialization logic here!
    }


}
