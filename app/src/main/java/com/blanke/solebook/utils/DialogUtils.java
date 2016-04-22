package com.blanke.solebook.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blanke.solebook.R;
import com.orhanobut.dialogplus.DialogPlus;

/**
 * Created by blanke on 16-3-21.
 */
public class DialogUtils {

    public static void show(Context context, BaseAdapter adapter, int gravity, View headView) {
        show(context, adapter, gravity, headView, android.R.color.white);
    }

    public static void show(Context context, BaseAdapter adapter, int gravity, View headView, int backColorId) {
        DialogPlus.newDialog(context)
                .setContentBackgroundResource(backColorId)
                .setAdapter(adapter)
                .setHeader(headView)
                .setExpanded(true)
                .setGravity(gravity)
                .setCancelable(true)
                .create()
                .show();
    }

    public static void show(Context context, String title, int textColor, int backgroundId, String... data) {
        show(context, Gravity.BOTTOM, title, textColor, backgroundId, data);
    }

    public static void show(Context context, int titleRes, int textColor, int backgroundId, String... data) {
        String title = context.getResources().getString(titleRes);
        show(context, title, textColor, backgroundId, data);
    }

    public static void show(Context context, int gravity, String title, int textColor, int backgroundId, String... data) {
        TextView head = new TextView(context);
        head.setText(title);
        head.setPadding(25, 25, 5, 5);
        head.setTextSize(20);
        int backColor = context.getResources().getColor(backgroundId);
        head.setBackgroundColor(backColor);
        head.setTextColor(context.getResources().getColor(R.color.colorAccent));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                boolean f = true;
                if (convertView == null) {
                    f = false;
                }
                View v = super.getView(position, convertView, parent);
                if (!f) {
                    v.setBackgroundColor(backColor);
                    TextView tv = (TextView) v.findViewById(android.R.id.text1);
                    tv.setTextColor(textColor);
                }
                return v;
            }
        };
        show(context, adapter, gravity, head, backgroundId);
    }
}
