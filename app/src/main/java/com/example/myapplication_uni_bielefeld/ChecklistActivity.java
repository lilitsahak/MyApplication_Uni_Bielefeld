package com.example.myapplication_uni_bielefeld;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication_uni_bielefeld.Helper.LocalHelper;

import io.paperdb.Paper;


public class ChecklistActivity extends AppCompatActivity {

   // public static final String INFO = "info";
    private Toolbar toolbar;
    private ImageView imageView;

    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    CheckBox cb5;
    TextView text;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"en"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        imageView = findViewById(R.id.image);
        Paper.init(this);
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language", "en");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cb1 = findViewById(R.id.check1);
        cb2 = findViewById(R.id.check2);
        cb3 = findViewById(R.id.check3);
        cb4 = findViewById(R.id.check4);
        cb5 = findViewById(R.id.check5);
        text = findViewById(R.id.textView);

        cb1.setChecked(Paper.book()
                .read("cb1",  "unchecked")
                .equals("checked"));
        cb2.setChecked(Paper.book()
                .read("cb2",  "unchecked")
                .equals("checked"));
        cb3.setChecked(Paper.book()
                .read("cb3",  "unchecked")
                .equals("checked"));
        cb4.setChecked(Paper.book()
                .read("cb4",  "unchecked")
                .equals("checked"));
        cb5.setChecked(Paper.book()
                .read("cb5",  "unchecked")
                .equals("checked"));

        onCheckClicked(null);

        updateView((String) Paper.book().read("language"));
    }

    private void updateView(String lang) {
        Context context = LocalHelper.setLocale(this, lang);
        Resources resources = context.getResources();
        cb1.setText(Html.fromHtml(resources.getString(R.string.cb1), Html.FROM_HTML_MODE_COMPACT));
        cb2.setText(Html.fromHtml(resources.getString(R.string.cb2), Html.FROM_HTML_MODE_COMPACT));
        cb3.setText(Html.fromHtml(resources.getString(R.string.cb3), Html.FROM_HTML_MODE_COMPACT));
        cb4.setText(Html.fromHtml(resources.getString(R.string.cb4), Html.FROM_HTML_MODE_COMPACT));
        cb5.setText(Html.fromHtml(resources.getString(R.string.cb5), Html.FROM_HTML_MODE_COMPACT));

        getSupportActionBar().setTitle(resources.getString(R.string.Chechlist));
        imageView.setImageResource(R.drawable.pager_pic_3);
    }

    public void onCheckClicked(View view) {
        Paper.book().write("cb1", cb1.isChecked() ? "checked" : "unchecked");
        Paper.book().write("cb2", cb2.isChecked() ? "checked" : "unchecked");
        Paper.book().write("cb3", cb3.isChecked() ? "checked" : "unchecked");
        Paper.book().write("cb4", cb4.isChecked() ? "checked" : "unchecked");
        Paper.book().write("cb5", cb5.isChecked() ? "checked" : "unchecked");


        if (cb1.isChecked() && cb2.isChecked() && cb3.isChecked() && cb4.isChecked() && cb5.isChecked())
        {
            text.setText("wow congrats");
        } else if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked() || cb5.isChecked() ) {
            text.setText("you still have some steps to do");
        }
    }






    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
