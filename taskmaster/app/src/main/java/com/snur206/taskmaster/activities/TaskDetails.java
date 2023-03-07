package com.snur206.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.snur206.taskmaster.R;
import com.snur206.taskmaster.activities.MainActivity;
import com.snur206.taskmaster.adapter.TaskRecyclerViewAdapter;

import java.io.File;

public class TaskDetails extends AppCompatActivity {
    public final static String TAG = "task_details";
    Context callingActivity;
    String taskTitle;
    String taskBody;
    String taskState;
    String taskImageKey = null;
    String taskLocation = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        consumeExtras();

        if(taskImageKey != null && !taskImageKey.isEmpty()){
            Amplify.Storage.downloadFile(
                    taskImageKey,
                    new File(getApplication().getFilesDir(), taskImageKey),
                    success -> {
                        Log.i(TAG, "SUCCESS! Got the image with the key: " + taskImageKey);
                        ImageView taskImgView = findViewById(R.id.taskDetailsActivityImage);
                        taskImgView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> Log.e(TAG, "FAILED! Unable to get image from S3 with the key: " + taskImageKey + "with error: " + failure)

            );
        }
    }

    public void consumeExtras(){
        Intent callingIntent = getIntent();
        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(TaskRecyclerViewAdapter.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(TaskRecyclerViewAdapter.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(TaskRecyclerViewAdapter.TASK_STATE_TAG);
            taskImageKey = callingIntent.getStringExtra(TaskRecyclerViewAdapter.TASK_IMAGE_KEY_TAG);
            taskLocation = callingIntent.getStringExtra(TaskRecyclerViewAdapter.TASK_LOCATION_KEY_TAG);
        }
        ((TextView) findViewById(R.id.TaskDetailsTVTitle)).setText(taskTitle);
        ((TextView) findViewById(R.id.TaskDetailsTVBody)).setText(taskBody);
        ((TextView) findViewById(R.id.TaskDetailsTVState)).setText(taskState);
        ((TextView) findViewById(R.id.taskDetailsActivityLocation)).setText(taskLocation);
    }
}