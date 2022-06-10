package com.gp2.calcalories.common.listner

import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.enums.ProfileSettings


interface GeneralListener {
    fun onClick(action: Actions,data: Any? = null){}
    fun onClick(action: ProfileSettings) {}
}