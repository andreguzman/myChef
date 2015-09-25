package mychef.mychef.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mychef.mychef.R;

public class DishAdapter extends ArrayAdapter<Dish> {

    private ArrayList<Dish> dishList;

    public DishAdapter(Context context, List<Dish> objects) {
        super(context, 0, objects);
        dishList = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.food_list_item, null);
        }

        Dish dish = dishList.get(position);

        if (dish != null) {
            ImageView foodImage = (ImageView) view.findViewById(R.id.foodInfoWindowImage);
            TextView foodName = (TextView) view.findViewById(R.id.foodNameText);
            TextView foodPrice = (TextView) view.findViewById(R.id.foodPriceText);
            TextView foodDistance = (TextView) view.findViewById(R.id.foodDistanceText);

            foodImage.setBackgroundDrawable(getContext().getResources().getDrawable(dish.getIcon()));
            foodName.setText(dish.getName());
            // Fix this to change from $ to other currency if necessary
            foodPrice.setText(String.format("%s%.2f", "$", dish.getPrice()));
            // Fix to change from mi to other units of distance
            foodDistance.setText(String.format("%.2f %s", dish.getDistance(), "mi"));
        }

       return view;
    }
}
