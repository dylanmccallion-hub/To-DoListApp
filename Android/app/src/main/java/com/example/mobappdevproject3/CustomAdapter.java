package com.example.mobappdevproject3;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    public Typeface customFont;
    private ArrayList<String> items;

    public CustomAdapter(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_text_view, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setTypeface(customFont); // Set custom font

        // Set the text for the item at the current position
        textView.setText(items.get(position));

        return convertView;
    }
}
