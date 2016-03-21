package com.blanke.solebook.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.orhanobut.dialogplus.DialogPlus;

/**
 * Created by blanke on 16-3-21.
 */
public class DialogUtils {

    public static void show(Context context, BaseAdapter adapter, int gravity) {
        DialogPlus.newDialog(context)
                .setAdapter(adapter)
                .setExpanded(true)
                .setGravity(gravity)
                .create()
                .show();
    }

    public static void show(Context context, String... data) {
        show(context, Gravity.BOTTOM, data);
    }

    public static void show(Context context, int gravity, String... data) {
        show(context,
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data)
                , gravity);
    }
}
