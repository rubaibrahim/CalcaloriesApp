package com.gp2.calcalories.remote.model.plan.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanMeal(
    @SerializedName("id") var id: Int,
    @SerializedName("user_plan_id") var user_plan_id: Int,
    @SerializedName("date") var date: String,
    @SerializedName("name") var name: String,

    @SerializedName("calories") val calories: Int,
    @SerializedName("vitamin_protein") var vitamin_protein: Float,
    @SerializedName("vitamin_iron") var vitamin_iron: Float,
    @SerializedName("vitamin_a") var vitamin_a: Float,

    @SerializedName("updated_at") val created_at: String
) : Serializable