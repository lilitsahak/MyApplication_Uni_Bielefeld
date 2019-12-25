package com.bielefeld.uni_bielefeld.adapter.viewholder;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bielefeld.uni_bielefeld.R;
import com.bielefeld.uni_bielefeld.model.Answer;
import com.bielefeld.uni_bielefeld.model.Topic;

import java.text.SimpleDateFormat;

public class AnswerViewHolder extends RecyclerView.ViewHolder {

    private TextView topicTitleTextView;
    private TextView postedByTextView;

    private SimpleDateFormat dateFormat;

    public AnswerViewHolder(@NonNull View itemView) {
        super(itemView);

        dateFormat = new SimpleDateFormat("hh:mm dd/mm/yyyy");

        topicTitleTextView = itemView.findViewById(R.id.title);
        postedByTextView = itemView.findViewById(R.id.askedByOn);
    }

    public void setup(Answer answer, Resources resources) {
        topicTitleTextView.setText(answer.text);
        postedByTextView.setText(
                resources.getString(
                        R.string.asked_by_on,
                        answer.postedBy,
                        dateFormat.format(answer.postedOn)));
    }
}
