package com.gp2.calcalories.remote.model.meal.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MealRecipe(
    @SerializedName("id")  val id:Int,
    @SerializedName("name")  val name:String,
    @SerializedName("details")  val details:String,
    @SerializedName("img_url")  val img_url:String,

    @SerializedName("calories")  val calories: Int = 0,
    @SerializedName("vitamin_protein") var vitamin_protein: Float = 0f,
    @SerializedName("vitamin_iron") var vitamin_iron: Float = 0f,
    @SerializedName("vitamin_a") var vitamin_a: Float = 0f,

    @SerializedName("meal_type") var meal_type: MealType? = null,
) : Serializable