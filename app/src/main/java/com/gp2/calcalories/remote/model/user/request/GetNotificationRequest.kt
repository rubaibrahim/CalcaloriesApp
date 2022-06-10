package com.gp2.calcalories.remote.model.user.request

import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.remote.model.general.request.GeneralRequest

data class GetNotificationRequest(
    @SerializedName("updated_at") var updated_at: String = "",

) : GeneralRequest() {
    override fun toHashMap(): HashMap<String, String> {
        val map = super.toHashMap()
        map["updated_at"] = updated_at
        return map
    }
}