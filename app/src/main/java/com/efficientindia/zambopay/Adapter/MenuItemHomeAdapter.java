package com.efficientindia.zambopay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.efficientindia.zambopay.R;

public class MenuItemHomeAdapter extends ArrayAdapter<String> {
    // private Context mContext;
    private Activity context;
    private final String[] nameItem;
    private final Integer[] imgItem;
    //  private int visibleFlag = 0;
    @Override
    public long getItemId(int position) {
        return 0;
    }
    private static class ViewHolder {
        private TextView textNam;
        private ImageView imgItem;
        // private ImageView imgForward;
        // private TextView txtSkip;
    }
    public MenuItemHomeAdapter(Activity context, String[] nameItem, Integer[] imgItem) {
        super(context, R.layout.item_home, nameItem);
        this.context = context;
        this.nameItem = nameItem;
        this.imgItem = imgItem;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_menu_customer, null);
            viewHolder=new ViewHolder();
            viewHolder. textNam =  convertView.findViewById(R.id.item_text);
            viewHolder. imgItem =  convertView.findViewById(R.id.item_image);
            viewHolder.textNam.setLines(2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //  String string = getItem(position);
        viewHolder.textNam.setText(nameItem[position]);
        viewHolder.imgItem.setImageResource(imgItem[position]);
        return convertView;
    }
}
