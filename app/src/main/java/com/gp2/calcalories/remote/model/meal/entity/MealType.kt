package com.gp2.calcalories.remote.model.meal.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MealType(
    @SerializedName("id")  val id:Int,
    @SerializedName("name")  val name:String,
) : Serializable