package mychef.mychef.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mychef.mychef.R;


public class MyPickupsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pickups);

        TextView currentPickupsText = (TextView) findViewById(R.id.myCurrentPickupsListingsLabelText);
        currentPickupsText.setText(R.string.my_current_pickups_label_text);

        TextView pastPickupsText = (TextView) findViewById(R.id.myPastPickupsListingsLabelText);
        pastPickupsText.setText(R.string.my_past_pickups_label_text);
    }

}
