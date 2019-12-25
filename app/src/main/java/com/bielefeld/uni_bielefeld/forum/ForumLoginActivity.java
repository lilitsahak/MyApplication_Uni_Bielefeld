package com.bielefeld.uni_bielefeld.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.bielefeld.uni_bielefeld.MainActivity;
import com.bielefeld.uni_bielefeld.R;
import com.bielefeld.uni_bielefeld.helper.DialogHelper;
import com.bielefeld.uni_bielefeld.helper.LocalHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ForumLoginActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_login);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String language = Paper.book().read("language");
        updateView(language);
    }

    public void onLogin(View view) {
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (username.length() < 3) {

            return;
        }
        if (password.length() < 5) {
            return;
        }

        // Check if user with such username exists
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            DialogHelper.showDialog(
                                    R.string.error,
                                    R.string.connection_error,
                                    ForumLoginActivity.this);
                            return;
                        }

                        // There was no user with this username
                        if (task.getResult().getDocuments().isEmpty()) {
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("username", username);
                            userMap.put("password", password);

                            // Create user with the username and password
                            db.collection("user")
                                    .add(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Save the username and proceed
                                            Paper.book().
                                                    write("username", username);
                                            startActivity(new Intent(
                                                    ForumLoginActivity.this,
                                                    TopicListActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            DialogHelper.showDialog(
                                                    R.string.error,
                                                    R.string.connection_error,
                                                    ForumLoginActivity.this);
                                        }
                                    });
                        } else {
                            // There was such user, check if passwords match
                            DocumentSnapshot document = task
                                    .getResult()
                                    .getDocuments()
                                    .get(0);

                            if (document.get("password").equals(password)) {
                                // Successful login, save the username and proceed
                                Paper.book().
                                        write("username", username);
                                startActivity(new Intent(
                                        ForumLoginActivity.this,
                                        TopicListActivity.class));
                            } else {
                                // Passwords did not match
                                DialogHelper.showDialog(
                                        R.string.error,
                                        R.string.credential_mismathc,
                                        ForumLoginActivity.this);
                            }
                        }
                    }
                });
    }

    private void updateView(String language) {
        Context context = LocalHelper.setLocale(this, language);
        Resources resources = context.getResources();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
