package com.example.administrator.sih2018; /**
 * Created by Administrator on 3/31/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sih2018.R;

//import com.example.android.sih_sample.R;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] code;
    private final String[] type;
    private final Integer[] imgid;

    public MySimpleArrayAdapter (Context context, String[] values,String[] type, String[] code, Integer[] imgid) {
        super(context, R.layout.mylist, values);
        this.context = context;
        this.values = values;
        this.code = code;
        this.type = type;
        this.imgid = imgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mylist, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView textView2 = (TextView) rowView.findViewById(R.id.type);
        TextView textView3 = (TextView) rowView.findViewById(R.id.code);

        textView.setText(values[position]);
        imageView.setImageResource(imgid[position]);
        textView3.setText(code[position]);
        textView2.setText(type[position]);


        // Change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            //imageView.setImageResource(R.drawable.no);
        } else {
            //imageView.setImageResource(R.drawable.ok);
        }

        return rowView;
    }




}
