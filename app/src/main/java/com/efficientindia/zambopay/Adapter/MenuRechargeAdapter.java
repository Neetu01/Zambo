package com.efficientindia.zambopay.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.efficientindia.zambopay.R;

public class MenuRechargeAdapter extends ArrayAdapter<String> {
    private Context mContext;
    Activity context;
    private final String[] nameItem;
    private final Integer[] imgItem;
    private int visibleFlag = 0;

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

    public MenuRechargeAdapter(Activity context, String[] nameItem, Integer[] imgItem) {
        super(context, R.layout.item_home, nameItem);
        this.context = context;
        this.nameItem = nameItem;
        this.imgItem = imgItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_menu, null);
            viewHolder=new ViewHolder();
            viewHolder. textNam = (TextView) convertView.findViewById(R.id.txt_item);
            viewHolder. imgItem = (ImageView) convertView.findViewById(R.id.img_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String string = getItem(position);
        viewHolder.textNam.setText(nameItem[position]);
        viewHolder.imgItem.setImageResource(imgItem[position]);
        return convertView;
    }

}
