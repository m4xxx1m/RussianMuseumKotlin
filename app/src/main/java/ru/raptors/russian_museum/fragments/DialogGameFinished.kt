package ru.raptors.russian_museum.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.raptors.russian_museum.R

class DialogGameFinished: DialogFragment() {
    companion object {
        private const val DIALOG_BUNDLE_MESSAGE_KEY = "message"
        @JvmStatic
        fun newInstance(message: String): DialogGameFinished {
            val fragment = DialogGameFinished()
            val args = Bundle()
            args.putString(DIALOG_BUNDLE_MESSAGE_KEY, message)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString(DIALOG_BUNDLE_MESSAGE_KEY)
        val adb = AlertDialog.Builder(activity)
            .setTitle(R.string.well_done)
            .setMessage(message)
            .setPositiveButton(R.string.exit) { _, _ ->
                activity?.finish()
            }
        return adb.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.finish()
    }
}