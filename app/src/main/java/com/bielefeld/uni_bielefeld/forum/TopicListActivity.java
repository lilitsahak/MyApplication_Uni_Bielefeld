package com.bielefeld.uni_bielefeld.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bielefeld.uni_bielefeld.adapter.TopicAdapter;
import com.bielefeld.uni_bielefeld.helper.DialogHelper;
import com.bielefeld.uni_bielefeld.helper.LocalHelper;
import com.bielefeld.uni_bielefeld.model.Topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class TopicListActivity extends AppCompatActivity implements TopicAdapter.OnTopicClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private TextView noDiscussionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        noDiscussionsTextView = findViewById(R.id.no_discussions);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new TopicAdapter(this, this);

        recyclerView = findViewById(R.id.topic_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refresh();

        updateView((String) Paper.book().read("language"));
    }

    private void refresh() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("topic")
                .orderBy("postedOn", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isComplete()) {
                            return;
                        }

                        noDiscussionsTextView.setVisibility(task.getResult().getDocuments().isEmpty() ? View.VISIBLE : View.GONE);

                        ArrayList<Topic> topics = new ArrayList<>();

                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            Topic topic = new Topic();
                            topic.id = document.getId();
                            topic.title = document.getString("title");
                            topic.askedBy = document.getString("askedBy");
                            topic.postedOn = document.getDate("postedOn");
                            topics.add(topic);
                        }

                        adapter.setTopics(topics);
                    }
                });
    }

    public void onAddTopic(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_topic);
        final EditText input = (EditText) getLayoutInflater().inflate(R.layout.dialog_topic_input, null);
        builder.setView(input);
        builder.setPositiveButton(R.string.add_topic, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = input.getText().toString();

                if (title.length() > 100) {
                    DialogHelper.showDialog(
                            R.string.error,
                            R.string.title_too_long,
                            TopicListActivity.this);
                    return;
                }

                final String username = Paper.book()
                        .read("username",  "");

                final Date postedDate = new Date();

                Map<String, Object> topicMap = new HashMap<>();
                topicMap.put("title", title);
                topicMap.put("askedBy", username);
                topicMap.put("postedOn", postedDate);

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("topic")
                        .add(topicMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (!task.isSuccessful()) {
                                    DialogHelper.showDialog(
                                            R.string.error,
                                            R.string.connection_error,
                                            TopicListActivity.this);
                                }

                                DialogHelper.showDialog(
                                        R.string.add_topic,
                                        R.string.topic_added,
                                        TopicListActivity.this);

                                Topic topic = new Topic();
                                topic.id = task.getResult().getId();
                                topic.title = title;
                                topic.askedBy = username;
                                topic.postedOn = postedDate;
                                adapter.add(topic);

                                noDiscussionsTextView.setVisibility(View.GONE);
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

    private void updateView(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();

        getSupportActionBar().setTitle(resources.getString(R.string.forum));

        Button addTopicButton = findViewById(R.id.add_topic);
        addTopicButton.setText(resources.getString(R.string.add_topic));
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
            adapter.setTopics(new ArrayList<Topic>());
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

    @Override
    public void onTopicClicked(Topic topic) {
        Intent intent = new Intent(this, DiscussionActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }
}
