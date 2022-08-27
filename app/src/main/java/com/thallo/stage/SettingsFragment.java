package com.thallo.stage;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference addons=findPreference("settingAddons");
        Preference about=findPreference("settingAbout");
        Preference feedback=findPreference("setting_feedback");
        ListPreference searchEngine=findPreference("searchEngine");

        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), FragmentHolder.class);
                intent.putExtra("page","ABOUT");
                getContext().startActivity(intent);
                return false;
            }
        });
        addons.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), FragmentHolder.class);
                intent.putExtra("page","ADDONS");
                getContext().startActivity(intent);
                return false;
            }
        });

        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                BaseActivity.url="https://support.qq.com/product/427204";
                Intent intent = new Intent(getContext(), BaseActivity.class);
                getContext().startActivity(intent);
                return false;
            }
        });





    }



}