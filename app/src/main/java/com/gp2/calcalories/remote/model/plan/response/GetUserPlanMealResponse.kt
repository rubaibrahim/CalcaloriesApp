package com.gp2.calcalories.remote.model.plan.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.response.GeneralResponse
import com.gp2.calcalories.remote.model.plan.entity.PlanMeal
import com.gp2.calcalories.remote.model.plan.entity.UserPlan

class GetUserPlanMealResponse(
    @SerializedName("user_plan_meal")  var user_plan_meal: PlanMeal
) : GeneralResponse()