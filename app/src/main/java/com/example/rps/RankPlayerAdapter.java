package com.example.rps;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

public class RankPlayerAdapter extends ArrayAdapter <JSONArray> {

    Context context;
    List<JSONArray> objects;
    public RankPlayerAdapter(Context context, int resource, int textViewResourceId, List<JSONArray> objects)
    {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.listview_item, parent, false);
        TextView item_name = (TextView) view.findViewById(R.id.item_name);
        TextView item_points = (TextView) view.findViewById(R.id.item_points);
        TextView item_pos = (TextView) view.findViewById(R.id.item_pos);
        JSONArray temp = objects.get(position);

        try {

            item_name.setText((CharSequence) temp.get(0));
            item_points.setText(String.valueOf(temp.get(1)));
            item_pos.setText(String.valueOf(position + 1)); // gets the index from the get view func = position, pos in json list.
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }


}
