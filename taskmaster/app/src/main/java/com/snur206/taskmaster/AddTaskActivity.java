package com.snur206.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = (Button) findViewById(R.id.AddTaskButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                 TextView nameTextView = (TextView) findViewById(R.id.addTaskText);
                 nameTextView.setText("Submitted!");
            }
        });

    }
}