package com.example.myapplication_uni_bielefeld;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Adapter extends PagerAdapter {
    private List<PagerItemModel> pagerItemModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private OnButtonClickedListener listener;

    public Adapter(List<PagerItemModel> pagerItemModels, Context context) {
        this.pagerItemModels = pagerItemModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pagerItemModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView;
        TextView title, desc;
        Button button;
        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        button = view.findViewById(R.id.button);

        imageView.setImageResource(pagerItemModels.get(position).getImage());
        desc.setText(pagerItemModels.get(position).getDesc());
        title.setText(pagerItemModels.get(position).getTitle());
        button.setText(pagerItemModels.get(position).getButton());
        container.addView(view, 0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    return;
                }

                listener.onButtonClicked(position);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public void setOnButtonClickListener(OnButtonClickedListener listener) {
        this.listener = listener;
    }

    public interface OnButtonClickedListener {
        void onButtonClicked(int position);
    }
}
