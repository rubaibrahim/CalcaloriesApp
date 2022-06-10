package com.gp2.calcalories.remote.model.plan.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanTotals(
    @SerializedName("used_calories") var used_calories:Int,
    @SerializedName("used_vitamin_protein") var used_vitamin_protein:Float,
    @SerializedName("used_vitamin_iron") var used_vitamin_iron:Float,
    @SerializedName("used_vitamin_a") var used_vitamin_a:Float,

    @SerializedName("remaining_calories") var remaining_calories:Int,
    @SerializedName("remaining_vitamin_protein") var remaining_vitamin_protein:Float,
    @SerializedName("remaining_vitamin_iron") var remaining_vitamin_iron:Float,
    @SerializedName("remaining_vitamin_a") var remaining_vitamin_a:Float,
):Serializable