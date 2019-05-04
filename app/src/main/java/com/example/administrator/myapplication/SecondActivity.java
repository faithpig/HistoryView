package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ListView listView = (ListView) findViewById(R.id.list_view);
        List<Fruit> data = new ArrayList<>();
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        data.add(new Fruit(R.mipmap.delete, "1111"));
        data.add(new Fruit(R.mipmap.delete, "2222"));
        data.add(new Fruit(R.mipmap.delete, "3333"));
        FruitAdapter adapter = new FruitAdapter(this.getApplicationContext(), R.layout.fruit_list_view_item, data);
        listView.setAdapter(adapter);
    }



}

class Fruit {

    int imageId;
     String text;

    public Fruit(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }
}

class FruitAdapter extends ArrayAdapter<Fruit> {

    private int resourceId;

    public FruitAdapter(@NonNull Context context, int resource, @NonNull List<Fruit> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit f = getItem(position);
        View v;
        ViewHolder viewHolder = new ViewHolder();
        ImageView imageView;
        TextView textView;
        if (null == convertView) {
            v = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            imageView = v.findViewById(R.id.list_item_image);
            textView = v.findViewById(R.id.list_item_text);
            viewHolder.imageView = imageView;
            viewHolder.textView = textView;
            v.setTag(viewHolder);
        } else {
            v = convertView;
            viewHolder = (ViewHolder) v.getTag();
            imageView = viewHolder.imageView;
            textView = viewHolder.textView;
        }
        imageView.setImageResource(f.imageId);
        textView.setText(f.text);
        return v;
    }
}

class ViewHolder {
    ImageView imageView;
    TextView textView;
}