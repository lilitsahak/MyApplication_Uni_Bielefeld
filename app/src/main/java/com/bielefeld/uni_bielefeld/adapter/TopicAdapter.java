package com.bielefeld.uni_bielefeld.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bielefeld.uni_bielefeld.R;
import com.bielefeld.uni_bielefeld.adapter.viewholder.AnswerViewHolder;
import com.bielefeld.uni_bielefeld.adapter.viewholder.OnItemClickListener;
import com.bielefeld.uni_bielefeld.adapter.viewholder.TopicViewHolder;
import com.bielefeld.uni_bielefeld.helper.LocalHelper;
import com.bielefeld.uni_bielefeld.model.Topic;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class TopicAdapter extends RecyclerView.Adapter<TopicViewHolder> implements OnItemClickListener {

    private List<Topic> topics;
    private Context context;
    private Resources resources;
    private OnTopicClickListener onTopicClickListener;

    public TopicAdapter(Context context, OnTopicClickListener onTopicClickListener) {
        String language = Paper.book().read("language");
        Context localeContext = LocalHelper.setLocale(context, language);
        this.context = context;
        this.resources = localeContext.getResources();
        this.topics = new ArrayList<>();
        this.onTopicClickListener = onTopicClickListener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new TopicViewHolder(
                layoutInflater
                        .inflate(R.layout.item_topic,
                                parent, false),
                this);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        holder.setup(topics.get(position), resources);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public void add(Topic topic) {
        topics.add(0, topic);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int position) {
        onTopicClickListener.onTopicClicked(topics.get(position));
    }

    public interface OnTopicClickListener {
        void onTopicClicked(Topic topic);
    }
}
