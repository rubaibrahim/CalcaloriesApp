package com.gp2.calcalories.remote.model.general.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class GeneralResponse(
    @SerializedName("status") var status: Int = 0,
    @SerializedName("message") var message: String = "",
    @SerializedName("errors") val errors: HashMap<String, Any> = HashMap()
): Serializable