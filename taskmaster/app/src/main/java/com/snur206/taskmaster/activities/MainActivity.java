package com.snur206.taskmaster.activities;

import static com.snur206.taskmaster.activities.UserSettingsActivity.USER_NAME_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModel;
import com.snur206.taskmaster.R;
import com.snur206.taskmaster.activities.authActivities.LoginActivity;
import com.snur206.taskmaster.activities.authActivities.SignUpActivity;
import com.snur206.taskmaster.adapter.TaskRecyclerViewAdapter;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "mainActivity";
    public static final String Select_Team_TAG = "selectTeam";

    List<TaskModel> taskModelsList;
    TaskRecyclerViewAdapter adapter;

    //    public final String TAG = "MainActivity";
    public static final String TASK_INPUT_EXTRA_TAG = "userTask";

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button addTaskButton = (Button) findViewById(R.id.mainActivityAddTaskBtn);

            addTaskButton.setOnClickListener(v -> {
                Intent goToAddTaskFromIntent = new Intent(this, AddTaskActivity.class);
                startActivity(goToAddTaskFromIntent);
            });


            Button allTaskButton = (Button)  findViewById(R.id.mainActivityAllTaskBtn);

            allTaskButton.setOnClickListener(v -> {
                Intent goToAllTaskFromIntent = new Intent(this, AllTaskActivity.class);
                startActivity(goToAllTaskFromIntent);
            });

            ImageView settingsButton = (ImageView) findViewById(R.id.mainActivitySettingsImgView);
            settingsButton.setOnClickListener(v -> {
                String userInput = ((EditText)findViewById(R.id.mainActivityUserInputEditText)).getText().toString();
                Intent goToUserSettingsIntent = new Intent(this, UserSettingsActivity.class);
                goToUserSettingsIntent.putExtra(TASK_INPUT_EXTRA_TAG, userInput);
                startActivity(goToUserSettingsIntent);
            });

        setupButtons();
        setUpRecyclerView();


        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
                writer.append("Example file contents");
                writer.close();
            } catch (Exception exception) {
                Log.e("MyAmplifyApp", "Upload failed", exception);
            }

            Amplify.Storage.uploadFile(
                    "ExampleKey",
                    exampleFile,
                    success -> Log.i(TAG, "File uploaded to S3"),
                    failure -> Log.e(TAG,"FAILED to upload file" + failure)
            );


    }

    public void setUpRecyclerView(){
        taskModelsList = new ArrayList<>();
        RecyclerView taskModelRecyclerView = findViewById(R.id.MainActivityRecyclerViewTaskModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskModelRecyclerView.setLayoutManager(layoutManager);
        adapter = new TaskRecyclerViewAdapter(taskModelsList, this);
        taskModelRecyclerView.setAdapter(adapter);


    }

    public void setupButtons() {

        AtomicReference<String> username = new AtomicReference<>("");
        // we need to get access to current auth user
        Amplify.Auth.getCurrentUser(
                success ->  {
                    Log.i(TAG, "Got current user");
                    username.set(success.getUsername());
                },
                failure -> {}
        );

        if (username.toString().equals("")) {
            ((Button) findViewById(R.id.MainActivitySignUpBtn)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.MainActivityLoginBtn)).setVisibility(View.VISIBLE);
        } else {
            ((Button)findViewById(R.id.MainActivitySignUpBtn)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.MainActivityLoginBtn)).setVisibility(View.INVISIBLE);
        }

        // login button
        findViewById(R.id.MainActivityLoginBtn).setOnClickListener(v -> {
            Intent goToLoginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(goToLoginActivityIntent);
        });

        // sign up button
        findViewById(R.id.MainActivitySignUpBtn).setOnClickListener(v -> {
            Intent goToSignUpActivityIntent = new Intent(this, SignUpActivity.class);
            startActivity(goToSignUpActivityIntent);
        });

        // logout button
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTeam = preferences.getString(Select_Team_TAG, "No team chosen");

        Amplify.API.query(
                ModelQuery.list(TaskModel.class),
                success -> {
                    taskModelsList.clear();
                    Log.i(TAG, "Task read successfully!");
                    for (TaskModel databaseTaskModel : success.getData()) {
                        String selectedTeamName = selectedTeam;
                        if(databaseTaskModel.getTeam() != null){
                            if(databaseTaskModel.getTeam().getName().equals(selectedTeamName)) {
                                taskModelsList.add(databaseTaskModel);
                            }
                        }

                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged()); // since this runs asynchronously, the adapter may already have rendered, so we have to tell it to update
                },
                failure -> Log.e(TAG, "FAILED to read task from the Database" + failure)
        );
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = preferences.getString(USER_NAME_TAG, "No Username");
        ((TextView)findViewById(R.id.mainActivityTaskMasterTextView)).setText(username);
        ((TextView)findViewById(R.id.mainActivityTaskMasterTextView)).setText(username);

//        taskModelsList.clear();
//        taskModelsList.addAll(taskMasterDatabase.taskDao().findAll());

    }
}