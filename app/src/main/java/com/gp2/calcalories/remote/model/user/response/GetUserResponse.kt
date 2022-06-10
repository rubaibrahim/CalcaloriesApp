package com.gp2.calcalories.remote.model.user.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.response.GeneralResponse
import com.gp2.calcalories.remote.model.user.entity.User

class GetUserResponse(
    @SerializedName("user")  var user: User?
) : GeneralResponse()
