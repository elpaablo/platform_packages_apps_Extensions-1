/*
 * Copyright (C) 2014-2016 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aospextended.extensions.categories;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import org.aospextended.extensions.utils.Util;

import org.aospextended.extensions.preference.CardviewPreference;

public class System extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "System";
    private static final String PREF_BATTERY = "battery";
    private static final String PREF_SYSTEM_APP_REMOVER = "system_app_remover";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system);
        setRetainInstance(true);

        ContentResolver resolver = getActivity().getContentResolver();

        Preference mBattery = findPreference(PREF_BATTERY);
        if (mBattery != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_enableSmartPixels)
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            getPreferenceScreen().removePreference(mBattery);
        }

        Preference systemAppRemover = findPreference(PREF_SYSTEM_APP_REMOVER);
        Util.requireRoot(getActivity(), systemAppRemover);
    }


    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EXTENSIONS;
    }

    @Override
    public void onResume() {
        super.onResume();
        themePreferences(getPreferenceScreen());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return true;
    }

    private void themePreferences(PreferenceGroup prefGroup) {
        themePreference(prefGroup);
        for (int i = 0; i < prefGroup.getPreferenceCount(); i++) {
            Preference pref = prefGroup.getPreference(i);
            if (pref instanceof PreferenceGroup) {
                themePreferences(prefGroup);
            } else {
                themePreference(pref);
            }
        }
    }

    private void themePreference(Preference pref) {
        if (pref instanceof CardviewPreference) {
            CardviewPreference card = (CardviewPreference) pref;
            card.updateTheme();
        }
    }
}
