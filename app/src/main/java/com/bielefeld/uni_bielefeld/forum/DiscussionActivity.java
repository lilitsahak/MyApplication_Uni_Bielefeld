package com.bielefeld.uni_bielefeld.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bielefeld.uni_bielefeld.R;
import com.bielefeld.uni_bielefeld.adapter.AnswerAdapter;
import com.bielefeld.uni_bielefeld.helper.DialogHelper;
import com.bielefeld.uni_bielefeld.helper.LocalHelper;
import com.bielefeld.uni_bielefeld.model.Answer;
import com.bielefeld.uni_bielefeld.model.Topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class DiscussionActivity extends AppCompatActivity {

    private Topic topic;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AnswerAdapter adapter;
    private TextView noAnswersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        noAnswersTextView = findViewById(R.id.no_answers);

        topic = (Topic) getIntent().getSerializableExtra("topic");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new AnswerAdapter(this);

        recyclerView = findViewById(R.id.discussion_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refresh();

        updateView((String) Paper.book().read("language"));
    }

    public void onAddAnswer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_topic);
        final EditText input = (EditText) getLayoutInflater().inflate(R.layout.dialog_topic_input, null);
        builder.setView(input);
        builder.setPositiveButton(R.string.add_topic, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String text = input.getText().toString();

                if (text.length() > 500) {
                    DialogHelper.showDialog(
                            R.string.error,
                            R.string.answer_too_long,
                            DiscussionActivity.this);
                    return;
                }

                final String username = Paper.book()
                        .read("username",  "");

                final Date postedDate = new Date();

                Map<String, Object> answerMap = new HashMap<>();
                answerMap.put("text", text);
                answerMap.put("postedBy", username);
                answerMap.put("postedOn", postedDate);
                answerMap.put("topicId", topic.id);

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("answer")
                        .add(answerMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (!task.isSuccessful()) {
                                    DialogHelper.showDialog(
                                            R.string.error,
                                            R.string.connection_error,
                                            DiscussionActivity.this);
                                }

                                Answer answer = new Answer();
                                answer.id = task.getResult().getId();
                                answer.text = text;
                                answer.postedBy = username;
                                answer.postedOn = postedDate;
                                answer.topicId = topic.id;
                                adapter.add(answer);

                                noAnswersTextView.setVisibility(View.GONE);
                            }
                        });
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void refresh() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("answer")
                .whereEqualTo("topicId", topic.id)
                .orderBy("postedOn", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isComplete()) {
                            return;
                        }

                        ArrayList<Answer> answers = new ArrayList<>();

                        noAnswersTextView.setVisibility(task.getResult().getDocuments().isEmpty() ? View.VISIBLE : View.GONE);

                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            Answer answer = new Answer();
                            answer.id = document.getId();
                            answer.text = document.getString("text");
                            answer.postedBy = document.getString("postedBy");
                            answer.postedOn = document.getDate("postedOn");
                            answers.add(answer);
                        }

                        adapter.setAnswers(answers);
                    }
                });
    }

    private void updateView(String language) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        getSupportActionBar().setTitle(resources.getString(R.string.forum));

        Button addAnswerButton = findViewById(R.id.add_answer);
        addAnswerButton.setText(resources.getString(R.string.add_answer));

        TextView topicTitleTextView = findViewById(R.id.title);
        TextView topicPostedOnByTextView = findViewById(R.id.askedByOn);

        topicTitleTextView.setText(topic.title);
        topicPostedOnByTextView.setText(
                resources.getString(
                    R.string.asked_by_on,
                    topic.askedBy,
                    dateFormat.format(topic.postedOn)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            adapter.setAnswers(new ArrayList<Answer>());
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
