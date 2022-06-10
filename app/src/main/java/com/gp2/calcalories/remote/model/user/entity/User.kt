package com.gp2.calcalories.remote.model.user.entity

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.plan.entity.UserPlan
import java.io.Serializable

data class User(
    @SerializedName("id")  val id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("email")  var email: String,
    @SerializedName("updated_at")  val created_at: String? = "",

    @SerializedName("token")  val token: String,

    @SerializedName("details")  val details: UserDetails?,
    @SerializedName("plans") var plans: MutableList<UserPlan> = mutableListOf()
): Serializable

/*
{
"name":"42343",
"name":"42343",
"name":"42343",
"name":"42343",
"name":"42343",
}
 */