package com.example.myapplication_uni_bielefeld;

import  android.app.Application;
import android.content.Context;

import com.example.myapplication_uni_bielefeld.Helper.LocalHelper;

import io.paperdb.Paper;

public class MainApplication  extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base, "en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Paper.init(this);
        String language = Paper.book().read("language");
        if (language == null) {
            Paper.book().write("language", "en");
        }
    }
}
