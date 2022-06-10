package com.gp2.calcalories.common.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.gp2.calcalories.common.enums.ProfileSettings

@Keep
data class ProfileSetting(
    @SerializedName("id")  val id:ProfileSettings,
    @SerializedName("title")  val title:String,
)
