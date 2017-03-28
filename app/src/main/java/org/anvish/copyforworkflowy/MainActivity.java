package org.anvish.copyforworkflowy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupCheckBox(preferences, R.id.checkBoxOpenWfApp, CopyActivity.prefOpenWfApp, CopyActivity.defaultOpenWfApp);
        setupCheckBox(preferences, R.id.checkBoxOpenWfSite, CopyActivity.prefOpenWfSite, CopyActivity.defaultOpenWfSite);
    }

    private void setupCheckBox(final SharedPreferences preferences, int id, final String key, boolean defaultValue) {
        CheckBox checkBoxOpenWfApp = (CheckBox)findViewById(id);
        checkBoxOpenWfApp.setChecked(preferences.getBoolean(key, defaultValue));
        checkBoxOpenWfApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(key, isChecked);
                editor.commit();
            }
        });
    }
}
