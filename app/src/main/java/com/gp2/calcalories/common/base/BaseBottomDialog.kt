package com.gp2.calcalories.common.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gp2.calcalories.R
import com.gp2.calcalories.common.enums.Event
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.preference.UserPreferences
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.ProgressDialog
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.remote.config.AppRepository

open class BaseBottomDialog : BottomSheetDialogFragment() {
    protected var session: UserPreferences? = null
    protected var repository: AppRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        session = UserPreferences.getInstance(requireContext())
        repository = AppRepository.getInstance()
    }
}