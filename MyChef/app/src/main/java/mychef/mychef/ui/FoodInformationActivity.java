package mychef.mychef.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import mychef.mychef.R;
import mychef.mychef.backend.Dish;


public class FoodInformationActivity extends Activity {

    private Dish dish;
    private TextView foodName;
    private TextView chefName;
    private TextView foodPrice;
    private TextView foodDescription;
    private TextView aboutChef;
    private TextView bottomChefName;
    private TextView uberPriceText;
    private ImageView chefImage;
    private ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        dish = getIntent().getExtras().getParcelable(FoodListFragment.DISH_INFORMATION_EXTRA);

        foodName = (TextView) findViewById(R.id.foodNameText);
        foodName.setText(dish.getName());

        chefName  = (TextView) findViewById(R.id.chefNameText);
        chefName.setText(dish.getChef().getFirstName() + " "
                + dish.getChef().getLastName().charAt(0) + ".");

        foodPrice = (TextView) findViewById(R.id.totalPriceText);
        foodPrice.setText(String.format("%.2f", dish.getPrice()));

        foodDescription = (TextView) findViewById(R.id.foodDescriptionText);
        foodDescription.setText(dish.getDescription());

        aboutChef = (TextView) findViewById(R.id.aboutChefText);
        aboutChef.setText(dish.getChef().getAboutChef());

        bottomChefName = (TextView) findViewById(R.id.chefNameBottomText);
        bottomChefName.setText(dish.getChef().getFirstName() + " "
                + dish.getChef().getLastName().charAt(0) + ".");

        chefImage = (ImageView) findViewById(R.id.chefPictureImage);
        chefImage.setImageDrawable(getResources().getDrawable(dish.getChef().getIcon()));

        foodImage = (ImageView) findViewById(R.id.foodInfoWindowImage);
        foodImage.setImageDrawable(getResources().getDrawable(dish.getIcon()));

        uberPriceText = (TextView) findViewById(R.id.uberPriceText);
        String uberPriceTextString;
        if (dish.getUberPrice() == 0) {
            uberPriceTextString = "Calculating...";
        } else {
            uberPriceTextString = String.format("%.2f", dish.getUberPrice());
        }
        uberPriceText.setText(uberPriceTextString);
    }

}
