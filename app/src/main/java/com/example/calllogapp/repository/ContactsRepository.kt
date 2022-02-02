package com.example.calllogapp.repository

interface ContactsRepository {

    fun getNameForNumber(phoneNumber: String): String?
}