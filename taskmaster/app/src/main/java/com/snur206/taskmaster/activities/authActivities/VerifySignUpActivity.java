package com.snur206.taskmaster.activities.authActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.snur206.taskmaster.R;
import com.snur206.taskmaster.adapter.TaskRecyclerViewAdapter;

public class VerifySignUpActivity extends AppCompatActivity {
    public static final String TAG = "verify_sign_up_activity";
    Intent callingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sign_up);
        callingIntent = getIntent();

        setUpConfirmBtn();
    }

    public void setUpConfirmBtn(){
        findViewById(R.id.VerifyActivitySubmitBtn).setOnClickListener(v -> {
            // gather intel
            String userEmail = "";
            if(callingIntent != null){
                userEmail = callingIntent.getStringExtra(SignUpActivity.USER_EMAIL);
            }
            String confirmationCode = ((EditText) findViewById(R.id.VerifyActivityConfirmCode)).getText().toString();
            // make call to cognito
            String finalUserEmail = userEmail;
            Amplify.Auth.confirmSignUp(
                userEmail,
                confirmationCode,
                success -> Log.i(TAG, "Confirmed sign up with user: " + finalUserEmail),
                failure -> Log.e(TAG, "Failed to verify confirmation code :" + confirmationCode + "for user: " + finalUserEmail + "with failure: " + failure)
        );
            Intent goToLoginActivity = new Intent(this, LoginActivity.class);
            goToLoginActivity.putExtra(SignUpActivity.USER_EMAIL, userEmail);
            startActivity(goToLoginActivity);
        });
    }
}