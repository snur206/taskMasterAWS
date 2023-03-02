package com.snur206.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


import java.io.FileNotFoundException;
import java.io.InputStream;
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
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        activityResultLauncher = getImagePickingActivityResultLauncher();
        taskStateSpinner = findViewById(R.id.taskStateSpinner);
        teamSpinner = findViewById(R.id.AddTaskActivityTeamSpinner);
        teamNames = new ArrayList<>();
        team = new ArrayList<>();


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
        setUpAddImgBtn();

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

    public void saveTask(){
        String selectedTeamStringName = taskStateSpinner.getSelectedItem().toString();
        try {
            team = (ArrayList<Team>) teamFuture.get();
        } catch (InterruptedException | ExecutionException ie) {
            ie.printStackTrace();
        }

        Team selectedTeam = team.stream().filter(team -> team.getName().equals(selectedTeamStringName)).findAny().orElseThrow(RuntimeException::new);


    }

    public void setUpAddImgBtn() {
        // on click listener -> launch the img picking intent
        findViewById(R.id.AddTaskActivityAddImgPicker).setOnClickListener(v -> {
            launchImageSelectionIntent();
        });
    }

    public void launchImageSelectionIntent(){
        // OnActivityResult
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Uri pickedImageFileUri = result.getData().getData();
                        try {
                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                            String pickedImageFileName = getFileNameFromUri(pickedImageFileUri);
                            Log.i(TAG, "Successfully got the image: " + pickedImageFileName);
                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFileName, pickedImageFileUri);
                        } catch (FileNotFoundException fnfe) {
                            Log.e(TAG, "Could not get file from picker! " + fnfe);

                        }
                    }
                }
        );
        return imagePickingActivityResultLauncher;
    }

    public void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFileName, Uri pickedImageFileUri){
        // upload to S3
        Amplify.Storage.uploadInputStream(
                pickedImageFileName,
                pickedImageInputStream,
                success -> {
                    Log.i(TAG, "SUCCESS! Uploaded file to S3! Filename is: " + success.getKey());
                    s3ImageKey = pickedImageFileName;
                    ImageView taskImageView = findViewById(R.id.AddTaskActivityAddImgPicker);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure -> Log.e(TAG, "FAILED to upload file to S3 with filename: " + pickedImageFileName + " with error: " + failure)

        );
    }


}