package com.example.myapplication_uni_bielefeld;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Adapter extends PagerAdapter {
    private List<PagerItemModel> pagerItemModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private Resources resources;
    private OnButtonClickedListener listener;

    public Adapter(List<PagerItemModel> pagerItemModels, Context context) {
        this.pagerItemModels = pagerItemModels;
        this.context = context;
        this.resources = context.getResources();
    }

    public void setContext(Context context, Resources resources) {
        this.context = context;
        this.resources = resources;
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
        TextView titleView;
        imageView = view.findViewById(R.id.image);
        imageView.setImageResource(pagerItemModels.get(position).getImage());
        titleView = view.findViewById(R.id.title);
        titleView.setText(resources.getString(pagerItemModels.get(position).getTitle()));

        container.addView(view, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
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

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface OnButtonClickedListener {
        void onButtonClicked(int position);
    }
}
