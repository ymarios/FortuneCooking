package com.adl.fortunecooking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResepModel(
    var id:String,
    var title:String,
    var userId:String,
    var ImageUri:String,
    var videoUri:String,
    var rating:String
):Parcelable
//
//@Parcelize
//data class Rating(
//    var id:String,
//    var userId:String,
//    var videoId:String,
//    var rateVal:Float
//):Parcelable