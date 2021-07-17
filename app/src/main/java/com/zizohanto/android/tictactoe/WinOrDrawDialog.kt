package com.zizohanto.android.tictactoe

import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class WinOrDrawDialog(var mMessage: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(ContextThemeWrapper(activity,
                R.style.AlertDialogCustom))
        builder.setMessage(mMessage)

        // Create the AlertDialog
        return builder.create()
    }
}