package mychef.mychef.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mychef.mychef.R;

public class ActivateSuggestionsActivity extends Activity implements View.OnClickListener {

    private Button submitButton;
    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_suggestions);

        submitButton = (Button) findViewById(R.id.submitCreditCardInfoButton);
        submitButton.setOnClickListener(this);

        usernameField = (EditText) findViewById(R.id.creditCardLoginField);
        passwordField = (EditText) findViewById(R.id.creditCardLoginPassword);
    }

    @Override
    public void onClick(View v)  {
        MainActivity.user.setCreditCardCredentials("plaid_test", "plaid_good");
        finish();
    }
}
