package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.*
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.btnNegative
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.tvSubTitle
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.tvSubTitle1
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.tvSubTitle2
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.tvSubTitle3
import kotlinx.android.synthetic.main.fragment_simple_dialog.view.tvTitle
import kotlinx.android.synthetic.main.negocio_dialog.view.*

class NegocioDialog : DialogFragment() {

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"
        private const val SUB_SUBTITLE1 = "SUB_SUBTITLE1"
        private const val KEY_SUBTITLE2 = "KEY_SUBTITLE2"
        private const val KEY_SUBTITLE3 = "KEY_SUBTITLE3"
        private const val KEY_SUBTITLE4 = "KEY_SUBTITLE4"


        fun newInstance(title: String, subTitle: String, subTitle1: String, subTitle2: String, subTitle3: String, promedio : Float): NegocioDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            args.putString(SUB_SUBTITLE1, subTitle1)
            args.putString(KEY_SUBTITLE2, subTitle2)
            args.putString(KEY_SUBTITLE3, subTitle3)
            args.putFloat(KEY_SUBTITLE4, promedio)
            val fragment = NegocioDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.negocio_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        view.tvTitle.text = arguments?.getString(KEY_TITLE)
        view.tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)
        view.tvSubTitle1.text = arguments?.getString(SUB_SUBTITLE1)
        view.tvSubTitle2.text = arguments?.getString(KEY_SUBTITLE2)
        view.tvSubTitle3.text = arguments?.getString(KEY_SUBTITLE3)
        view.ratingBar1.rating = arguments?.getFloat(KEY_SUBTITLE4)!!
    }

    private fun setupClickListeners(view: View) {

        view.btnNegative.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
    }

}
