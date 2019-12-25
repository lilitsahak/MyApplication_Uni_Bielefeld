package com.bielefeld.uni_bielefeld.adapter.viewholder;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bielefeld.uni_bielefeld.R;
import com.bielefeld.uni_bielefeld.model.Topic;

import java.text.SimpleDateFormat;

public class TopicViewHolder extends RecyclerView.ViewHolder {

    private OnItemClickListener onItemClickListener;

    private TextView topicTitleTextView;
    private TextView postedByTextView;

    private SimpleDateFormat dateFormat;

    public TopicViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);

        this.onItemClickListener = onItemClickListener;

        dateFormat = new SimpleDateFormat("dd/mm/yyyy");

        topicTitleTextView = itemView.findViewById(R.id.title);
        postedByTextView = itemView.findViewById(R.id.askedByOn);
    }

    public void setup(Topic topic, Resources resources) {
        topicTitleTextView.setText(topic.title);
        postedByTextView.setText(
                resources.getString(
                        R.string.asked_by_on,
                        topic.askedBy,
                        dateFormat.format(topic.postedOn)));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(getAdapterPosition());
            }
        });
    }
}
