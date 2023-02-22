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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModel;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.snur206.taskmaster.R;


import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "AddTaskActivity";
    Spinner taskStateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskStateSpinner = findViewById(R.id.taskStateSpinner);

        setupTypeSpinner();
        setupSaveBtn();

        Button addTaskButton = (Button) findViewById(R.id.addTaskActivityBtn);

    }

    public void setupTypeSpinner() {
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()
        ));
    }

    public void setupSaveBtn(){
        findViewById(R.id.addTaskActivityBtn).setOnClickListener(v -> {
            TaskModel newTask = TaskModel.builder()
                    .name(((EditText)findViewById(R.id.addTaskTitleEdit)).getText().toString())
                    .state((TaskStateEnum) taskStateSpinner.getSelectedItem())
                    .description(((EditText) findViewById(R.id.addTaskActivityTaskDecriptionEdit)).getText().toString())
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