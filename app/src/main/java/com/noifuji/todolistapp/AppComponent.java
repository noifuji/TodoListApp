package com.noifuji.todolistapp;

import android.app.Application;

import com.noifuji.todolistapp.db.AppDatabase;

public class AppComponent extends Application {
    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }
}
