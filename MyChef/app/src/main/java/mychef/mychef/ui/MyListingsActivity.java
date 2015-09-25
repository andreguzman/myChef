package mychef.mychef.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mychef.mychef.R;


public class MyListingsActivity extends Activity implements View.OnClickListener {

    private Button createListingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        TextView currentListingsText = (TextView) findViewById(R.id.myCurrentPickupsListingsLabelText);
        currentListingsText.setText(R.string.my_current_listings_label_text);

        TextView pastListingsText = (TextView) findViewById(R.id.myPastPickupsListingsLabelText);
        pastListingsText.setText(R.string.my_past_listings_label_text);

        createListingButton = (Button) findViewById(R.id.createListingButton);
        createListingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createListingButton) {
            Intent intent = new Intent(this, CreateListingActivity.class);
            startActivity(intent);
        }
    }
}
