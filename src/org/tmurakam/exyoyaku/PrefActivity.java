
package org.tmurakam.exyoyaku;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity {
    public final static String PREF_NAME = "userConfig";

    public final static String PREF_KEY_USERID = "UserId";

    public final static String PREF_KEY_PASSWORD = "Password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // backward compat.
        getPreferenceManager().setSharedPreferencesName(PREF_NAME);

        addPreferencesFromResource(R.xml.preferences);
    }
}
