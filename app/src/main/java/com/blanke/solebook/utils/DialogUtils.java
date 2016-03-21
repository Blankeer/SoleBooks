package com.blanke.solebook.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
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
        DialogPlus.newDialog(context)
                .setAdapter(adapter)
                .setHeader(headView)
                .setExpanded(true)
                .setGravity(gravity)
                .setCancelable(true)
                .create()
                .show();
    }

    public static void show(Context context, String title, String... data) {
        show(context, Gravity.BOTTOM, title, data);
    }

    public static void show(Context context, int titleRes, String... data) {
        String title = context.getResources().getString(titleRes);
        show(context, title, data);
    }

    public static void show(Context context, int gravity, String title, String... data) {
        TextView head = new TextView(context);
        head.setText(title);
        head.setPadding(25, 25, 5, 5);
        head.setTextSize(20);
        head.setTextColor(context.getResources().getColor(R.color.colorAccent));
        show(context,
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data)
                , gravity
                , head);
    }

    public static void show(Context context, int gravity, int titleRes, String... data) {
        String title = context.getResources().getString(titleRes);
        show(context, gravity, title, data);
    }
}
