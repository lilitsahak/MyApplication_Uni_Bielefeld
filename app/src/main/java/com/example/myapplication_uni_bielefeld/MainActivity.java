package com.example.myapplication_uni_bielefeld;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.myapplication_uni_bielefeld.Helper.LocalHelper;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    TextView textview;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalHelper.onAttach(newBase,"en"));
    }

    private ViewPager viewPager;
    private Adapter adapter;
    private List<PagerItemModel> pagerItemModels;
    private Integer[] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private Class[] activitiesToOpen = new Class[] { Main2Activity.class, Main3Activity.class,
            Main4Activity.class, Main5Activity.class, Main6Activity.class, Main7Activity.class, Main8Activity.class, Main9Activity.class};
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textView);
        Paper.init(this);
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language", "en");
        updateView((String) Paper.book().read("language"));



        pagerItemModels = new ArrayList<>();

        pagerItemModels.add(new PagerItemModel(R.drawable.pic1, "I love nature", "I love nature very much", "Sabrina" ));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic2, "I love nature", "I love nature very much", "Mama"));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic3, "I love nature", "I love nature very much", "Papa"));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic4, "I love nature", "I love nature very much", "Edita"));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic5, "I love nature", "I love nature very much", "Artash"));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic6, "I love nature", "I love nature very much", "Lilit"));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic7, "I love nature", "I love nature very much", "Robin"));
        pagerItemModels.add(new PagerItemModel(R.drawable.pic8, "I love nature", "I love nature very much", "Nik"));
        adapter = new Adapter(pagerItemModels, this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);





        viewPager.setPadding(130, 0, 130, 0);
        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
        };
        colors = colors_temp;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

               @Override
            public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels) {
                   if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                       viewPager.setBackgroundColor(
                               (Integer) argbEvaluator.evaluate(
                                       positionOffset,
                                       colors[position],
                                       colors[position + 1]
                               )
                       );
                   } else {
                       viewPager.setBackgroundColor(colors[colors.length - 1]);
                   }
               }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });

        adapter.setOnButtonClickListener(new Adapter.OnButtonClickedListener() {
            @Override
            public void onButtonClicked(int position) {
                Intent intent = new Intent(MainActivity.this, activitiesToOpen[position]);
                startActivity(intent);
            }
        });
        bt=(Button)findViewById(R.id.button2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body here";
                String shareSub = "Your Subject here";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        } );

    }

private void updateView(String lang) {
        Context context = LocalHelper.setLocale(this,lang);
        Resources resources =  context.getResources();
        textview.setText(resources.getString(R.string.Bielefeld_University));
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.language_en)
        {
            Paper.book().write("language","en");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId() == R.id.language_de)
        {
            Paper.book().write("language","de");
            updateView((String)Paper.book().read("language"));
        }
        return true;
    }
}
