package com.blanke.solebook.core.settings;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;

import com.blanke.solebook.R;

import me.imid.swipebacklayout.lib.app.SwipeBackPreferenceActivity;

public class SettingsActivity extends SwipeBackPreferenceActivity{


    private EditTextPreference mEtPreference;
    private ListPreference mListPreference;
    private CheckBoxPreference mCheckPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initPreferences();
    }

    private void initPreferences() {
        mEtPreference = (EditTextPreference)findPreference(Consts.EDIT_KEY);
        mListPreference = (ListPreference)findPreference(Consts.LIST_KEY);
        mCheckPreference = (CheckBoxPreference)findPreference(Consts.CHECKOUT_KEY);
    }
}