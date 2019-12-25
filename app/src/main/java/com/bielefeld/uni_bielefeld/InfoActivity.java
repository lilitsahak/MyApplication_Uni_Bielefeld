package com.bielefeld.uni_bielefeld;

import android.content.Context;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bielefeld.uni_bielefeld.helper.LocalHelper;

import io.paperdb.Paper;

public class InfoActivity extends AppCompatActivity {



    public static final String TITLE = "title";
    public static final String INFO = "info";
    public static final String IMAGE = "image";

    private Toolbar toolbar;
    private TextView infoTextView;
    private ImageView imageView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        infoTextView = findViewById(R.id.html_info);
        imageView = findViewById(R.id.image);
        Paper.init(this);
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language", "en");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateView((String) Paper.book().read("language"));

    }
    private void updateView(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        toolbar.setTitle(resources.getString(getIntent().getIntExtra(TITLE, 0)));
        String info = resources.getString(getIntent().getIntExtra(INFO, 0)).toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            infoTextView.setText(Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT));
        } else {
            infoTextView.setText(Html.fromHtml(info));
        }
        imageView.setImageResource(getIntent().getIntExtra(IMAGE, 0));
        infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}