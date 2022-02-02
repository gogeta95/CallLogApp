package com.example.calllogapp.repository

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup


class ContactsRepositoryImpl(private val context: Context) : ContactsRepository {

    override fun getNameForNumber(phoneNumber: String): String? {

        val uri: Uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cursor =
            context.contentResolver.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null)
                ?: return null

        var contactName: String? = null
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME))
        }
        cursor.close()

        return contactName
    }
}