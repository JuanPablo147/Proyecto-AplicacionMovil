package com.example.myapplication

import android.content.ContentProvider
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.api.Context
import java.security.Provider

class PreferencesData(val name:String, val vip:Boolean,val id_name :String) {

    private suspend fun saveValues(name:String,checked:Boolean,context: ContentProvider){
        val pref = requireContext(context).dataStore
        pref.edit { preferences ->
            preferences[stringPreferencesKey("name")] = name
            preferences[booleanPreferencesKey("vip")] = checked
        }

    }
}