package com.example.face.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.face.R;

import java.util.List;

public class ThingAdapter extends ArrayAdapter<Thing> {
    private int resoureceId;

    public ThingAdapter(@NonNull Context context, int resource, @NonNull List<Thing> objects) {
        super(context, resource, objects);
        resoureceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Thing thing = getItem(position);
        View view;
        ViewHolder viewHolder = null;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resoureceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.thingKeyword = view.findViewById(R.id.tv_thing_keyword);
            viewHolder.thingScore = view.findViewById(R.id.tv_thing_score);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.thingKeyword.setText(thing.getKeyword());
        viewHolder.thingScore.setText(thing.getScore());
        return view;
    }

    public class ViewHolder{
        TextView thingKeyword;
        TextView thingScore;
    }
}
