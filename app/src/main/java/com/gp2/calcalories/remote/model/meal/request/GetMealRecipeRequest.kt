package com.gp2.calcalories.remote.model.meal.request

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.request.GeneralRequest

data class GetMealRecipeRequest(
    @SerializedName("meal_type_id") var meal_type_id: String = "",

) : GeneralRequest() {
    override fun toHashMap(): HashMap<String, String> {
        val map = super.toHashMap()
        map["meal_type_id"] = meal_type_id
        return map
    }
}