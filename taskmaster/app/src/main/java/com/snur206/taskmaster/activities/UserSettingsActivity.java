package com.snur206.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.snur206.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserSettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public static final String USER_NAME_TAG = "userName";
    public static final String Select_Team_TAG = "selectTeam";
    public static final String TAG = "settingsActivity";
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();
    Spinner selectTeamSpinner;
    ArrayList<String> teamNames;
    ArrayList<Team> team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        selectTeamSpinner = findViewById(R.id.UserSettingsActivitySelectTeam);
        teamNames = new ArrayList<>();
        team = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Team read successfully!");
                    for (Team databaseTeam : success.getData()) {
                        teamNames.add(databaseTeam.getName());
                        team.add(databaseTeam);
                    }
                    teamFuture.complete(team);
                    runOnUiThread(this::setupTaskSpinners);
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.e(TAG, "FAILED to read team!" + failure);
                }
        );

        Intent callingIntent = getIntent();
        String userInputString = null;
        if (callingIntent != null) {
            userInputString = callingIntent.getStringExtra(MainActivity.TASK_INPUT_EXTRA_TAG);
        }

        TextView userInputTextView = (TextView) findViewById(R.id.userSettingsActivityInputTextView);
        if (userInputString != null) {
            userInputTextView.setText(userInputString);
        } else {
            userInputTextView.setText(R.string.no_input);
        }


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userName = preferences.getString(USER_NAME_TAG, "");
        EditText userNameEditText = (EditText) findViewById(R.id.userSettingsActivityUsernameEditTextView);
        userNameEditText.setText(userName);
        setupSaveBtn();
    }

    public void setupTaskSpinners() {
        selectTeamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }

    public void setupSaveBtn(){
        Button saveButton = findViewById(R.id.userSettingsActivitySaveBtn);
        EditText userNameEditText = (EditText) findViewById(R.id.userSettingsActivityUsernameEditTextView);
        saveButton.setOnClickListener(v -> {

            String userNameString = userNameEditText.getText().toString();
            String selectTeamName = selectTeamSpinner.getSelectedItem().toString();
            SharedPreferences.Editor preferencesEditor = preferences.edit();

            preferencesEditor.putString(USER_NAME_TAG, userNameString);
            preferencesEditor.putString(Select_Team_TAG, selectTeamName);
            preferencesEditor.apply();

            Toast.makeText(UserSettingsActivity.this, "Settings saved!", Toast.LENGTH_SHORT).show();
        });
    }
}