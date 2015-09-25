package mychef.mychef.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.victorsima.uber.UberClient;
import com.victorsima.uber.model.Prices;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import mychef.mychef.R;
import mychef.mychef.backend.Chef;
import mychef.mychef.backend.Dish;
import mychef.mychef.backend.PictureText;
import mychef.mychef.backend.PictureTextAdapter;
import mychef.mychef.backend.PlaidAccess;
import mychef.mychef.backend.PlaidCallback;
import mychef.mychef.backend.User;
import retrofit.RestAdapter;

public class MainActivity extends ActionBarActivity implements View.OnClickListener,
        DrawerLayout.DrawerListener, AdapterView.OnItemClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        PlaidCallback {

    private DrawerLayout mDrawerLayout;

    // Views in navigation drawer
    private ImageButton notificationsButton;
    private ImageButton messagesButton;
    private TextView settingsText;
    private TextView chefText;
    private TextView eaterText;
    private TextView userNameText;
    private ListView listingPickupsList;
    private ImageView profilePictureImage;

    // Views in custom action bar
    private ImageButton switchButton;

    // State variables
    private MainActivityState activityState;
    private NavigationDrawerState drawerState;

    PictureText findFoodPictureText;
    PictureText paymentInfoPictureText;
    PictureText[] eaterArray;
    PictureText[] chefArray;

    public static User user;
    private boolean plaidCalled;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private ArrayList<Dish> dishList;
    private UberClient uberClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User(R.drawable.ayush, "Ayush", "Goyal", 39.9015160, -75.1712720);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainActivityTopLevelContainer);
        mDrawerLayout.setDrawerListener(this);

        notificationsButton = (ImageButton) findViewById(R.id.openNotificationsButton);
        notificationsButton.setOnClickListener(this);

        messagesButton = (ImageButton) findViewById(R.id.openMessagesButton);
        messagesButton.setOnClickListener(this);

        settingsText = (TextView) findViewById(R.id.navigationDrawerSettingsText);
        settingsText.setOnClickListener(this);

        chefText = (TextView) findViewById(R.id.navigationDrawerChefSelectionText);
        chefText.setOnClickListener(this);

        eaterText = (TextView) findViewById(R.id.navigationDrawerEaterSelectionText);
        eaterText.setOnClickListener(this);

        userNameText = (TextView) findViewById(R.id.profileNameText);
        userNameText.setText(user.getFirstName() + " " + user.getLastName().charAt(0) + ".");

        profilePictureImage = (ImageView) findViewById(R.id.profilePictureImage);
        profilePictureImage.setImageDrawable(getResources().getDrawable(user.getIcon()));

        listingPickupsList = (ListView) findViewById(R.id.navigationDrawerMenuList);
        listingPickupsList.setOnItemClickListener(this);

        findFoodPictureText = new PictureText(R.drawable.search_icon,
                getResources().getString(R.string.find_food_label_text));
        paymentInfoPictureText = new PictureText(R.drawable.dollar_sign,
                "Payment Info");
        eaterArray = new PictureText[4];
        chefArray = new PictureText[3];

        eaterArray[0] = chefArray[0] = findFoodPictureText;
        eaterArray[2] = chefArray[2] = paymentInfoPictureText;
        eaterArray[1] = new PictureText(R.drawable.my_pick_ups,
                getResources().getString(R.string.my_pickups_label_text));
        chefArray[1] = new PictureText(R.drawable.my_listings,
                getResources().getString(R.string.my_listings_label_text));
        eaterArray[3] = new PictureText(R.drawable.question_mark, "My Suggestions");

        uberClient = new UberClient("aW8920hQ10VVsqScmC6LlU4oNrk7b-6e",
                "R-YJ9gNGgvOQXhPHtd7Z8JwrCQ2BG_7jYN1el0y0", null,
                "ak1RevgxHKMEtgfbSFDJpOCVddAtvOZh7B9IWK37", null, false,
                RestAdapter.LogLevel.FULL);


        buildGoogleApiClient();
        setUpDishList();
        setUpEaterSelection();
        setUpSearchBarActionBar();
        setUpFoodList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (user.getCreditCardPassword() != null && user.getCreditCardUsername() != null
                && !plaidCalled) {
            try {
                new PlaidAccess().getCategories(user.getCreditCardUsername(), user.getCreditCardPassword(),
                        dishList, this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            plaidCalled = true;
        }
    }

    private void setUpDishList() {
        dishList = new ArrayList<>(10);

        Chef chefOne = new Chef(R.drawable.andre, "Andre", "Guzman", "A super cool guy",
                39.9155296, -75.1681566);
        Chef chef2 = new Chef(R.drawable.person1, "Ayush", "Guzman", "A super cool guy",
                39.9103288, -75.1807737);
        Chef chef3 = new Chef(R.drawable.person2, "Andre", "Goyal", "A super cool guy",
                39.9114480, -75.1881552);
        Chef chef4 = new Chef(R.drawable.person3, "Elan", "Guzman", "A super cool guy",
                39.9089133, -75.1801300);
        Chef chef5 = new Chef(R.drawable.person4, "Nicole", "Guzman", "A super cool guy",
                39.9115796, -75.1767826);
        Chef chef6 = new Chef(R.drawable.person5, "Pablo", "Guzman", "A super cool guy",
                39.9101313, -75.1711607);

        dishList.add(new Dish(R.drawable.coffee_muffins_reduced, "Coffee and Muffins",
                "Delicious Coffee and Muffins", 3.50, chefOne, "Coffee Shop"));
        dishList.add(new Dish(R.drawable.crepe, "Crepes", "Authentic French Crepes",
                4.20, 7.42, chefOne, ""));
        dishList.add(new Dish(R.drawable.pizza, "Pizza", "", 9.00, chef2, "Pizza"));
        dishList.add(new Dish(R.drawable.lasagna, "Lasagna", "", 12.25, chef3, "Italian"));
        dishList.add(new Dish(R.drawable.paella, "Paella", "", 10.60, chef4,"Spanish"));
        dishList.add(new Dish(R.drawable.paneer, "Paneer", "", 8.20, chef5, "Indian"));
        dishList.add(new Dish(R.drawable.dosa, "Dosa", "", 6.00, chef6, "Indian"));
        dishList.add(new Dish(R.drawable.pad_thai, "Pad Thai", "", 7.50, chef3, "Thai"));
        dishList.add(new Dish(R.drawable.dumplings, "Dumplings", "", 4.20, chef5, "Chinese"));

        getFoodDistances();

        Collections.sort(dishList, new DishByDistance());

        new RetrievePriceInfo().execute();
    }

    private void getFoodDistances() {
        for(Dish d : dishList) {
            d.setDistance(calculateLatLongDistance(user.getLatitude(), user.getLongitude(),
                    d.getChef().getLatitude(), d.getChef().getLongitude()));
        }
    }

    public static double calculateLatLongDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            user.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.i("testy", "" + user.getLatitude() + ", " + user.getLongitude());
            Toast.makeText(this, "Retrieved Location", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpEaterSelection() {
        listingPickupsList.setAdapter(new PictureTextAdapter(this,
                new ArrayList<PictureText>(Arrays.asList(eaterArray))));
        drawerState = NavigationDrawerState.EATER;
    }

    private void setUpChefSelection() {
        listingPickupsList.setAdapter(new PictureTextAdapter(this,
                new ArrayList<PictureText>(Arrays.asList(chefArray))));
        drawerState = NavigationDrawerState.CHEF;
    }

    private void setUpSearchBarActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.search_bar_layout, null);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        switchButton = (ImageButton) findViewById(R.id.searchBarSwitchButton);
        switchButton.setOnClickListener(this);
        findViewById(R.id.searchBarOpenNavigationDrawer).setOnClickListener(this);
    }

    private void setUpNavigationDrawerSearchBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.navigation_drawer_open_layout, null);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        findViewById(R.id.closeNavigationDrawerButton).setOnClickListener(this);
    }

    private void setUpFoodMap() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FoodMapFragment foodMapFragment = new FoodMapFragment();

        Bundle extras = new Bundle();
        extras.putParcelableArrayList("dishList", dishList);
        extras.putDouble("lat", user.getLatitude());
        extras.putDouble("long", user.getLongitude());
        foodMapFragment.setArguments(extras);

        fragmentTransaction.replace(R.id.mainActivityMainContentContainer, foodMapFragment);
        fragmentTransaction.commit();
        activityState = MainActivityState.MAP;
    }

    private void setUpFoodList() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FoodListFragment foodListFragment = new FoodListFragment();

        Bundle extras = new Bundle();
        extras.putParcelableArrayList("dishList", dishList);
        foodListFragment.setArguments(extras);

        fragmentTransaction.replace(R.id.mainActivityMainContentContainer, foodListFragment);
        fragmentTransaction.commit();
        activityState = MainActivityState.LIST;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchBarSwitchButton) {
            if (activityState == MainActivityState.LIST) {
                setUpFoodMap();
            } else if (activityState == MainActivityState.MAP) {
                setUpFoodList();
            }
        } else if (v.getId() == R.id.searchBarOpenNavigationDrawer) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        } else if (v.getId() == R.id.closeNavigationDrawerButton) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (v.getId() == R.id.openMessagesButton) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            this.startActivity(intent);
        } else if (v.getId() == R.id.openNotificationsButton) {
            Intent intent = new Intent(this, MessagesActivity.class);
            this.startActivity(intent);
        } else if (v.getId() == R.id.navigationDrawerSettingsText) {
            // Create and start a settings page
            Toast.makeText(this, "Open Settings", Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.navigationDrawerChefSelectionText) {
            if (drawerState == NavigationDrawerState.EATER) {
                setUpChefSelection();
            }
        } else if(v.getId() == R.id.navigationDrawerEaterSelectionText) {
            if (drawerState == NavigationDrawerState.CHEF) {
                setUpEaterSelection();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            mDrawerLayout.closeDrawers();
        }
        if (position == 1) {
            if (drawerState == NavigationDrawerState.CHEF) {
                Intent intent = new Intent(this, MyListingsActivity.class);
                this.startActivity(intent);
            } else if (drawerState == NavigationDrawerState.EATER) {
                Intent intent = new Intent(this, MyPickupsActivity.class);
                this.startActivity(intent);
            }
        } else if (position == 2) {
            Intent intent = new Intent(this, PaymentActivity.class);
            this.startActivity(intent);
        } else if (position == 3) {
            if (user.getCreditCardUsername() == null || user.getCreditCardPassword() == null) {
                Intent intent = new Intent(this, ActivateSuggestionsActivity.class);
                startActivity(intent);
            } else {
                Collections.sort(dishList, new DishByPriority());
                setUpFoodList();
            }
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {}

    @Override
    public void onDrawerOpened(View drawerView) {
        setUpNavigationDrawerSearchBar();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
       setUpSearchBarActionBar();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult i) {
        Toast.makeText(this, "Location Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDrawerStateChanged(int newState) {}

    @Override
    public void plaidCallback() {
        Collections.sort(dishList, new DishByPriority());
        setUpFoodList();
    }

    private enum MainActivityState {LIST, MAP}

    private enum NavigationDrawerState {CHEF, EATER}

    private class DishByDistance implements Comparator<Dish> {

        @Override
        public int compare(Dish lhs, Dish rhs) {
            return (int) ((lhs.getDistance() - rhs.getDistance())*100);
        }
    }

    private class DishByPriority implements Comparator<Dish> {

        @Override
        public int compare(Dish lhs, Dish rhs) {
            if (lhs.getPriority() == rhs.getPriority()) {
                return (int) ((lhs.getDistance() - rhs.getDistance())*100);
            }

            return rhs.getPriority() - lhs.getPriority();
        }
    }

    private class RetrievePriceInfo extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        protected Void doInBackground(Void... urls) {
            try {
                for(Dish d : dishList) {
                    Prices prices = uberClient.getApiService().getPriceEstimates(user.getLatitude(),
                            user.getLongitude(), d.getChef().getLatitude(), d.getChef().getLongitude());
                    d.setUberPrice((prices.getPrices().get(0).getHighEstimate()
                            + prices.getPrices().get(0).getLowEstimate()) / 2);
                    Log.i("testy", "" + d.getUberPrice());
                }
            } catch (Exception e) {
                this.exception = e;
            }

            return null;
        }
    }
}

/*
        categories.put("afghan", 0);
        categories.put("african", 0);
        categories.put("asian", 0);
        categories.put("american", 0);
        categories.put("australian", 0);
        categories.put("bagel shop", 0);
        categories.put("bakery", 0);
        categories.put("barbecue", 0);
        categories.put("brazilian", 0);
        categories.put("breakfast spot", 0);
        categories.put("burgers", 0);
        categories.put("cafe", 0);
        categories.put("cajun", 0);
        categories.put("caribbean", 0);
        categories.put("chinese", 0);
        categories.put("coffee shop", 0);
        categories.put("cuban", 0);
        categories.put("cupcake shop", 0);
        categories.put("delis", 0);
        categories.put("dessert", 0);
        categories.put("distillery", 0);
        categories.put("donuts", 0);
        categories.put("eastern european", 0);
        categories.put("ethiopian", 0);
        categories.put("winery", 0);
        categories.put("Vegan and Vegetarian", 0);
        categories.put("Turkish", 0);
        categories.put("Thai", 0);
        categories.put("Swiss", 0);
        categories.put("Sushi", 0);
        categories.put("Steakhouses", 0);
        categories.put("Spanish", 0);
        categories.put("Seafood", 0);
        categories.put("Scandinavian", 0);
        categories.put("Portuguese", 0);
        categories.put("Pizza", 0);
        categories.put("Moroccan", 0);
        categories.put("Middle Eastern", 0);
        categories.put("Mexican", 0);
        categories.put("Mediterranean", 0);
        categories.put("Latin American", 0);
        categories.put("Korean", 0);
        categories.put("Juice Bar", 0);
        categories.put("Japanese", 0);
        categories.put("Italian", 0);
        categories.put("Indonesian", 0);
        categories.put("Indian", 0);
        categories.put("Ice Cream", 0);
        categories.put("Greek", 0);
        categories.put("German", 0);
        categories.put("Gastropub", 0);
        categories.put("French", 0);
        categories.put("Food Truck", 0);
        categories.put("Fish and Chips", 0);
        categories.put("Filipino", 0);
        categories.put("Fast Food", 0);
        categories.put("Falafel", 0);
        */
