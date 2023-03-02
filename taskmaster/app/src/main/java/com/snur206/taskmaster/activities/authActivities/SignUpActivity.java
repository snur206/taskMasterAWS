package com.snur206.taskmaster.activities.authActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.snur206.taskmaster.R;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "sign_up_activity";
    public static final String USER_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpSignUpButton();
    }

    public void setUpSignUpButton(){
        findViewById(R.id.SignUpActivityBtn).setOnClickListener(view -> {
            // gather intel
            String userEmail = ((EditText)findViewById(R.id.SignUpActivityEmail)).getText().toString();
            String userPassword = ((EditText)findViewById(R.id.SignUpActivityPassword)).getText().toString();
            // make call to cognito
            Amplify.Auth.signUp(
                userEmail,
                userPassword,
                AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), userEmail)
                        .userAttribute(AuthUserAttributeKey.nickname(), "snur206")
                        .build(),
                success -> Log.i(TAG, "Successful Sign Up!"),
                failure -> Log.e(TAG, "Sign up fail with email:" + userEmail + failure)
                );

            // redirect to Confirm activity
            Intent goToVerifySignUpActivity = new Intent(this, VerifySignUpActivity.class);
            goToVerifySignUpActivity.putExtra(USER_EMAIL, userEmail);
            startActivity(goToVerifySignUpActivity);
        });

    }
}

