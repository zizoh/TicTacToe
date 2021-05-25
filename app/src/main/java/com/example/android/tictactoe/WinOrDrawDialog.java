package com.example.android.tictactoe;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.ContextThemeWrapper;

public class WinOrDrawDialog extends DialogFragment {

    String mMessage;

    public WinOrDrawDialog(String message) {
        mMessage = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),
                R.style.AlertDialogCustom));
        builder.setMessage(mMessage);

        // Create the AlertDialog
        return builder.create();
    }
}

