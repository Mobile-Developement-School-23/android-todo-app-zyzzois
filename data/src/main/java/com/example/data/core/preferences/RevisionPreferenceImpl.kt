package com.example.data.core.preferences

import android.app.Application
import android.content.Context
import javax.inject.Inject

class RevisionPreferenceImpl @Inject constructor(context: Application): RevisionPreference {

    private var preference = context.getSharedPreferences(
        REVISION_TABLE,
        Context.MODE_PRIVATE
    )
    private var editor = preference.edit()

    override fun getRevision(): Int {
        return getValue(REVISION_KEY)
    }

    override fun setRevision(revisionNumber: Int) {
        saveValue(revisionNumber)
    }

    private fun saveValue(value: Int) {
        editor.putInt(REVISION_KEY, value).apply()
    }

    private fun getValue(key: String, defaultValue: Int = 1): Int {
        return preference.getInt(key, defaultValue)
    }

    companion object {
        const val REVISION_TABLE = "revision_table"
        const val REVISION_KEY = "revision"
    }

}