package com.gp2.calcalories.remote.model.meal.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.meal.entity.MealType
import com.gp2.calcalories.remote.model.general.response.GeneralResponse

class GetMealTypesResponse(
    @SerializedName("male_types")  var male_types: MutableList<MealType>
) : GeneralResponse()