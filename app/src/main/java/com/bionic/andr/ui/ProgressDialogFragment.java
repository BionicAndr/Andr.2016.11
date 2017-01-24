package com.bionic.andr.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**  */
public class ProgressDialogFragment extends DialogFragment {

    public static final String ARG_TEXT = "arg:text";

    public static ProgressDialogFragment newInstance(String text) {
        final ProgressDialogFragment f = new ProgressDialogFragment();
        final Bundle args = new Bundle(1);
        args.putString(ARG_TEXT, text);
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        String text = getArguments().getString(ARG_TEXT);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(text);
        return dialog;
    }
}
