package com.gp2.calcalories.common.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gp2.calcalories.common.enums.Event
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.preference.UserPreferences
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.ProgressDialog
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.remote.config.AppRepository

open class BaseFragment : Fragment() {
    protected var session: UserPreferences? = null
    protected var progress: ProgressDialog? = null
    protected var repository: AppRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = UserPreferences.getInstance(requireContext())
        progress = ProgressDialog(requireContext())
        repository = AppRepository.getInstance()
    }
}