

package mychef.mychef.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mychef.mychef.R;


public class SignUpActivity extends Activity implements View.OnClickListener {

    private EditText usernameField;
    private EditText passwordField;
    private EditText reEnterPasswordField;
    private EditText emailField;
    private EditText nameField;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpButton) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (username.length() > 6 && password.length() > 6) {
                // Do some networking shot that creates a username

                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            } else {
                if (username.length() <= 6) {
                    // Warn user about a username too short
                }
                if (password.length() <= 6) {
                    //Warn user about too short of a password
                }
            }
        } else {
            Toast.makeText(this, "Button Error", Toast.LENGTH_LONG);
        }
    }
}
