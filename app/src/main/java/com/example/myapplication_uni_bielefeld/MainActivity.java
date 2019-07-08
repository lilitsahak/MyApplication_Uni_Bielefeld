package com.example.myapplication_uni_bielefeld;

import android.animation.ArgbEvaluator;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.pic1, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic2, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic3, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic4, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic5, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic6, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic7, "I love nature", "I love nature very much"));
        models.add(new Model(R.drawable.pic8, "I love nature", "I love nature very much"));
        adapter = new Adapter(models, this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);
        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
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
    }
}
