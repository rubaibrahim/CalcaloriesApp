package com.gp2.calcalories.remote.model.meal.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.meal.entity.MealType
import com.gp2.calcalories.remote.model.general.response.GeneralResponse

class PostMealTypeResponse(
    @SerializedName("data") var data: Data,

    ) : GeneralResponse()

class Data(
    @SerializedName("img_url") var img_url: String,
    @SerializedName("google_url") var google_url: String,
)