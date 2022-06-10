package com.gp2.calcalories.remote.model.user.entity

import com.google.gson.annotations.SerializedName

data class UserNotification(
    @SerializedName("id")  val id: Int,
    @SerializedName("type")  val type: Int,  // 1=notify, 2=action
    @SerializedName("title") var title: String,
    @SerializedName("message") var message: String,
    @SerializedName("ids")  val ids: String,  // [] : 1=protein , 2=iron , 3=a
    @SerializedName("is_read")  val is_read: Int,  // 1=Yes, 0=No
    @SerializedName("updated_at") var date: String,
)
