package com.gp2.calcalories.remote.model.user.entity

import com.google.gson.annotations.SerializedName

data class UserDetails(
    @SerializedName("date_of_birth")  val date_of_birth: String,
    @SerializedName("gender")  val gender: String? = "M",  // M=Male, F-Female
    @SerializedName("height") var height: Float = 0f,
    @SerializedName("weight") var weight: Float = 0f,

    @SerializedName("calories")  val calories: Int = 0,
    @SerializedName("vitamin_protein") var vitamin_protein: Float = 0f,
    @SerializedName("vitamin_iron") var vitamin_iron: Float = 0f,
    @SerializedName("vitamin_a") var vitamin_a: Float = 0f,

    @SerializedName("age")  val age: Int = 0,
)
