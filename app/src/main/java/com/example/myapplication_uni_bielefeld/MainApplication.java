package com.example.myapplication_uni_bielefeld;

import  android.app.Application;
import android.content.Context;

import com.example.myapplication_uni_bielefeld.Helper.LocalHelper;

public class MainApplication  extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base, "en"));
    }
}
