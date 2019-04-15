package elarham.tahfizh.ictaq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import elarham.tahfizh.ictaq.Global.LocaleHelper;

public class SettingsActivity extends AppCompatActivity {

    ActionBar actBar;
    public static final String KEY_PREF_LANGUAGE = "pref_key_language";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.settings));
        actBar.setDisplayHomeAsUpEnabled(true);



        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {


        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            Preference setAccount = findPreference(getActivity().getString(R.string.accountsettingskey));
            setAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent pass = new Intent(getActivity(), Account.class);
                    startActivity(pass);
                    return false;
                }
            });




        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case KEY_PREF_LANGUAGE:
                    LocaleHelper.setLocale(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(key, ""));
                    getActivity().recreate(); // necessary here because this Activity is currently running and thus a recreate() in onResume() would be too late
                    break;
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            // documentation requires that a reference to the listener is kept as long as it may be called, which is the case as it can only be called from this Fragment
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent apply = new Intent(this,MainActivity.class);
        startActivity(apply);
    }


}
