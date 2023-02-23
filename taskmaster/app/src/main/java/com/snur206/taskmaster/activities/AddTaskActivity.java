package com.snur206.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModel;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.snur206.taskmaster.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddTaskActivity";
    Spinner taskStateSpinner;
    Spinner teamSpinner;
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();
    ArrayList<String> teamNames;
    ArrayList<Team> team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStateSpinner = findViewById(R.id.taskStateSpinner);
        teamSpinner = findViewById(R.id.AddTaskActivityTeamSpinner);
        teamNames = new ArrayList<>();
        team = new ArrayList<>();

//        Team teamA = Team.builder()
//                .name("A")
//                .build();
//        Team teamB = Team.builder()
//                .name("B")
//                .build();
//        Team teamC = Team.builder()
//                .name("C")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(teamA),
//                success -> Log.i("Success", "Successfully created team!"),
//                failure -> Log.e("Failure", "FAILED" + failure)
//        );

        // Completable Future
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Team read successfully!");
                    for (Team databaseTeam : success.getData()) {
                        teamNames.add(databaseTeam.getName());
                        team.add(databaseTeam);
                    }
                    teamFuture.complete(team);
                    runOnUiThread(this::setupSpinners);
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.e(TAG, "FAILED to read team!" + failure);
                }
        );

        setupSpinners();
        setupSaveBtn();

        Button addTaskButton = (Button) findViewById(R.id.addTaskActivityBtn);

    }

    public void setupSpinners() {
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()
        ));
        teamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));

    }

    public void setupSaveBtn(){
        findViewById(R.id.addTaskActivityBtn).setOnClickListener(v -> {
            String selectedTeamStringName = teamSpinner.getSelectedItem().toString();
            try {
                team = (ArrayList<Team>) teamFuture.get();
            } catch (InterruptedException | ExecutionException ie) {
                ie.printStackTrace();
            }

            Team selectedTeam = team.stream().filter(team -> team.getName().equals(selectedTeamStringName)).findAny().orElseThrow(RuntimeException::new);
            TaskModel newTask = TaskModel.builder()
                    .name(((EditText)findViewById(R.id.addTaskTitleEdit)).getText().toString())
                    .state((TaskStateEnum) taskStateSpinner.getSelectedItem())
                    .description(((EditText) findViewById(R.id.addTaskActivityTaskDecriptionEdit)).getText().toString())
                    .team(selectedTeam)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "Created a task successfully!"),
                    failure -> Log.e(TAG, "FAILED to make task", failure)
            );
//            taskMasterDatabase.taskDao().insertTask(newTaskModel);
            Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
        });
    }
}