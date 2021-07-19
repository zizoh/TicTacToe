package com.zizohanto.android.tictactoe

import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class WinOrDrawDialog(@StringRes private val messageId: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return with(AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AlertDialogCustom))) {
            setMessage(messageId)
            create()
        }
    }
}