package com.example.pokefi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MonsterAdapter extends BaseAdapter {
    private final Activity activity;
    private final LayoutInflater inflater;
    private final ArrayList<Monster> items = new ArrayList<>();

    public MonsterAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    public void setItems(List<Monster> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Monster getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class Holder {
        TextView name;
        TextView details;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_monster, parent, false);
            holder = new Holder();
            holder.name = convertView.findViewById(R.id.monsterName);
            holder.details = convertView.findViewById(R.id.monsterDetails);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Monster monster = getItem(position);
        holder.name.setText(monster.name + "  ·  " + monster.rarity);
        holder.details.setText(
                "Element: " + monster.element +
                "   |   Signal: " + monster.signal + " dBm" +
                "   |   " + monster.security +
                "   |   " + monster.ssid
        );
        return convertView;
    }
}
