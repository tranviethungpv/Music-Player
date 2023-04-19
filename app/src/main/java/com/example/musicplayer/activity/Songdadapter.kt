package com.example.musicplayer.activity

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class Songdadapter : BaseAdapter, Parcelable {
    constructor() : super()
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Songdadapter> {
        override fun createFromParcel(parcel: Parcel): Songdadapter {
            return Songdadapter(parcel)
        }

        override fun newArray(size: Int): Array<Songdadapter?> {
            return arrayOfNulls(size)
        }
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}