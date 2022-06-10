package com.gp2.calcalories.remote.model.general.request

import com.google.gson.annotations.SerializedName

open class GeneralRequest(
    @SerializedName("search") var search: String = "",
    @SerializedName("page") var page: Int = 0,  // page number
) {
    open fun toHashMap(): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["search"] = search
        map["page"] = page.toString()
        return map
    }
}