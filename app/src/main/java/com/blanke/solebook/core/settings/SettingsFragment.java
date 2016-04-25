package com.blanke.solebook.core.settings;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.blanke.solebook.R;

public class SettingsFragment extends PreferenceFragment {


    private EditTextPreference mEtPreference;
    private ListPreference mListPreference;
    private CheckBoxPreference mCheckPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefer_settings);
        initPreferences();
    }

    private void initPreferences() {
    }
}