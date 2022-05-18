package com.chernybro.loftmoneyjava.presentation.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.activities.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 777;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configureViews();
        configureViewModel();
        configureGoogleAuth();
    }

    private void configureGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                loginViewModel.makeLogin(((LoftApp) getApplication()).authApi, account.getId());
            } else {

                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void configureViews() {
        AppCompatButton loginEnterView = findViewById(R.id.loginEnterView);

        loginEnterView.setOnClickListener(v -> {
//            loginViewModel.makeLogin(((LoftApp) getApplication()).authApi);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void configureViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.messageString.observe(this, error -> {
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        loginViewModel.authToken.observe(this, authToken -> {
            if (!TextUtils.isEmpty(authToken)) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
                sharedPreferences.edit().putString(LoftApp.AUTH_KEY, authToken).apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}