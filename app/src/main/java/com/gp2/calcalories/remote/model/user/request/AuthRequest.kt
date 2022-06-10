package com.gp2.calcalories.remote.model.user.request

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("email") var email: String = "",
    @SerializedName("password") var password: String = "",
)