package com.gp2.calcalories.remote.model.meal.request

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.request.GeneralRequest

data class PostMealRecipeRequest(
    @SerializedName("vitamins") var vitamins: MutableList<Vitamin>? = mutableListOf(),
) : GeneralRequest()

data class Vitamin(
    @SerializedName("id") var id: String = "",
    @SerializedName("min") var min: String = "",
    @SerializedName("max") var max: String = "",
)