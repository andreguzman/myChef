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

public class PictureTextAdapter extends ArrayAdapter<PictureText> {

    private ArrayList<PictureText> pictureTextList;

    public PictureTextAdapter(Context context, List<PictureText> objects) {
        super(context, 0, objects);
        pictureTextList = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.picture_text_list_item_layout, null);
        }

        PictureText pcitureText = pictureTextList.get(position);

        if (pcitureText != null) {
            ImageView pictureTextImage = (ImageView) view.findViewById(R.id.pictureTextListItemImage);
            TextView pictureTextText = (TextView) view.findViewById(R.id.pictureTextListItemText);

            pictureTextImage.setBackgroundDrawable(getContext().getResources()
                    .getDrawable(pcitureText.getIcon()));
            pictureTextText.setText(pcitureText.getText());
        }

        return view;
    }
}
