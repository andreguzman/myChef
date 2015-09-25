package mychef.mychef.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.regions.Regions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mychef.mychef.R;


public class SignInActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext;
    private Button signInButton;
    private EditText usernameField;
    private EditText passwordField;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private  CognitoCachingCredentialsProvider credentialsProvider;
    private CognitoSyncManager client;
    private Dataset userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_sign_in);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext.getApplicationContext(),
                mContext.getString(R.string.identity_pool_id), // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Map<String, String> logins = new HashMap<String, String>();
                logins.put("graph.facebook.com", AccessToken.getCurrentAccessToken().getToken());
                credentialsProvider.setLogins(logins);

                client = new CognitoSyncManager(
                        mContext.getApplicationContext(),
                        Regions.US_EAST_1,
                        credentialsProvider);

                userPreferences = client.openOrCreateDataset("userPref");
                String chefOrEater = userPreferences.get("chefOrEater");

                if (chefOrEater == null || !chefOrEater.isEmpty()) {
                    Intent intent = new Intent(mContext, ChefOrEaterActivity.class);
                    userPreferences.put("chefOrEater", "chef");
                    userPreferences.synchronize(new Dataset.SyncCallback() {
                        @Override
                        public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                            Log.d("mychef.mychef", "sync success");
                        }

                        @Override
                        public boolean onConflict(Dataset dataset, List<SyncConflict> conflicts) {
                            return false;
                        }

                        @Override
                        public boolean onDatasetDeleted(Dataset dataset, String datasetName) {
                            return false;
                        }

                        @Override
                        public boolean onDatasetsMerged(Dataset dataset, List<String> datasetNames) {
                            return false;
                        }

                        @Override
                        public void onFailure(DataStorageException dse) {

                        }
                    });
                    mContext.startActivity(intent);
                }

                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInButton) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                // Do some networking shit that logs the user in
                // Show errors when there's a sign in error
            } else  {
                if (username.isEmpty()) {
                    // Show an error about empty username
                }
                if (password.isEmpty()) {
                    // Show an error about empty password
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        } else {
            Toast.makeText(this, "Button Error", Toast.LENGTH_LONG).show();
        }
    }
}

/*
From Amazon generated code:
// Initialize the Cognito Sync client
CognitoSyncManager syncClient = new CognitoSyncManager(
   getApplicationContext(),
   Regions.US_EAST_1, // Region
   credentialsProvider);

// Create a record in a dataset and synchronize with the server
Dataset dataset = syncClient.openOrCreateDataset("myDataset");
dataset.put("myKey", "myValue");
dataset.synchronize(new DefaultSyncCallback() {
    @Override
    public void onSuccess(Dataset dataset, List newRecords) {
        //Your handler code here
    }
});
 */
