package mychef.mychef.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import mychef.mychef.R;
import mychef.mychef.backend.Dish;
import mychef.mychef.backend.DishAdapter;


public class FoodMapFragment extends Fragment {

    View mView;
    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;

    private ArrayList<Dish> dishList;
    private double longitude;
    private double latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_food_map, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            dishList = extras.getParcelableArrayList("dishList");
            longitude = extras.getDouble("long");
            latitude = extras.getDouble("lat");
        }

        MapsInitializer.initialize(getActivity());
        mMapView = (MapView) mView.findViewById(R.id.foodMap);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(mView);

       return mView;
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.foodMap)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        LatLng userLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(userLoc).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc , 14.0f));

        for (Dish d : dishList) {
            LatLng foodLoc = new LatLng(d.getChef().getLatitude(), d.getChef().getLongitude());
            mMap.addMarker(new MarkerOptions().position(foodLoc).title(d.getName()));
        }
    }

}
