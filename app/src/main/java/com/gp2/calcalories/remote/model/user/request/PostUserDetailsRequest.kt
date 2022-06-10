package com.gp2.calcalories.remote.model.user.request

import com.google.gson.annotations.SerializedName

data class PostUserDetailsRequest(
    @SerializedName("date_of_birth") var date_of_birth: String = "",
    @SerializedName("gender") var gender: String = "M", // M=Male, F-Female
    @SerializedName("height") var height: Float = 0f,
    @SerializedName("weight") var weight: Float = 0f,

    @SerializedName("vitamin_protein") var vitamin_protein: Float = 0f,
    @SerializedName("vitamin_iron") var vitamin_iron: Float = 0f,
    @SerializedName("vitamin_a") var vitamin_a: Float = 0f,
    @SerializedName("update_vitamin") var update_vitamin: Boolean =true,
)
