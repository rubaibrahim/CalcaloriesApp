package com.gp2.calcalories.remote.model.plan.entity

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.meal.entity.MealType
import com.gp2.calcalories.remote.model.user.entity.User
import java.io.Serializable

data class UserPlan(
    @SerializedName("id")  val id: Int,
    @SerializedName("calories")  val calories: Int,
    @SerializedName("vitamin_protein") var vitamin_protein: Float,
    @SerializedName("vitamin_iron") var vitamin_iron: Float,
    @SerializedName("vitamin_a") var vitamin_a: Float,

    @SerializedName("meal_type") var meal_type: MealType? = null,
    @SerializedName("totals") var totals: PlanTotals? = null,
    @SerializedName("user") var user: User? = null,

    @SerializedName("meals") var meals: MutableList<PlanMeal>? = mutableListOf()
):Serializable