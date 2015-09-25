package mychef.mychef.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mychef.mychef.R;


public class HomeActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        View signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInButton) {
            Intent intent = new Intent(this, SignInActivity.class);
            this.startActivity(intent);
        } else if (v.getId() == R.id.signUpButton) {
            Intent intent = new Intent(this, SignUpActivity.class);
            this.startActivity(intent);
        }
    }
}
