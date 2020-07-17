package com.example.testaidlservice

import android.os.Parcel
import android.os.Parcelable

class Book() :Parcelable {

    var bookName:String? = null

    constructor(parcel: Parcel) : this() {
        bookName = parcel.readString()
    }

    constructor(name: String) : this() {
        bookName = name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bookName)
    }

    fun readFromParcel(desc: Parcel) {
        bookName = desc.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}