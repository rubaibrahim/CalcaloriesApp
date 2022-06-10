package com.gp2.calcalories.common.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import com.gp2.calcalories.databinding.DialogLoadingBinding

class ProgressDialog(context: Context) : Dialog(context) {

    private var _binding: DialogLoadingBinding? = null
    // This property is only valid between onCreateView and  onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        _binding = DialogLoadingBinding.inflate(layoutInflater)

        setTitle(null);
        setCancelable(true)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}