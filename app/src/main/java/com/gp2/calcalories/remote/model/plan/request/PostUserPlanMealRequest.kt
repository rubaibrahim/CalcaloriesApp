package com.gp2.calcalories.remote.model.plan.request

import com.google.gson.annotations.SerializedName

data class PostUserPlanMealRequest(
    @SerializedName("user_plan_id") var user_plan_id: Int = 0,
//    @SerializedName("date") var date: String = "",

    @SerializedName("name") var name: String = "",

    @SerializedName("calories") var calories: Int = 0,
    @SerializedName("vitamin_protein") var vitamin_protein: Float = 0f,
    @SerializedName("vitamin_iron") var vitamin_iron: Float = 0f,
    @SerializedName("vitamin_a") var vitamin_a: Float = 0f,

    @SerializedName("updated_at") var updated_at: String = "",
)
