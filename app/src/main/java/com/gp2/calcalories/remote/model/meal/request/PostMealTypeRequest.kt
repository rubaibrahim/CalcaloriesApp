package com.gp2.calcalories.remote.model.meal.request

import com.google.gson.annotations.SerializedName

data class PostMealTypeRequest(
    @SerializedName("image") val image: String,
)
