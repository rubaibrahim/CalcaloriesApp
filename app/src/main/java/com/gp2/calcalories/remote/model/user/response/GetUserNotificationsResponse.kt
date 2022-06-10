package com.gp2.calcalories.remote.model.user.response

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.response.GeneralResponse
import com.gp2.calcalories.remote.model.meal.entity.MealRecipe
import com.gp2.calcalories.remote.model.user.entity.User
import com.gp2.calcalories.remote.model.user.entity.UserNotification

class GetUserNotificationsResponse(
    @SerializedName("user_notifications")  var user_notifications: MutableList<UserNotification>
) : GeneralResponse()
