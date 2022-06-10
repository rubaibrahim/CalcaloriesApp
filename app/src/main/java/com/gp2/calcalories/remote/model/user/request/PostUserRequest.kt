package com.gp2.calcalories.remote.model.user.request

import com.google.gson.annotations.SerializedName

data class PostUserRequest(
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("fcm_token") var fcm_token: String = "",
)
