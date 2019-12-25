package com.bielefeld.uni_bielefeld.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bielefeld.uni_bielefeld.R;
import com.bielefeld.uni_bielefeld.adapter.viewholder.AnswerViewHolder;
import com.bielefeld.uni_bielefeld.adapter.viewholder.TopicViewHolder;
import com.bielefeld.uni_bielefeld.helper.LocalHelper;
import com.bielefeld.uni_bielefeld.model.Answer;
import com.bielefeld.uni_bielefeld.model.Topic;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder> {

    private List<Answer> answers;
    private Context context;
    private Resources resources;

    public AnswerAdapter(Context context) {
        String language = Paper.book().read("language");
        Context localeContext = LocalHelper.setLocale(context, language);
        this.context = context;
        this.resources = localeContext.getResources();
        this.answers = new ArrayList<>();
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new AnswerViewHolder(
                layoutInflater
                        .inflate(R.layout.item_answer,
                                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        holder.setup(answers.get(position), resources);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    public void add(Answer answer) {
        answers.add(answer);
        notifyDataSetChanged();
    }
}
