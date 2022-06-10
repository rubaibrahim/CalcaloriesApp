package com.gp2.calcalories.remote.model.plan.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.response.GeneralResponse
import com.gp2.calcalories.remote.model.plan.entity.UserPlan

class GetUserPlansResponse(
    @SerializedName("user_plans")  var user_plans: MutableList<UserPlan>
) : GeneralResponse()