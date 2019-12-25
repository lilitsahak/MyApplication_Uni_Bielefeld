package com.bielefeld.uni_bielefeld;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bielefeld.uni_bielefeld.adapter.MenuAdapter;
import com.bielefeld.uni_bielefeld.forum.ForumLoginActivity;
import com.bielefeld.uni_bielefeld.forum.TopicListActivity;
import com.bielefeld.uni_bielefeld.helper.LocalHelper;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private MenuAdapter menuAdapter;
    private List<PagerItemModel> pagerItemModels;
    private Button languageButton;
    private Button shareButton;
    private Button forumButton;

    private LinearLayout dotsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dotsLayout = findViewById(R.id.dotsContainer);

        languageButton = findViewById(R.id.languageButton);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeLanguage(view);
            }
        });

        pagerItemModels = new ArrayList<>();

        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_1, R.string.About_Bielefeld_and_the_University));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_2, R.string.ProspectiveStudents));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_3, R.string.Deadlines));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_4, R.string.BeforeArrival));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_5, R.string.AfterArrival));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_6, R.string.ServiceInstitutions));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_7, R.string.FinancialAid));
        pagerItemModels.add(new PagerItemModel(R.drawable.pager_pic_6, R.string.ISSC));

        menuAdapter = new MenuAdapter(pagerItemModels, this);
        viewPager = findViewById(R.id.menuPager);
        viewPager.setAdapter(menuAdapter);
        prepareDots(0);

        viewPager.setPadding(130, 0, 130, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int position) {
                prepareDots(position);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        menuAdapter.setOnButtonClickListener(new MenuAdapter.OnButtonClickedListener() {
            @Override
            public void onButtonClicked(int position) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.About_Bielefeld_and_the_University);
                    intent.putExtra(InfoActivity.INFO, R.string.UniBie);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_1);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.ProspectiveStudents);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_1);
                    intent.putExtra(InfoActivity.INFO, R.string.ProStu);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.Deadlines);
                    intent.putExtra(InfoActivity.INFO, R.string.AppDeadlines);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_3);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.BeforeArrival);
                    intent.putExtra(InfoActivity.INFO, R.string.Before_Arrival);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_3);
                    startActivity(intent);
                } else if (position == 4) {
                    Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
                    startActivity(intent);
                } else if (position == 5) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.ServiceInstitutions);
                    intent.putExtra(InfoActivity.INFO, R.string.SerInst);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_3);
                    startActivity(intent);
                } else if (position == 6) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.FinancialAid);
                    intent.putExtra(InfoActivity.INFO, R.string.Financing);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_3);
                    startActivity(intent);
                }
                else if (position == 7) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    intent.putExtra(InfoActivity.TITLE, R.string.ISSC_title);
                    intent.putExtra(InfoActivity.INFO, R.string.ISSC_info);
                    intent.putExtra(InfoActivity.IMAGE, R.drawable.pager_pic_3);
                    startActivity(intent);
                }
            }
            
        });

        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "App for international students";
                String shareSub = "Your Subject here";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });

        forumButton = findViewById(R.id.forum_button);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Paper.book()
                        .read("username",  "");

                if (username.isEmpty()) {
                    startActivity(new Intent(
                            MainActivity.this,
                            ForumLoginActivity.class));
                } else {
                    startActivity(new Intent(
                            MainActivity.this,
                            TopicListActivity.class));
                }
            }
        });

        updateView((String) Paper.book().read("language"));
    }

    private void updateView(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();
        menuAdapter.setContext(context, resources);
        menuAdapter.notifyDataSetChanged();

        languageButton.setText(resources.getString(R.string.current_language));
        shareButton.setText(R.string.share);
        forumButton.setText(R.string.forum);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase, "en"));
    }

    private void prepareDots(int currentSlidePosition) {
        if(dotsLayout.getChildCount() > 0) {
            dotsLayout.removeAllViews();
        }

        ImageView dots[] = new ImageView[pagerItemModels.size()];

        for(int dotIndex = 0; dotIndex < pagerItemModels.size(); dotIndex++) {
            dots[dotIndex] = new ImageView(this);

            if (dotIndex == currentSlidePosition) {
                dots[dotIndex].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));
            } else {
                dots[dotIndex].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dot));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 0, 4, 0);
            dotsLayout.addView(dots[dotIndex], layoutParams);
        }
    }

    public void onChangeLanguage(View view) {
        String language = Paper.book().read("language");
        if (language.equals("en")) {
            language = "de";
        } else {
            language = "en";
        }

        Paper.book().write("language", language);

        updateView(language);
    }
}
