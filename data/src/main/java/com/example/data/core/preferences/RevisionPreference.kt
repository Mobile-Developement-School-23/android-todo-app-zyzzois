package com.example.data.core.preferences

interface RevisionPreference {
    fun getRevision(): Int
    fun setRevision(revisionNumber: Int)
}
