package com.gp2.calcalories.remote.model.meal.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.meal.entity.MealRecipe
import com.gp2.calcalories.remote.model.general.response.GeneralResponse

class GetMealRecipesResponse(
    @SerializedName("meal_recipes")  var meal_recipes: MutableList<MealRecipe>
) : GeneralResponse()