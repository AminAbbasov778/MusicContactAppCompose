package com.example.musiccontactapp.data.repositories

import android.content.Context
import android.provider.ContactsContract
import com.example.musiccontactapp.data.mappers.toDomain
import com.example.musiccontactapp.data.models.Contact
import com.example.musiccontactapp.domain.interfaces.ContactRepository
import com.example.musiccontactapp.domain.models.ContactModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    ContactRepository {
    override fun getContacts(): Result<List<ContactModel>> {

        val contacts = mutableListOf<ContactModel>()
        try {
            val resolver = context.contentResolver

            val cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )

            return cursor?.use {
                val nameIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val phone = it.getString(numberIndex)
                    contacts.add(Contact(name, phone).toDomain())
                }
                Result.success(contacts)
            } ?: Result.failure(Exception("Null"))
        } catch (e: Exception) {
          return  Result.failure(e)
        }


    }


}