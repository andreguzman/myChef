package mychef.mychef.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mychef.mychef.R;
import mychef.mychef.backend.Chef;
import mychef.mychef.backend.Dish;
import mychef.mychef.backend.DishAdapter;


public class FoodListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String DISH_INFORMATION_EXTRA = "dishInformation";

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_food_list, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) mView.findViewById(R.id.foodList);
        listView.setOnItemClickListener(this);

        Bundle extras = getArguments();
        ArrayList<Dish> dishList;
        if (extras != null) {
            dishList = extras.getParcelableArrayList("dishList");
            listView.setAdapter(new DishAdapter(mView.getContext(), dishList));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), FoodInformationActivity.class);
        intent.putExtra(DISH_INFORMATION_EXTRA, (Dish) parent.getItemAtPosition(position));
        startActivity(intent);
    }
}
